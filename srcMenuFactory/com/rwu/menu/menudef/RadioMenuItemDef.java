package com.rwu.menu.menudef;

import com.rwu.menu.menudef.base.MenuEntryDef;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

import javax.swing.*;

public class RadioMenuItemDef extends MenuEntryDef {

	// Impl
	private RadioMenuItem implFx = null;
	private JRadioButtonMenuItem implSwing = null;

	private ToggleGroup toggleGroup;

	public RadioMenuItemDef(String label) {
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

	public void setText(String text) {
		if (implFx != null) {
			implFx.setText(text);
		}
		if (implSwing != null) {
			implSwing.setText(text);
		}
	}

	public String getText() {
		if (implFx != null) {
			return implFx.getText();
		}
		if (implSwing != null) {
			return implSwing.getText();
		}

		return null;
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

	public void setImplFx(RadioMenuItem implFx) {
		this.implFx = implFx;
	}

	public void setImplSwing(JRadioButtonMenuItem implSwing) {
		this.implSwing = implSwing;
	}

	@Override
	public boolean isRadio() {
		return true;
	}

	public RadioMenuItem getImplFx() {
		return implFx;
	}

	public JRadioButtonMenuItem getImplSwing() {
		return implSwing;
	}

	public void setToggleGroup(ToggleGroup toggleGroup) {
		this.toggleGroup = toggleGroup;
	}

	public ToggleGroup getToggleGroup() {
		return toggleGroup;
	}

}
