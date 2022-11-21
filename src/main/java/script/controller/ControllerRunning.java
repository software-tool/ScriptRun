package script.controller;

import script.frame.PaneMain;

public class ControllerRunning {

	public static void reportScriptFinished(int instId) {
		PaneMain.inst.reportScriptFinished(instId);
	}

}
