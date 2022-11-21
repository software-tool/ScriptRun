package script.controller;

import script.data.Script;
import script.frame.PaneMain;
import script.recent.ScriptRecent;

public class ControllerScripts {

	public static void doRunSelected(boolean later) {
		PaneMain.inst.doRunSelected(later);
	}

	public static void doEditSelected() {
		PaneMain.inst.doEditSelected();
	}

	public static void openDetails(Script script, boolean inNewTab, boolean openCodeEditor) {
		PaneMain.inst.openDetails(script, inNewTab, openCodeEditor, null, true);
	}

	public static void openDetails(Script script, ScriptRecent scriptRecent) {
		PaneMain.inst.openDetails(script, false, false, scriptRecent, false);
	}

	public static Script getSelected() {
		return PaneMain.inst.getSelected();
	}
}
