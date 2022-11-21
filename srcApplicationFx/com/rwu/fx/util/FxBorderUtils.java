package com.rwu.fx.util;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;

public class FxBorderUtils {

	public static void setBorderDisabled(ScrollPane scrollPane) {
		scrollPane.setPadding(new Insets(0));

		// Border white, when clicked in area
		FxStyleUtils.addStyle(scrollPane, "-fx-background-color: #ffffff;");
	}
}
