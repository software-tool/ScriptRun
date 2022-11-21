package script.frame.context;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.Ln;
import com.rwu.fx.alerts.Alerts;
import com.rwu.fx.chooser.ChooseFromFilesystem;
import com.rwu.fx.dialog.DialogArrange;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.misc.StringUtils;

import javafx.scene.control.*;
import script.config.ConfigConstants;
import script.config.entities.DirectoryConfig;
import script.controller.ControllerPages;
import script.data.DirectoryGroup;
import script.data.ScriptDirectory;
import script.lang.K;
import script.manager.DirectoryConfigManager;
import script.state.SidebarState;

public class ContextMenuScriptGroup {

	public static ScriptDirectory scriptPathClicked = null;

	private ContextMenu contextMenu = new ContextMenu();

	private MenuItem itemSetOrder = new MenuItem();
	private MenuItem itemAdd = new MenuItem();
	private MenuItem itemRemoveTag = new MenuItem();

	private MenuItem itemShowOnlySelected = new MenuItem();

	private CheckMenuItem itemShowAll = new CheckMenuItem();

	private Menu menuSelectGroup = new Menu();

	private String tagClicked;

	public ContextMenuScriptGroup() {
		init();
		initActions();
	}

	private void init() {
		itemSetOrder.setText(Ln.dots(K.ScriptTagsSetOrder));

		itemAdd.setText(Ln.dots(K.ScriptDirectoryAdd));

		itemShowOnlySelected.setText(Ln.get(K.ScriptTagOnlySelected));
		itemShowAll.setText(Ln.get(K.ScriptTagsShowAll));

		itemRemoveTag.setText(Ln.dots(K.ScriptTagRemove));

		menuSelectGroup.setText(Ln.get(K.ScriptGroupShow));

		contextMenu.getItems().add(itemShowOnlySelected);
		contextMenu.getItems().add(itemShowAll);
		contextMenu.getItems().add(menuSelectGroup);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(itemSetOrder);
		contextMenu.getItems().add(itemRemoveTag);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(itemAdd);
	}

	private void initActions() {
		itemSetOrder.setOnAction(e -> {
			String tagHiddenStr = AppConfig.getValue(ConfigConstants.GROUPS, "tag_hidden");
			List<String> tagHidden = StringUtils.split(tagHiddenStr, ";", true, false);

			DialogArrange dialog = new DialogArrange(AppWindowFx.getStage(), Ln.get(K.ScriptTagsSetOrder), Ln.get(K.ScriptTagsSetOrderInfo), DirectoryConfigManager.getGroupTitlesSorted(),
					tagHidden, true);
			boolean ok = dialog.open();
			if (ok) {
				DirectoryConfigManager.setTagOrder(dialog.getValues());
				DirectoryConfigManager.setTagHidden(dialog.getHidden());

				SidebarState.showAllTags = false;
			}

			ControllerPages.updateScriptDirectories();
		});

		itemAdd.setOnAction(e -> {
			File directory = ChooseFromFilesystem.browseForDirectory(AppWindowFx.getStage(), Ln.get(K.ScriptDirectoryAdd), null);
			if (directory != null) {
				DirectoryConfig config = DirectoryConfigManager.addScriptPath(directory);
				if (tagClicked != null) {
					config.setTags(Arrays.asList(tagClicked));

					DirectoryConfigManager.store();

					ControllerPages.updateScriptDirectories();
				}
			}
		});

		itemShowAll.setOnAction(e -> {
			boolean selected = itemShowAll.isSelected();

			SidebarState.showAllTags = selected;

			ControllerPages.updateScriptDirectories();
		});

		itemShowOnlySelected.setOnAction(e -> {
			DirectoryConfigManager.hideOthers(tagClicked);
			SidebarState.showAllTags = false;

			ControllerPages.updateScriptDirectories();
		});

		itemRemoveTag.setOnAction(e -> {
			ButtonType type = Alerts.showConfirmYesNoCancel(Ln.get(K.ScriptTagRemoveConfirm), null, Ln.get(K.ScriptTagRemove));
			if (type != ButtonType.YES) {
				return;
			}

			DirectoryConfigManager.removeGroup(tagClicked);

			ControllerPages.updateScriptDirectories();
		});
	}

	public ContextMenu getContextMenu(String tagClicked) {
		this.tagClicked = tagClicked;

		itemShowAll.setSelected(SidebarState.showAllTags);

		menuSelectGroup.getItems().clear();
		List<DirectoryGroup> groups = DirectoryConfigManager.getGroups();
		for(DirectoryGroup group : groups) {
			MenuItem menuItemGroup = new MenuItem(group.getTag());
			menuItemGroup.setOnAction(e -> {
				DirectoryConfigManager.hideOthers(group.getTag());
				SidebarState.showAllTags = false;

				ControllerPages.updateScriptDirectories();
			});

			menuSelectGroup.getItems().add(menuItemGroup);
		}

		return contextMenu;
	}

}
