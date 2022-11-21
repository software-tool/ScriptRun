package com.rwu.fx.window;

import javafx.stage.Stage;

public class AppWindowFx {

	private static Stage stageMain;

	public static Stage getStage() {
		return stageMain;
	}

	public static void setPrimaryStage(Stage stage) {
		stageMain = stage;
	}
}
