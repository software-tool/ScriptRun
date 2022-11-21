package script.repeat;

import java.util.ArrayList;
import java.util.List;

import com.rwu.log.Log;
import xml.LogHandler;

public class RepeatThread extends Thread {

	public static RepeatThread inst = null;

	private boolean running = true;

	private List<IRepeatListeners> listeners = new ArrayList<>();

	public RepeatThread() {
		inst = this;
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
				Log.error("Repeat thread interrupted", e);
			}

			tick();
		}
	}

	private void tick() {
		for (IRepeatListeners listener : listeners) {
			listener.tick();
		}
	}

	public void addListener(IRepeatListeners listener) {
		removeListener(listener);

		this.listeners.add(listener);
	}

	public void removeListener(IRepeatListeners listener) {
		while (listeners.remove(listener)) {
			// Remove all
		}
	}

	public void shutdown() {
		running = false;

		inst = null;
	}
}
