package com.rwu.menu.menudef;

import javax.swing.JMenuItem;

import com.rwu.menu.menudef.base.MenuEntryDef;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class MenuItemDef extends MenuEntryDef {

	private ImageView graphic = null;

	// Impl
	private MenuItem implFx = null;
	private JMenuItem implSwing = null;

	private boolean mnemonicParsing = true;

	public MenuItemDef(String label) {
		this.label = label;
	}

	public MenuItemDef(String label, boolean mnemonicParsing) {
		this.label = label;
		this.mnemonicParsing = mnemonicParsing;
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

	public void setGraphic(ImageView graphic) {
		// if (Environment.isMacDesktop()) {
		// No icons in mac menu
		// return;
		// }

		this.graphic = graphic;
	}

	public ImageView getGraphic() {
		return graphic;
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

	public void setVisible(boolean visible) {
		if (implFx != null) {
			implFx.setVisible(visible);
		}
		if (implSwing != null) {
			implSwing.setVisible(visible);
		}
	}

	public void setImplFx(MenuItem implFx) {
		this.implFx = implFx;
	}

	public void setImplSwing(JMenuItem implSwing) {
		this.implSwing = implSwing;
	}

	public boolean getMnemonicParsing() {
		return mnemonicParsing;
	}

	@Override
	public boolean isActionItem() {
		return true;
	}

}
