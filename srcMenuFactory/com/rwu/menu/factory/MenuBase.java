package com.rwu.menu.factory;

import javax.swing.JMenu;

import com.rwu.menu.menudef.MenuDef;

import javafx.scene.control.Menu;

public class MenuBase {

	protected MenuDef menu;

	public Menu getMenuFx() {
		return MenuFactory.getMenuFx(menu);
	}

	public JMenu getMenuSwing() {
		return MenuFactory.getMenuSwing(menu);
	}
}
