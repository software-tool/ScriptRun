package com.rwu.menu.factory;

import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import com.rwu.menu.menudef.CheckMenuItemDef;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.RadioMenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;

public class MenuItemFactory {

	// Needs to be initialized on program startup
	public static boolean isMac = false;

	/**
	 * JavaFX: Action Item
	 */
	public static MenuItem createItem(MenuItemDef menuItemDef) {
		MenuItem item = new MenuItem(menuItemDef.getLabel());

		item.setDisable(menuItemDef.getDisabled());
		item.setGraphic(menuItemDef.getGraphic());
		item.setAccelerator(menuItemDef.getCombination());

		item.setOnAction((ActionEvent e) -> {
			menuItemDef.getOnAction().run();
		});

		item.setMnemonicParsing(menuItemDef.getMnemonicParsing());

		menuItemDef.setImplFx(item);

		return item;
	}

	/**
	 * JavaFX: Checkbox Item
	 */
	public static CheckMenuItem createItem(CheckMenuItemDef menuItemDef) {
		CheckMenuItem item = new CheckMenuItem(menuItemDef.getLabel());
		item.setDisable(menuItemDef.getDisabled());
		item.setSelected(menuItemDef.getSelected());
		item.setAccelerator(menuItemDef.getCombination());

		item.setOnAction((ActionEvent e) -> {
			menuItemDef.getOnAction().run();
		});

		menuItemDef.setImplFx(item);

		return item;
	}

	/**
	 * JavaFX: Radio Item
	 */
	public static RadioMenuItem createItem(RadioMenuItemDef menuItemDef) {
		RadioMenuItem item = new RadioMenuItem(menuItemDef.getLabel());
		item.setDisable(menuItemDef.getDisabled());
		item.setSelected(menuItemDef.getSelected());
		item.setToggleGroup(menuItemDef.getToggleGroup());
		item.setAccelerator(menuItemDef.getCombination());

		item.setOnAction((ActionEvent e) -> {
			menuItemDef.getOnAction().run();
		});

		menuItemDef.setImplFx(item);

		return item;
	}

	// Swing

	/**
	 * Swing: Action Item
	 */
	public static JMenuItem createItemSwing(MenuItemDef menuItemDef) {
		String label = getSwingLabelPrepared(menuItemDef.getLabel(), menuItemDef.getMnemonicParsing());

		JMenuItem item = new JMenuItem(label);
		item.setEnabled(!menuItemDef.getDisabled());

		// Keys
		swingApplyKey(menuItemDef, item);

		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(() -> {
					menuItemDef.getOnAction().run();
				});
			}
		});

		menuItemDef.setImplSwing(item);

		return item;
	}

	/**
	 * Swing: Checkbox Item
	 */
	public static JCheckBoxMenuItem createItemSwing(CheckMenuItemDef menuItemDef) {
		String label = getSwingLabelPrepared(menuItemDef.getLabel(), true);

		JCheckBoxMenuItem item = new JCheckBoxMenuItem(label);
		item.setEnabled(!menuItemDef.getDisabled());
		item.setSelected(menuItemDef.getSelected());

		// Keys
		swingApplyKey(menuItemDef, item);

		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(() -> {
					menuItemDef.getOnAction().run();
				});
			}
		});

		menuItemDef.setImplSwing(item);

		return item;
	}

	/**
	 * Swing: Radio Item
	 */
	public static JRadioButtonMenuItem createItemSwing(RadioMenuItemDef menuItemDef) {
		String label = getSwingLabelPrepared(menuItemDef.getLabel(), true);

		JRadioButtonMenuItem item = new JRadioButtonMenuItem(label);
		item.setEnabled(!menuItemDef.getDisabled());
		item.setSelected(menuItemDef.getSelected());

		// Keys
		swingApplyKey(menuItemDef, item);

		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(() -> {
					menuItemDef.getOnAction().run();
				});
			}
		});

		menuItemDef.setImplSwing(item);

		return item;
	}

	private static void swingApplyKey(MenuEntryDef menuItemDef, JMenuItem item) {
		if (menuItemDef.getKeyStroke() != null) {
			item.setAccelerator(menuItemDef.getKeyStroke());
		} else if (menuItemDef.getCombination() != null) {
			String name = menuItemDef.getCombination().getName();

			name = name.replace("+", " ");
			name = name.replace("Shift", "shift");
			name = name.replace("Ctrl", "ctrl");
			name = name.replace("Meta", "meta");

			KeyStroke keyStroke = KeyStroke.getKeyStroke(name);
			item.setAccelerator(keyStroke);
		}

	}

	private static String getSwingLabelPrepared(String label, boolean activated) {
		if (isMac && label != null && activated) {
			label = label.replace("_", "");
		}

		return label;
	}
}
