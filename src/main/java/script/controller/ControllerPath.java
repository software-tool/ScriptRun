package script.controller;

import script.data.ScriptDirectory;
import script.frame.PaneMain;

public class ControllerPath {

	public static void setCurrentDirectory(ScriptDirectory path, boolean addScriptOnOpen) {
		PaneMain.inst.setCurrentDirectory(path, addScriptOnOpen);
	}
	
}
