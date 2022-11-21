package com.rwu.fx.tooltip;

public class FxTooltipCloseThread extends Thread {

	private Object obj = new Object();

	private boolean running = true;

	private boolean active = false;

	public static long hideAfterTime = -1;

	public static FxTooltipCloseThread INST = null;

	public FxTooltipCloseThread() {
		INST = this;

		setName("tooltip-close");
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (!active) {
					synchronized (obj) {
						obj.wait();
					}
				}
			} catch (InterruptedException e) {
				// Ignore
			}

			if (!running) {
				break;
			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// Ignore
			}

			long now = System.currentTimeMillis();
			if (now > hideAfterTime) {
				FxTooltip.hideNow(true);

				active = false;
			}
		}
	}

	public void wakeUp() {
		if (active) {
			return;
		}

		synchronized (obj) {
			obj.notify();

			active = true;
		}
	}

	public void stopNow() {
		running = false;

		INST = null;

		synchronized (obj) {
			obj.notify();

			active = true;
		}

		interrupt();
	}
}
