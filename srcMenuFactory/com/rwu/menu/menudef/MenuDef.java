package com.rwu.menu.menudef;

import com.rwu.menu.factory.MenuFactory;
import com.rwu.menu.menudef.base.MenuEntryDef;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDef extends MenuEntryDef {

	private List<MenuEntryDef> items = new ArrayList<MenuEntryDef>();

	private ImageView graphic = null;
	private Icon icon = null;

	private Runnable onShowing;
	private Runnable onShown;
	private Runnable onHiding;
	private Runnable onHidden;

	// Impl
	private Menu implFx;
	private JMenu implSwing;

	public MenuDef(String label) {
		this.label = label;
	}

	public void setOnShowing(Runnable runnable) {
		this.onShowing = runnable;
	}

	/**
	 * Caution: Entries may not be changable in swing (Selection etc.)
	 * 
	 * -> use setOnShowing
	 */
	public void setOnShown(Runnable runnable) {
		this.onShown = runnable;
	}

	public void setOnHiding(Runnable onHiding) {
		this.onHiding = onHiding;
	}

	public void setOnHidden(Runnable onHidden) {
		this.onHidden = onHidden;
	}

	public Runnable getOnShowing() {
		return onShowing;
	}

	public Runnable getOnShown() {
		return onShown;
	}

	public Runnable getOnHiding() {
		return onHiding;
	}

	public Runnable getOnHidden() {
		return onHidden;
	}

	public List<MenuEntryDef> getItems() {
		return items;
	}

	public void addSeparator() {
		items.add(new MenuSeparatorDef());
	}

	public void setGraphic(ImageView graphic, Icon icon) {
		this.graphic = graphic;
		this.icon = icon;
	}

	public ImageView getGraphic() {
		return graphic;
	}

	public Icon getIcon() {
		return icon;
	}

	@Override
	public void setDisable(boolean disabled) {
		// Do nothing for menu
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	public void setImplFx(Menu implFx) {
		this.implFx = implFx;
	}

	public void setImplSwing(JMenu implSwing) {
		this.implSwing = implSwing;
	}

	public Menu getImplFx() {
		return implFx;
	}

	public JMenu getImplSwing() {
		return implSwing;
	}

	public void refill() {
		if (implFx != null) {
			MenuFactory.updateMenu(this, implFx);
		}

		if (implSwing != null) {
			MenuFactory.updateMenu(this, implSwing);
		}
	}

	public boolean isVisible() {
		if (implFx != null) {
			return implFx.isVisible();
		}

		if (implSwing != null) {
			return implSwing.isVisible();
		}

		return false;
	}
}
