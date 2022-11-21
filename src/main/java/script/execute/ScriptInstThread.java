package script.execute;

import script.controller.ControllerRunning;
import script.execute.intf.IScriptImpl;
import script.execute.result.ScriptError;
import script.execute.result.ScriptErrorType;
import script.execute.result.ScriptRunning;

public class ScriptInstThread extends Thread {

	private int instId;
	private IScriptImpl script;

	private ScriptError error;

	private long startTime = -1;

	public ScriptInstThread(int instId, IScriptImpl script) {
		this.instId = instId;
		this.script = script;
	}

	public ScriptError getAndResetError() {
		ScriptError result = error;
		this.error = null;
		return result;
	}

	@Override
	public void run() {
		// Start time
		startTime = System.currentTimeMillis();

		// Monitor
		ScriptMonitorThread.inst.threadStarting(this);

		try {
			script.run();

			//String output = script.getOutput();

			//System.out.println("output: " + output);
		} catch (Throwable t) {
			//LogHandler.warn("Failed to run groovy script: " + t.getClass().getSimpleName(), t);

			error = ScriptRun.getError(null, ScriptErrorType.RUN, t);

			ControllerRunning.reportScriptFinished(instId);
		}
	}

	public double getSecondsRunning() {
		long now = System.currentTimeMillis();

		long diff = (now - startTime);
		double seconds = diff / 1000.0;

		return seconds;
	}

	public long getStartTime() {
		return startTime;
	}

	public boolean matchInstId(int other) {
		return instId == other;
	}
}
