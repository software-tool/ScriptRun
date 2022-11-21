package script.frame.context;

import com.rwu.application.lang.Ln;
import com.rwu.fx.dialog.DialogEditStringList;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.misc.SystemUtils;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import script.controller.ControllerPages;
import script.data.ScriptDirectory;
import script.lang.K;
import script.manager.DirectoryConfigManager;

import java.util.List;

public class ContextMenuScriptDirectory {

	public static ScriptDirectory scriptPathClicked = null;

	private ContextMenu contextMenu = new ContextMenu();

	private MenuItem itemRemove = new MenuItem();

	private MenuItem itemEditTags = new MenuItem();

	private MenuItem itemOpenInSystem = new MenuItem();

	public ContextMenuScriptDirectory() {
		init();
		initActions();
	}

	private void init() {
		itemRemove.setText(Ln.get(K.ScriptDirectoryRemove));
		itemEditTags.setText(Ln.dots(K.ScriptDirectoryEditTags));
		itemOpenInSystem.setText(Ln.get(K.OpenInSystem));

		contextMenu.getItems().add(itemEditTags);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(itemOpenInSystem);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(itemRemove);
	}

	private void initActions() {
		itemRemove.setOnAction(e -> {
			DirectoryConfigManager.removeScriptPath(scriptPathClicked.getDirectory());

			scriptPathClicked = null;

			ControllerPages.updateScriptDirectories();
		});

		itemOpenInSystem.setOnAction(e -> {
			SystemUtils.openDirectory(scriptPathClicked.getDirectory());
		});

		itemEditTags.setOnAction(e -> {
			List<String> allTags = DirectoryConfigManager.getGroupTitlesSorted();
			List<String> tags = DirectoryConfigManager.getTags(scriptPathClicked.getDirectory());

			DialogEditStringList dialog = new DialogEditStringList(AppWindowFx.getStage(), Ln.get(K.ScriptDirectoryEditTags), Ln.get(K.GroupListIntro), allTags, tags, Ln.colon(K.GroupListNew), Ln.get(K.GroupListAddGroup));
			boolean ok = dialog.open();
			if (ok) {
				DirectoryConfigManager.setTags(scriptPathClicked.getDirectory(), dialog.getResult());

				ControllerPages.updateScriptDirectories();
			}
		});
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}

}
