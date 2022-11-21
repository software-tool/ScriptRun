package com.rwu.fx.dialog.base;

import com.rwu.util.FxRect;

import javafx.stage.Stage;

public class FxDialogBase {

	private static final double DIALOG_MIN_WIDTH = 120;
	private static final double DIALOG_MIN_HEIGHT = 100;

	public static void setMinWidth(Stage dialog, double width) {
		if (width != -1) {
			dialog.setMinWidth(width);
		}
	}

	public static void setMinHeight(Stage dialog, double height) {
		if (height != -1) {
			dialog.setMinHeight(height);
		}
	}

	public static void setWidth(Stage dialog, double width) {
		if (width != -1) {
			dialog.setWidth(width);
		}
	}

	public static void setHeight(Stage dialog, double height) {
		if (height != -1) {
			dialog.setHeight(height);
		}
	}

	public static FxRect findSize(Stage templateStage, int percentWidth, int percentHeight, double defaultWidth,
			double defaultHeight) {
		double width = templateStage.getWidth();
		double height = templateStage.getHeight();

		if (width == 0d) {
			width = defaultWidth;
		} else {
			// Calculate

			width = (width * (percentWidth / 100.d));
		}

		if (height == 0d) {
			height = defaultHeight;
		} else {
			// Calculate

			height = (height * (percentHeight / 100.d));
		}

		return new FxRect(width, height);
	}

	public static void overwriteSizeIfValid(FxRect rectangle, double widthPrio, double heightPrio) {
		if (widthPrio > DIALOG_MIN_WIDTH) {
			rectangle.width = widthPrio;
		}
		if (heightPrio > DIALOG_MIN_HEIGHT) {
			rectangle.height = heightPrio;
		}
	}
}
