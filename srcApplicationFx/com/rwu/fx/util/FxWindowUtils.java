package com.rwu.fx.util;

import java.awt.Rectangle;
import java.util.List;

import com.rwu.misc.DoubleUtils;
import com.rwu.misc.ScreenUtils;

import javafx.stage.Stage;

public class FxWindowUtils {

	public static String getSizeStored(Stage stage) {
		double width = stage.getWidth();
		double height = stage.getHeight();

		return width + ";" + height;
	}

	/**
	 * Restore Width+Height
	 */
	public static void restoreSize(Stage stage, String sizeStr, boolean ensureFullyVisible, double defaultWidth, double defaultHeight) {
		if (sizeStr == null) {
			return;
		}

		double minWidth = 100;
		double minHeight = 100;

		double x = stage.getX();
		double y = stage.getY();

		if (x == Double.NaN) {
			x = 0;
		}
		if (y == Double.NaN) {
			y = 0;
		}

		String parts[] = sizeStr.split(";");

		Double width = null;
		Double height = null;

		if (parts.length > 0) {
			width = DoubleUtils.parse(parts[0], -1);
		}

		if (parts.length > 1) {
			height = DoubleUtils.parse(parts[1], -1);
		}

		if (width == null || width.doubleValue() == -1) {
			return;
		}
		if (height == null || height.doubleValue() == -1) {
			return;
		}

		if (width < minWidth) {
			width = minWidth;
		}
		if (height < minHeight) {
			height = minHeight;
		}

		if (ensureFullyVisible) {
			Rectangle rect = new Rectangle((int) x, (int) y, Double.valueOf(width).intValue(), Double.valueOf(height).intValue());
			List<Rectangle> screens = ScreenUtils.getScreensContainingWindow(rect);

			for (Rectangle screen : screens) {
				int screenWidth = screen.width;
				int screenHeight = screen.height;

				// Window can be somehow wider
				screenWidth += 30;

				if (width > screenWidth) {
					width = defaultWidth;
				}
				if (height > screenHeight) {
					height = defaultHeight;
				}
			}
		}

		stage.setWidth(width);
		stage.setHeight(height);
	}
}
