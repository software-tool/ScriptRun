package script.execute;

import com.rwu.application.lang.LangHelper.Language;
import com.rwu.log.Log;
import script.Input;
import script.Output;
import script.execute.intf.IScriptImpl;
import script.execute.result.ScriptError;
import script.execute.result.ScriptErrorType;
import script.execute.result.ScriptRunning;
import script.groovy.GroovyScript;
import script.manager.ScriptReader;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;
import script.util.Multiple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptRun {

	/** Automatic filename  */
	private static String DEFAULT_FILENAME = "Script1.groovy";

	private static int SCRIPT_SYSTEM_WAIT_TIME = 3000;

	private static int SCRIPT_LOOP_COUNT_FAST = 5;
	private static int SCRIPT_LOOP_COUNT_NORMAL = 60 - 1 - 3; // Seconds

	private static int SCRIPT_LOOP_COUNT = SCRIPT_LOOP_COUNT_FAST + SCRIPT_LOOP_COUNT_NORMAL;

	private static int SCRIPT_LOOP_MAX = SCRIPT_LOOP_COUNT + 10;

	private static int SCRIPT_LOOP_SLEEP_TIME_FAST = 200;
	private static int SCRIPT_LOOP_SLEEP_TIME_DEFAULT = 1000;

	/**
	 * Run script
	 */
	public static ScriptStartResult runScriptInThread(Language language, ScriptInst scriptInst, ScriptFile scriptFile) {
		String content = ScriptReader.readScript(scriptFile);
		if (content == null) {
			return new ScriptStartResult(new ScriptRunning(getError(null, ScriptErrorType.COMPILE, null)), null);
		}

		scriptInst.applyContent(content);

		IScriptImpl scriptImpl = getGroovy(scriptFile.getFile(), scriptInst);

		try {
			// Inputs
			Input input = scriptInst.applyInputs(scriptFile.getInputs());

			// Outputs
			Output output = scriptInst.initOutput(scriptFile.getOutput());

			scriptInst.setExecution(input, output);
			scriptInst.setOutput();

			// Shell
			scriptImpl.prepare();

			// Variables
			//scriptInst.bindVariables(script);
		} catch (Throwable t) {
			//Log.info("Failed to read groovy script: " + t.getMessage());

			return new ScriptStartResult(new ScriptRunning(getError(null, ScriptErrorType.COMPILE, t)), null);
		}

		if (scriptImpl.isValid()) {
			// Save input as recent
			ScriptRecent recent = scriptFile.asRecent();
			ScriptRecentManager.addRecent(recent);

			ScriptInstThread newThread = new ScriptInstThread(scriptInst.getInstId(), scriptImpl);
			newThread.start();

			return new ScriptStartResult(new ScriptRunning(scriptFile, newThread), recent);

			//			try {
			//				// System awaits termination
			//				newThread.join(SCRIPT_SYSTEM_WAIT_TIME);
			//			} catch (InterruptedException e1) {
			//				// Ignore
			//			}
			//
			//			for (int i = 1; i <= SCRIPT_LOOP_MAX; i++) {
			//				if (!newThread.isAlive()) {
			//					// Ended
			//					break;
			//				}
			//
			//				long sleepTime = SCRIPT_LOOP_SLEEP_TIME_DEFAULT;
			//				if (i <= SCRIPT_LOOP_COUNT_FAST) {
			//					sleepTime = SCRIPT_LOOP_SLEEP_TIME_FAST;
			//				}
			//
			//				try {
			//					// Wait
			//					Thread.sleep(sleepTime);
			//				} catch (InterruptedException e) {
			//					// Ignore
			//				}
			//
			//				if (i >= SCRIPT_LOOP_COUNT) {
			//					// Takes too long, abort
			//
			//					// Force abort
			//					try {
			//						newThread.stop();
			//					} catch (Throwable t) {
			//						LogHandler.info("Failed to stop script thread: " + t.getMessage());
			//					}
			//
			//					return getError(ln, ScriptErrorType.TIMEOUT, null);
			//				}
			//			}

			//			ScriptError error = newThread.getError();
			//			if (error != null) {
			//				return error;
			//			}
		}

		return null;
	}

	/**
	 * New object for Script-Error
	 */
	public static ScriptError getError(Language language, ScriptErrorType type, Throwable t) {
		String message = null;
		String details = null;

		StringBuilder lineNumberString = new StringBuilder();

		if (t != null) {
			message = t.getMessage() + " (" + t.getClass().getSimpleName() + ")";

			// Line numbers
			List<Integer> lineNumbers = getLineNumbers(t);
			if (!lineNumbers.isEmpty()) {
				lineNumberString.append(lineNumbers.get(0));

				if (lineNumbers.size() > 1) {
					lineNumberString.append(" (=> ");

					for (int i = 1; i < lineNumbers.size(); i++) {
						if (i > 1) {
							lineNumberString.append(", ");
						}

						lineNumberString.append(lineNumbers.get(i));
					}

					lineNumberString.append(")");
				}
			}

			details = message;
		}

		if (type == ScriptErrorType.TIMEOUT) {
			// TODO: Message
			//message = L.get(ln, L.ScriptErrorTimeout);
		}

		ScriptError error = new ScriptError(type, message, details, lineNumberString.toString());
		return error;
	}

	private static List<Integer> getLineNumbers(Throwable t) {
		StackTraceElement[] stackTraceElements = t.getStackTrace();

		List<Integer> numbers = new ArrayList<Integer>();

		for (StackTraceElement element : stackTraceElements) {
			String filename = element.getFileName();
			int lineNumber = element.getLineNumber();

			// filename: "Script1.groovy"
			if (DEFAULT_FILENAME.equals(filename)) {
				numbers.add(lineNumber);
			}
		}

		return numbers;
	}

	private static GroovyScript getGroovy(File file, ScriptInst scriptInst) {
		GroovyScript script = new GroovyScript(file, scriptInst);

		return script;
	}
}
