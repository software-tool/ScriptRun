package com.rwu.menu.factory;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.rwu.menu.menudef.CheckMenuItemDef;
import com.rwu.menu.menudef.MenuDef;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.RadioMenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuFactory {

	public static boolean USE_SWING_MENU = false;

	/**
	 * Menu for JavaFX
	 */
	public static Menu getMenuFx(MenuDef menuDef) {
		Menu menu = new Menu(menuDef.getLabel());
		menu.setMnemonicParsing(true);
		menu.setGraphic(menuDef.getGraphic());

		if (menuDef.getOnShowing() != null) {
			menu.setOnShowing((action) -> {
				menuDef.getOnShowing().run();
			});
		}

		if (menuDef.getOnShown() != null) {
			menu.setOnShown((action) -> {
				menuDef.getOnShown().run();
			});
		}

		if (menuDef.getOnHiding() != null) {
			menu.setOnHiding((action) -> {
				menuDef.getOnHiding().run();
			});
		}

		if (menuDef.getOnHidden() != null) {
			menu.setOnHidden((action) -> {
				menuDef.getOnHidden().run();
			});
		}

		// Fill
		fillMenu(menuDef, menu);

		menuDef.setImplFx(menu);

		return menu;
	}

	/**
	 * Menu for Swing
	 */
	public static JMenu getMenuSwing(MenuDef menuDef) {
		String label = menuDef.getLabel().replace("_", "");

		JMenu menu = new JMenu(label);
		menu.setIcon(menuDef.getIcon());

		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (menuDef.getOnShowing() == null) {
					return;
				}

				Platform.runLater(() -> {
					menuDef.getOnShowing().run();
				});
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				if (menuDef.getOnHiding() != null) {
					Platform.runLater(() -> {
						menuDef.getOnHiding().run();
					});
				}

				if (menuDef.getOnHidden() != null) {
					Platform.runLater(() -> {
						menuDef.getOnHidden().run();
					});
				}
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		if (menuDef.getOnShown() != null) {
			menu.addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {
					menuDef.getOnShown().run();
				}

				@Override
				public void componentResized(ComponentEvent e) {
				}

				@Override
				public void componentMoved(ComponentEvent e) {
				}

				@Override
				public void componentHidden(ComponentEvent e) {
				}
			});

		}

		fillMenu(menuDef, menu);

		menuDef.setImplSwing(menu);

		return menu;
	}

	public static void updateMenu(MenuDef menuDef, Menu menu) {
		menu.getItems().clear();

		// Fill
		fillMenu(menuDef, menu);
	}

	public static void updateMenu(MenuDef menuDef, JMenu menu) {
		// Clear
		menu.removeAll();

		// Re-fill
		fillMenu(menuDef, menu);
	}

	/**
	 * JavaFX-Men� bef�llen
	 */
	private static void fillMenu(MenuDef menuDef, Menu menu) {
		List<MenuEntryDef> items = menuDef.getItems();
		for (MenuEntryDef item : items) {
			MenuItem created = null;

			if (item.isCheckbox()) {
				// Checkbox

				CheckMenuItemDef itemDef = (CheckMenuItemDef) item;

				// Create
				created = MenuItemFactory.createItem(itemDef);
			}

			if (item.isRadio()) {
				// Radio-Button

				RadioMenuItemDef itemDef = (RadioMenuItemDef) item;

				// Create
				created = MenuItemFactory.createItem(itemDef);
			}

			if (item.isSeparator()) {
				// Separator

				created = new SeparatorMenuItem();
			}

			if (item.isMenu()) {
				// Menu

				MenuDef itemDef = (MenuDef) item;

				// Create
				created = getMenuFx(itemDef);
			}

			if (item.isActionItem()) {
				// Normal menu item

				MenuItemDef itemDef = (MenuItemDef) item;

				// Create
				created = MenuItemFactory.createItem(itemDef);
			}

			menu.getItems().add(created);
		}
	}

	/**
	 * Fill swing menu
	 */
	private static void fillMenu(MenuDef menuDef, JMenu menu) {
		//		Map<ToggleGroup, List<RadioMenuItemDef>> groups = new HashMap<>();
		//
		//		
		//
		//		for (MenuEntryDef item : items) {
		//			if (item.isRadio()) {
		//				// Radio-Button
		//
		//				RadioMenuItemDef itemDef = (RadioMenuItemDef) item;
		//				ToggleGroup toggleGroup = itemDef.getToggleGroup();
		//
		//				List<RadioMenuItemDef> group = groups.get(toggleGroup);
		//				if (group == null) {
		//					group = new ArrayList<>();
		//					groups.put(toggleGroup, group);
		//				}
		//
		//				group.add(itemDef);
		//				
		//				ButtonGroup buttonGroup = buttonGroups.get(toggleGroup);
		//				if (buttonGroup == null) {
		//					buttonGroup = new ButtonGroup();
		//					buttonGroups.put(toggleGroup, buttonGroup);
		//				}
		//				
		//				
		//			}
		//		}

		Map<ToggleGroup, ButtonGroup> buttonGroups = new HashMap<>();

		// Create
		List<MenuEntryDef> items = menuDef.getItems();
		for (MenuEntryDef item : items) {
			JMenuItem created = null;

			if (item.isCheckbox()) {
				// Checkbox

				CheckMenuItemDef itemDef = (CheckMenuItemDef) item;

				// Create
				created = MenuItemFactory.createItemSwing(itemDef);
			}

			if (item.isRadio()) {
				// Radio-Button

				RadioMenuItemDef itemDef = (RadioMenuItemDef) item;

				// Create
				created = MenuItemFactory.createItemSwing(itemDef);

				// Toggle group
				ToggleGroup toggleGroup = itemDef.getToggleGroup();
				ButtonGroup buttonGroup = buttonGroups.get(toggleGroup);
				if (buttonGroup == null) {
					buttonGroup = new ButtonGroup();
					buttonGroups.put(toggleGroup, buttonGroup);
				}

				buttonGroup.add(created);
			}

			if (item.isSeparator()) {
				// Separator

				menu.addSeparator();
				continue;
			}

			if (item.isMenu()) {
				// Menu

				MenuDef itemDef = (MenuDef) item;

				// Create
				created = getMenuSwing(itemDef);
				itemDef.setImplSwing((JMenu) created);
			}

			if (item.isActionItem()) {
				// Normal menu item

				MenuItemDef itemDef = (MenuItemDef) item;

				// Create
				created = MenuItemFactory.createItemSwing(itemDef);
			}

			menu.add(created);
		}
	}

	public static void setEnabled(boolean enabled, MenuEntryDef... entries) {
		for (MenuEntryDef entry : entries) {
			entry.setDisable(!enabled);
		}
	}

	public static void setToggleGroup(RadioMenuItemDef... entries) {
		setToggleGroup(Arrays.asList(entries));
	}

	/**
	 * Toggle Group for Radios
	 */
	public static void setToggleGroup(List<RadioMenuItemDef> entries) {
		if (USE_SWING_MENU) {
			ButtonGroup group = new ButtonGroup();

			for (MenuEntryDef entry : entries) {
				RadioMenuItemDef radioDef = (RadioMenuItemDef) entry;
				group.add(radioDef.getImplSwing());
			}
		} else {
			ToggleGroup group = new ToggleGroup();

			for (MenuEntryDef entry : entries) {
				RadioMenuItemDef radioDef = (RadioMenuItemDef) entry;

				radioDef.getImplFx().setToggleGroup(group);
			}
		}
	}

}
