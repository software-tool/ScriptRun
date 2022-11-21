package script.execute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rwu.log.Log;
import script.frame.state.OpenedPanes;
import xml.LogHandler;

public class ScriptMonitorThread extends Thread {

	private boolean running = true;

	private List<ScriptInstThread> threadsRunning = new ArrayList<>();

	public static ScriptMonitorThread inst = null;

	public ScriptMonitorThread() {
		inst = this;
	}

	/**
	 * New thread is started
	 */
	public synchronized void threadStarting(ScriptInstThread newThread) {
		threadsRunning.add(newThread);
	}

	public void shutdown() {
		running = false;

		inst = null;
	}

	@Override
	public void run() {
		super.run();

		while (true) {
			if (!running) {
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.error("Script monitor thread interrupted", e);
			}

			// Files saved outside of the application
			OpenedPanes.tickScriptFilesSavedOutside();

			checkThreads();
		}
	}

	private synchronized void checkThreads() {
		Iterator<ScriptInstThread> iterator = threadsRunning.iterator();

		while (iterator.hasNext()) {
			ScriptInstThread thread = iterator.next();

			if (!thread.isAlive()) {
				//System.err.println("Thread finished: " + thread);

				// Ready
				iterator.remove();
				continue;
			}

			// No maximum runtime
			//double seconds = thread.getSecondsRunning();
			//System.err.println("seconds: " + seconds);
			//			if (seconds > SCRIPT_MAX_SECONDS) {
			//				System.err.println("STOP: " + seconds);
			//
			//				thread.interrupt();
			//			}
		}
	}


}
