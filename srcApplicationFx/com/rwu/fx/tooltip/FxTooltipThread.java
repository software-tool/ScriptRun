package com.rwu.fx.tooltip;

import com.rwu.misc.EqualsUtil;

import javafx.scene.Node;
import javafx.scene.control.Control;

public class FxTooltipThread extends Thread {

	private static long SHOW_DELAY = 500;
	private static long REINIT_DURATION = 4000;
	private static long SHOW_DURATION = 8000;

	public static long MINIMUM_SHOW_DURATION = 500;

	public static long SHORT_DURATION = 1800;

	private boolean running = true;

	private Object obj = new Object();

	private Node currentControl = null;

	private Node beforeWait = null;

	private boolean sleep = true;

	@Override
	public void run() {
		setName("tooltip-show");

		while (true) {
			try {
				if (sleep) {
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

			beforeWait = currentControl;

			try {
				sleep(SHOW_DELAY);
			} catch (InterruptedException e) {
				// Ignore
			}

			//System.out.println("Check: " + currentControl + ", " + beforeWait);

			if (beforeWait != null && EqualsUtil.isEqual(beforeWait, currentControl)) {
				// Tooltip should be shown

				//System.out.println("+++++++++ Trigger: " + currentControl);

				hideInXSeconds();

				FxTooltip.triggerShow();

				currentControl = null;
			}

			if (currentControl != null) {
				sleep = false;
			} else {
				sleep = true;
			}
		}
	}

	public void trigger(Node control) {
		long now = System.currentTimeMillis();
		long diff = now - FxTooltip.getLastInitShowing();

		this.currentControl = control;

		if (beforeWait != control) {
			// Other control
			beforeWait = null;
		}

		//System.out.println("currentControl: " + currentControl + ", showing: " + FxTooltip.isShowing() + ", diff: " + diff);

		if (FxTooltip.isShowing() && diff < REINIT_DURATION) {
			// Retrigger until tooltip active

			hideInXSeconds();

			// Same/other control
			FxTooltip.triggerShow();
			return;
		}

		synchronized (obj) {
			obj.notify();
		}
	}

	/**
	 * Close in x seconds
	 */
	public void hideInXSeconds() {
		long now = System.currentTimeMillis();
		long hideAfterTime = now + SHOW_DURATION;

		FxTooltipCloseThread.hideAfterTime = hideAfterTime;

		FxTooltipCloseThread.INST.wakeUp();
	}

	public void resetTriggers() {
		currentControl = null;
		//System.out.println("## no control");

		sleep = true;
	}

	public void stopNow() {
		running = false;

		resetTriggers();

		synchronized (obj) {
			obj.notify();
		}

		interrupt();
	}

	public Node getCurrentControl() {
		return currentControl;
	}

}
