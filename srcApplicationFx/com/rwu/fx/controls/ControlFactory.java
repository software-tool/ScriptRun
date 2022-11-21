package com.rwu.fx.controls;

import com.rwu.fx.tooltip.FxTooltip;

import javafx.scene.control.Label;

public class ControlFactory {
	public static Label createLabel(String text) {
		Label label = createLabel(text, null);
		return label;
	}

	public static Label createLabel(String text, String tooltip) {
		Label label = new Label(text);

		if (tooltip != null) {
			FxTooltip.apply(label, tooltip);
		}

		return label;
	}
}
