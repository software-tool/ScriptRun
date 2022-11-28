package script.frame.details.contextmenu;

import com.rwu.application.lang.Ln;
import com.rwu.fx.dialog.OneValueInputDialog;
import com.rwu.fx.window.AppWindowFx;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import script.metadata.file.FileMetadata;
import script.metadata.file.FileMetadataManager;
import script.frame.details.PaneScriptDetails;
import script.lang.K;

public class ContextMenuDetailsTitle {

	private PaneScriptDetails parent;

	private ContextMenu contextMenu = new ContextMenu();

	public ContextMenuDetailsTitle(PaneScriptDetails parent) {
		this.parent = parent;

		init();
	}

	private void init() {
		MenuItem itemChangeTitle = new MenuItem(Ln.get(K.ChangeTitle));
		itemChangeTitle.setOnAction(e -> {
			String preset = null;

			String path = parent.getScriptFullPath();
			FileMetadata fileCustom = FileMetadataManager.getCombined(path);
			if (fileCustom != null) {
				preset = fileCustom.getTitle();
			}

			OneValueInputDialog dialog = new OneValueInputDialog(AppWindowFx.getStage(), Ln.get(K.InputTitle), null, Ln.get(K.ChangeTitleField), preset);
			boolean ok = dialog.open();
			if (!ok) {
				return;
			}

			String value = dialog.getValue1();

			parent.setNewTitle(value);
		});

		contextMenu.getItems().add(itemChangeTitle);
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}

}
