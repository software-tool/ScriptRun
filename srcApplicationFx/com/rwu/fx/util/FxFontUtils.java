package com.rwu.fx.util;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FxFontUtils {

	/**
	 * Set Font size
	 */
	public static void addFontSize(Node node, int size) {
		FxStyleUtils.addStyle(node, "-fx-font-size: " + size + ";");
	}

	public static double calculateWidth(String text, Font font, double added) {
		Text theText = new Text(text);
		theText.setFont(font);
		double width = theText.getBoundsInLocal().getWidth();

		return width + added;
	}

	public static Bounds calculateBounds(String text, Font font) {
		Text theText = new Text(text);
		theText.setFont(font);

		return theText.getBoundsInLocal();
	}
}
