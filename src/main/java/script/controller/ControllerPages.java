package script.controller;

import script.frame.PaneMain;

public class ControllerPages {

	public static void closeCurrentPage() {
		PaneMain.inst.closeCurrentPage();
	}

	public static void openWelcome() {
		PaneMain.inst.openWelcome();
	}

	public static void openOptions() {
		PaneMain.inst.openOptions();
	}

	public static void openRecents() {
		PaneMain.inst.openRecents();
	}

	public static void updateScriptDirectories() {
		PaneMain.inst.updateScriptDirectories();
	}

	public static void updateExecutions() {
		PaneMain.inst.updateExecutions();
	}

	public static void updateEditings() {
		PaneMain.inst.updateEditings();
	}
}
