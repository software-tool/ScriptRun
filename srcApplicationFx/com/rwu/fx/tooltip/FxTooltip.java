package com.rwu.fx.tooltip;

import com.rwu.fx.util.FxFontUtils;
import com.rwu.log.Log;
import com.rwu.misc.ListUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Own tooltips, because performance issues with default implementation
 * 
 * Info: https://blog.ngopal.com.np/2011/01/03/custom-tooltip-on-javafx/
 */
public class FxTooltip {

	// Margin to window border right
	private static int RIGHT_END_XOFFSET = 25;
	private static int BOTTOM_END_OFFSET = 35;

	// Tooltip-Offset Y
	private static int OFFSET_Y = 20;

	private static FxTooltipNode node;

	private static MouseEvent event;
	private static String text;

	private static FxTooltipThread thread = new FxTooltipThread();
	private static FxTooltipCloseThread closeThread = new FxTooltipCloseThread();

	private static EventHandler<MouseEvent> handlerExited;
	private static EventHandler<MouseEvent> handlerEntered;

	private static WeakHashMap<Node, String> handlerTexts = new WeakHashMap<>();

	// Time of init
	public static long lastInitShowing = -1;

	/**
	 * Set tooltip
	 */
	public static void setTooltip(Node button, String text) {
		apply(button, text);
	}

	public static void init() {
		thread.start();
		closeThread.start();

		handlerEntered = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Object source = event.getSource();

				Node control = (Node) source;
				String text = handlerTexts.get(control);

				if (text != null) {
					requestShow(control, event, text);
				}
			}
		};

		handlerExited = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hideNow(false);
			}
		};
	}

	public static void stopThread() {
		thread.stopNow();
		closeThread.stopNow();
	}

	public static void apply(Node control, String text) {
		String oldText = handlerTexts.get(control);

		if (text != null) {
			if (oldText == null) {
				// No listener yet

				control.addEventHandler(MouseEvent.MOUSE_ENTERED, handlerEntered);
				control.addEventHandler(MouseEvent.MOUSE_EXITED, handlerExited);
			}

			handlerTexts.put(control, text);
		} else {
			String textBefore = handlerTexts.get(control);

			if (textBefore != null) {
				// Entered
				control.removeEventHandler(MouseEvent.MOUSE_ENTERED, handlerEntered);
				// Exited
				control.removeEventHandler(MouseEvent.MOUSE_EXITED, handlerExited);

				handlerTexts.remove(control);
			}
		}
	}

	/**
	 * Close
	 */
	public static void hideNow(boolean force) {
		if (!force) {
			// Not directly

			long now = System.currentTimeMillis();
			long diff = now - lastInitShowing;

			if (diff < FxTooltipThread.MINIMUM_SHOW_DURATION) {
				// Visible to short time, do not hide now but earlier
				FxTooltipCloseThread.hideAfterTime = (now + FxTooltipThread.SHORT_DURATION);

				// Mouse out: Do not hide if only some ms visible
				return;
			}
		}

		if (node != null) {
			node.setVisible(false);
		}

		if (force) {
			lastInitShowing = -1;
		}

		thread.resetTriggers();
	}

	private static void requestShow(Node control, MouseEvent eventNew, String textNew) {
		event = eventNew;
		text = textNew;

		thread.trigger(control);
	}

	public static void triggerShow() {
		// Store time for being triggered
		lastInitShowing = System.currentTimeMillis();

		Node parent = thread.getCurrentControl();

		List<Pane> panes = new ArrayList<>();
		while (parent != null) {
			if (parent instanceof Pane) {
				Pane pane = (Pane) parent;
				panes.add(pane);
			}

			parent = parent.getParent();
		}

		Pane last = ListUtils.getLast(panes);
		if (last == null) {
			Log.warn("Cannot set tooltip: " + parent);
		} else {
			//System.out.println("Apply: " + last);

			apply(last, event, text);
		}
	}

	public static long getLastInitShowing() {
		return lastInitShowing;
	}

	public static boolean isShowing() {
		return lastInitShowing != -1;
	}

	private static void apply(Pane pane, MouseEvent event, String text) {
		Platform.runLater(() -> {
			if (node != null) {
				node.parent.getChildren().remove(node);
			}

			node = new FxTooltipNode();
			node.parent = pane;
			node.setText(text);

			configure(node, event);

			pane.getChildren().add(node);

			// Correct position, if necessary

			ensurePosition(pane);
		});
	}

	private static void ensurePosition(Pane pane) {
		Bounds bounds = FxFontUtils.calculateBounds(text, node.label.getFont());

		double width = bounds.getWidth() + RIGHT_END_XOFFSET;
		double height = bounds.getHeight() + BOTTOM_END_OFFSET;

		double paneWidth = pane.getWidth();
		double paneHeight = pane.getHeight();

		double right = node.getLayoutX() + width;
		double diffX = right - paneWidth;

		double bottom = node.getLayoutY() + height;
		double diffY = bottom - paneHeight;

		if (diffX > 0) {
			node.setLayoutX(node.getLayoutX() - diffX);
		}
		if (diffY > 0) {
			node.setLayoutY(node.getLayoutY() - diffY);
		}
	}

	private static void configure(FxTooltipNode node, MouseEvent event) {
		double x = event.getSceneX();
		double y = event.getSceneY();

		y += OFFSET_Y;

		node.setLayoutX(x);
		node.setLayoutY(y);

		node.setManaged(false);

	}
}
