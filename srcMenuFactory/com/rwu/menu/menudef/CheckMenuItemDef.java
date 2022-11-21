package com.rwu.menu.menudef;

import javax.swing.JCheckBoxMenuItem;

import com.rwu.menu.menudef.base.MenuEntryDef;

import javafx.scene.control.CheckMenuItem;

public class CheckMenuItemDef extends MenuEntryDef {

	// Impl
	private CheckMenuItem implFx = null;
	private JCheckBoxMenuItem implSwing = null;

	public CheckMenuItemDef(String label) {
		this.label = label;
	}

	@Override
	public void setDisable(boolean disabled) {
		this.disabled = disabled;

		if (implFx != null) {
			implFx.setDisable(disabled);
		}
		if (implSwing != null) {
			implSwing.setEnabled(!disabled);
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;

		if (implFx != null) {
			implFx.setSelected(selected);
		}
		if (implSwing != null) {
			implSwing.setSelected(selected);
		}
	}

	public boolean getSelected() {
		if (implFx != null) {
			return implFx.isSelected();
		}
		if (implSwing != null) {
			return implSwing.isSelected();
		}

		return this.selected;
	}

	public void setImplFx(CheckMenuItem implFx) {
		this.implFx = implFx;
	}

	public void setImplSwing(JCheckBoxMenuItem implSwing) {
		this.implSwing = implSwing;
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}

}
