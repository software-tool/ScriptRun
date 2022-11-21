package script.frame.menu;

import java.util.List;

import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.Ln;
import com.rwu.fx.alerts.AlertsBase;
import com.rwu.fx.dialog.OneValueInputDialog;
import com.rwu.fx.util.FxKeyUtils;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.menu.menudef.CheckMenuItemDef;
import com.rwu.menu.menudef.MenuDef;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;
import com.rwu.misc.EqualsUtil;

import cp.system.AppCommon;
import script.ScriptingRun;
import script.config.ConfigConstants;
import script.controller.ControllerPages;
import script.data.DirectoryGroup;
import script.frame.PaneMain;
import script.frame.pages.PageTypeEnum;
import script.lang.K;
import script.manager.DirectoryConfigManager;
import script.state.SelectionState;

public class MenuApplication extends MenuBase {

	private MenuItemDef itemWelcome;

	private MenuItemDef itemClose;

	private MenuItemDef itemExit;

	//private MenuDef menuGroups;

	public MenuApplication() {
		super(Ln.get(K.MenuApplication));

		init();
		initItems();
	}

	private void init() {
		itemWelcome = new MenuItemDef(Ln.dots(K.MenuOpenWelcome));

		itemClose = new MenuItemDef(Ln.get(K.MenuClosePage));

		itemExit = new MenuItemDef(Ln.get(K.MENU_EXIT));

		//menuGroups = new MenuDef(Ln.get(K.MenuDirectoryGroups));

		itemWelcome.setAccelerator(FxKeyUtils.meta("home"));

		itemClose.setAccelerator(FxKeyUtils.meta("w"));
	}

	private void initItems() {
		itemWelcome.setOnAction(() -> {
			ControllerPages.openWelcome();
		});

		itemClose.setOnAction(() -> {
			ControllerPages.closeCurrentPage();
		});

		itemExit.setOnAction(() -> {
			ScriptingRun.closeApplication(false);
		});

		List<MenuEntryDef> items = menu.getItems();
		items.add(itemWelcome);
		menu.addSeparator();
		items.add(itemClose);

		// Groups
		//items.add(menuGroups);

		if (AppCommon.isNotMac) {
			menu.addSeparator();
			items.add(itemExit);
		}
	}

	@Override
	public void updateOnShowing() {
		//menuGroups.getItems().clear();

		// Existing groups
		//addGroups();

		// Add
		//addGroupNew();
		//addGroupEdit();

		//menuGroups.refill();

		if (PaneMain.inst != null) {
			PageTypeEnum type = PaneMain.inst.getDockNodeTypeSelected();

			boolean canClose = false;
			if (type != null) {
				canClose = true;
			}

			itemClose.setDisable(!canClose);
		}
	}

	/*private void addGroups() {
		String currentGroup = SelectionState.currentDirectoryGroup;

		List<DirectoryGroup> groups = DirectoryConfigManager.getGroups();
		for (DirectoryGroup group : groups) {
			String groupName = group.getTag();

			CheckMenuItemDef def = new CheckMenuItemDef(groupName);

			def.setOnAction(() -> {
				// Select group

				SelectionState.userSelectedGroup(groupName);

				PaneMain.inst.updateScriptDirectories();
			});

			// Selection
			def.setSelected(EqualsUtil.isEqual(currentGroup, groupName));

			menuGroups.getItems().add(def);
		}
	}*/

	/*private void addGroupNew() {
		menuGroups.addSeparator();

		MenuItemDef itemAdd = new MenuItemDef(Ln.dots(K.AddDirectoryGroup));
		itemAdd.setOnAction(() -> {
			String title = Ln.get(K.AddDirectoryGroup);
			String introduction = Ln.get(K.AddGroupIntroduction);
			String labelText = Ln.get(K.GroupName);

			OneValueInputDialog dialog = new OneValueInputDialog(AppWindowFx.getStage(), title, introduction, labelText, null);
			boolean confirmed = dialog.open();
			if (!confirmed) {
				return;
			}

			String value = dialog.getValue1();

			//DirectoryGroupManager.addDirectoryGroup(value);

			SelectionState.userSelectedGroup(value);

			PaneMain.inst.updateScriptDirectories();
		});
		menuGroups.getItems().add(itemAdd);
	}*/

	/*private void addGroupEdit() {
		String currentGroup = SelectionState.currentDirectoryGroup;

		MenuItemDef itemRemove = new MenuItemDef(Ln.dots(K.RemoveDirectoryGroup));
		itemRemove.setOnAction(() -> {
			String title = Ln.get(K.RemoveDirectoryGroup);
			String mainMessage = Ln.get(K.RemoveDirectoryGroupInfo);
			String secondaryMessage = null;

			Boolean confirmed = AlertsBase.showConfigDialogYesNo(AppWindowFx.getStage(), mainMessage, secondaryMessage, title);

			if (confirmed != null && confirmed) {
				// New Group
				//DirectoryGroupManager.removeDirectoryGroup(currentGroup);

				// Default
				SelectionState.userSelectedGroup(DirectoryConfigManager.DEFAULT_GROUP_NAME);

				PaneMain.inst.updateScriptDirectories();
			}
		});
		menuGroups.getItems().add(itemRemove);

		MenuItemDef itemRename = new MenuItemDef(Ln.dots(K.RenameDirectoryGroup));
		itemRename.setOnAction(() -> {
			String title = Ln.get(K.RenameDirectoryGroup);
			String introduction = Ln.get(K.RenameGroupIntroduction);
			String labelText = Ln.get(K.GroupName);

			OneValueInputDialog dialog = new OneValueInputDialog(AppWindowFx.getStage(), title, introduction, labelText, null);
			boolean confirmed = dialog.open();
			if (!confirmed) {
				return;
			}

			String newName = dialog.getValue1();

			DirectoryConfigManager.renameDirectoryGroup(currentGroup, newName);

			SelectionState.userSelectedGroup(newName);

			PaneMain.inst.updateScriptDirectories();
		});
		menuGroups.getItems().add(itemRename);
	}*/

	@Override
	public void updateOnHiding() {
	}
}
