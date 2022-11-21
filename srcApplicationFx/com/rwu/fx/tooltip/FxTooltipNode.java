package com.rwu.fx.tooltip;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class FxTooltipNode extends Pane {

	public Label label = new Label(null);

	public Pane parent = null;

	public FxTooltipNode() {
		label.getStyleClass().add("tooltip");

		getChildren().add(label);
	}

	public void setText(String text) {
		label.setText(text);
	}
}
