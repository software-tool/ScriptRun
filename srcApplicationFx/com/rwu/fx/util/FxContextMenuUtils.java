package com.rwu.fx.util;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;

public class FxContextMenuUtils {

	/**
	 * Show context menu
	 */
	public static void showContextMenu(ContextMenu context, Node node) {
		showContextMenu(context, node, false, null, null);
	}

	public static void showContextMenuTop(ContextMenu context, Node node) {
		showContextMenu(context, node, true, null, null);
	}

	public static void showContextMenuRight(ContextMenu context, Node node) {
		showContextMenu(context, node, false, 120d, 0d);
	}

	/**
	 * KShow context menu for right click
	 */
	public static void showContextMenu(ContextMenuEvent event, ContextMenu context, Node node) {
		showContextMenu(context, node, false, event.getX(), event.getY());
	}

	private static void showContextMenu(ContextMenu context, Node node, boolean top, Double offsetX, Double offsetY) {
		Bounds bounds = node.getBoundsInLocal();
		Bounds screenBounds = node.localToScreen(bounds);

		double width = screenBounds.getWidth();
		double heigth = screenBounds.getHeight();

		double x = screenBounds.getMinX();
		double y = screenBounds.getMinY();

		if (offsetX == null && offsetY == null) {
			x += (width / 2);
			y += (heigth / 2);
		} else {
			x += offsetX;
			y += offsetY;

			// Mouse cursor above first entry Mauszeiger über ersten Eintrag
			x -= 6;
			y -= 10;
		}

		if (top) {
			// Above
			y -= context.getHeight();

			// A bit down
			y += (heigth / 3);

			// Align left
			x -= (width / 2);
		}

		context.show(node, x, y);
	}

}
