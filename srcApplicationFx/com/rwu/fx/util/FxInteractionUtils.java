package com.rwu.fx.util;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class FxInteractionUtils {
	public static void setToggleGroup(RadioButton... controls) {
		ToggleGroup group = new ToggleGroup();

		for (RadioButton control : controls) {
			control.setToggleGroup(group);
		}
	}

	public static ToggleGroup setToggleGroupForList(List<RadioButton> controls) {
		ToggleGroup group = new ToggleGroup();

		for (RadioButton control : controls) {
			control.setToggleGroup(group);
		}

		return group;
	}
}
