package com.rwu.fx.util;

import com.rwu.misc.SystemUtils;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;

public class FxControlUtils {

	public static void setVisible(Control control, boolean visible) {
		control.setManaged(visible);
		control.setVisible(visible);
	}

	public static void setVisible(Pane control, boolean visible) {
		control.setManaged(visible);
		control.setVisible(visible);
	}

	public static void setOnclickHyperlink(Hyperlink control, Runnable runnable) {
		control.setOnAction(e -> {
			runnable.run();
		});
	}
}
