package script.execute.result;

import script.execute.ScriptFile;
import script.execute.ScriptInstThread;

import java.util.ArrayList;
import java.util.List;

public class ScriptRunning {

	private ScriptError error = null;
	private ScriptInstThread thread = null;

	private ScriptFile scriptFile;

	public ScriptRunning(ScriptError error) {
		this.error = error;
	}

	public ScriptRunning(ScriptFile scriptFile, ScriptInstThread thread) {
		this.scriptFile = scriptFile;
		this.thread = thread;
	}

	public ScriptInstThread getThread() {
		return thread;
	}

	public void interruptThread() {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
	}

	public boolean isRunning() {
		if (thread == null) {
			return false;
		}

		return thread.isAlive();
	}

	public List<ScriptError> getAndResetError() {
		List<ScriptError> errors = new ArrayList();

		if (thread != null) {
			ScriptError threadError = thread.getAndResetError();
			if (threadError != null) {
				errors.add(threadError);
			}
		}

		if (error != null) {
			errors.add(error);
		}

		this.error = null;
		return errors;
	}

	public ScriptFile getScriptFile() {
		return scriptFile;
	}
}
