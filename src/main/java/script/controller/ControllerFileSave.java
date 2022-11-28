package script.controller;

import script.frame.PaneMain;

import java.io.File;

public class ControllerFileSave {

	public static void doSaveSelected() {
		PaneMain.inst.doSaveSelected();
	}

	public static void onFileSaved(File file) {
		PaneMain.inst.updateScriptDirectories();
	}

}
