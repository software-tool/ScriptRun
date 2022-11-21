package script.frame.menu;

import javax.swing.JMenuBar;

import cp.system.AppCommon;
import javafx.scene.control.MenuBar;
import script.Scripting;

public class ScriptingMenubar {

	public static ScriptingMenubar inst;

	private MenuBar menuBarFx = new MenuBar();
	private JMenuBar menuBarSwing = new JMenuBar();

	private MenuApplication menuApplication;
	private MenuScript menuScript;
	private MenuExtras menuExtras;
	private MenuRecent menuRecent;
	private MenuHelp menuHelp;

	public ScriptingMenubar() {
		inst = this;

		if (AppCommon.isMac) {
			menuBarFx.setUseSystemMenuBar(true);
		}

		addMenus();

		updateMenues();

		// Shoutcast.addContentListener(this);
	}

	public MenuBar getMenuBar() {
		return menuBarFx;
	}

	public JMenuBar getMenuBarSwing() {
		return menuBarSwing;
	}

	public void updateMenues() {
		menuApplication.updateOnShowing();
		menuRecent.updateOnShowing();

		// For Shortcuts
		menuScript.update();
	}

	private void addMenus() {
		menuApplication = new MenuApplication();
		menuScript = new MenuScript();
		menuExtras = new MenuExtras();
		menuRecent = new MenuRecent();
		menuHelp = new MenuHelp();

		if (Scripting.useSwingMenu()) {
			menuBarSwing.add(menuApplication.getMenuSwing());
			menuBarSwing.add(menuScript.getMenuSwing());
			menuBarSwing.add(menuRecent.getMenuSwing());
			menuBarSwing.add(menuExtras.getMenuSwing());
			menuBarSwing.add(menuHelp.getMenuSwing());
		} else {
			menuBarFx.getMenus().add(menuApplication.getMenuFx());
			menuBarFx.getMenus().add(menuScript.getMenuFx());
			menuBarFx.getMenus().add(menuRecent.getMenuFx());
			menuBarFx.getMenus().add(menuExtras.getMenuFx());
			menuBarFx.getMenus().add(menuHelp.getMenuFx());
		}
	}

}
