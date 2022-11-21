package com.rwu.menu.menudef;

import com.rwu.menu.menudef.base.MenuEntryDef;

public class MenuSeparatorDef extends MenuEntryDef {

	@Override
	public boolean isSeparator() {
		return true;
	}

	@Override
	public void setDisable(boolean disabled) {
		// Do nothing for Separator
	}
}
