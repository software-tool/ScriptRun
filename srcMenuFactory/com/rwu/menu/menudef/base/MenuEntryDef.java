package com.rwu.menu.menudef.base;

import javax.swing.KeyStroke;

import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public abstract class MenuEntryDef {

	protected String label;

	protected Runnable onAction;

	private KeyCombination combination;

	// Swing
	private KeyStroke keyStroke;

	protected boolean disabled = false;
	protected boolean selected = false;

	public abstract void setDisable(boolean disabled);

	public void setAccelerator(KeyCombination combination) {
		this.combination = combination;
	}

	public void setAccelerator(KeyCodeCombination combination) {
		this.combination = combination;
	}

	public void setKeyStroke(KeyStroke keyStroke) {
		this.keyStroke = keyStroke;
	}

	public KeyStroke getKeyStroke() {
		return keyStroke;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public String getLabel() {
		return label;
	}

	public void setOnAction(Runnable onAction) {
		this.onAction = onAction;
	}

	public Runnable getOnAction() {
		return onAction;
	}

	public KeyCombination getCombination() {
		return combination;
	}

	public boolean isCheckbox() {
		return false;
	}

	public boolean isRadio() {
		return false;
	}

	public boolean isSeparator() {
		return false;
	}

	public boolean isMenu() {
		return false;
	}

	public boolean isActionItem() {
		return false;
	}
}
