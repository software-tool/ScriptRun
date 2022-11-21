
package com.rwu.misc;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class ScreenUtils {

	public static void ensureIsVisible(Window window, Rectangle bounds) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		int intersections = 0;
		for (GraphicsDevice device : devices) {
			GraphicsConfiguration[] configs = device.getConfigurations();

			for (GraphicsConfiguration config : configs) {
				Rectangle screenBounds = config.getBounds();

				Rectangle intersection = intersection(screenBounds, bounds);

				if (intersection != null) {
					intersections++;
				}
			}
		}

		if (intersections == 0) {
			// Not visible
			ScreenUtils.centerWindowScreen(window);
		}
	}

	public static List<Rectangle> getScreensContainingWindow(Rectangle bounds) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		List<Rectangle> result = new ArrayList<>();

		for (GraphicsDevice device : devices) {
			GraphicsConfiguration[] configs = device.getConfigurations();

			for (GraphicsConfiguration config : configs) {
				Rectangle screenBounds = config.getBounds();
				Rectangle intersection = intersection(screenBounds, bounds);

				if (intersection != null) {
					result.add(screenBounds);
				}
			}
		}

		return result;
	}

	public static void centerWindowScreen(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = window.getPreferredSize();

		int x = screenSize.width / 2 - (size.width / 2);
		int y = screenSize.height / 2 - (size.height / 2) - 70;

		if (x > 0 && y > 0) {
			window.setLocation(x, y);
		} else {
			window.setLocation(30, 30);
		}
	}

	public static void alignWindowRight(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = window.getPreferredSize();

		int x = screenSize.width - size.width - 20;
		int y = 15;

		if (x > 0 && y > 0) {
			window.setLocation(x, y);
		} else {
			window.setLocation(30, 30);
		}
	}

	/**
	 * Get intersection of both rectangles
	 */
	private static Rectangle intersection(Rectangle screen, Rectangle area) {
		Rectangle target = new Rectangle();

		Rectangle.intersect(screen, area, target);

		if (target.getWidth() < 0 || target.getHeight() < 0) {
			return null;
		}

		return target;
	}
}
