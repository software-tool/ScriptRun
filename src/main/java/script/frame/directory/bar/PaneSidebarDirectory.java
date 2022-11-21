package script.frame.directory.bar;

import com.rwu.fx.util.FxContextMenuUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import script.controller.ControllerPath;
import script.data.ScriptDirectory;
import script.frame.context.ContextMenuScriptDirectory;
import script.frame.scripts.PaneScriptPathFile;
import script.manager.DirectoryConfigManager;
import script.manager.ScriptDirectoryReader;
import script.metadata.file.FileMetadata;
import script.metadata.file.FileMetadataManager;
import script.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Pane für Scriptpfad
 */
public class PaneSidebarDirectory {

	private VBox gridPaneDirectory = new VBox();

	private GridPane titlePane = new GridPane();

	private Label title = new Label();
	private Label labelMetainfo = new Label();

	private Button buttonCollapse = new Button();

	private GridPane contentGrid = new GridPane();

	private ScriptDirectory scriptDirectory = null;
	private ContextMenuScriptDirectory contextMenu;

	private int fileCount;
	private List<PaneScriptPathFile> filePanes = new ArrayList<>();

	private boolean collapsed;

	public PaneSidebarDirectory(ScriptDirectory scriptPath, ContextMenuScriptDirectory contextMenu, boolean collapsed) {
		this.scriptDirectory = scriptPath;
		this.contextMenu = contextMenu;
		this.collapsed = collapsed;

		initActions();

		fillFiles();

		initTitle();
	}

	private void initActions() {
		gridPaneDirectory.setOnMouseClicked(e -> {
			MouseButton button = e.getButton();
			if (!(button == MouseButton.PRIMARY)) {
				return;
			}

			ControllerPath.setCurrentDirectory(scriptDirectory, false);

			e.consume();
		});

		buttonCollapse.setOnAction(e -> {
			collapsed = !collapsed;

			fillFiles();
			initTitle();

			DirectoryConfigManager.setCollapsed(scriptDirectory.getDirectory(), collapsed);
		});

		gridPaneDirectory.setOnContextMenuRequested(e -> {
			ContextMenuScriptDirectory.scriptPathClicked = scriptDirectory;

			FxContextMenuUtils.showContextMenu(e, contextMenu.getContextMenu(), gridPaneDirectory);
		});
	}

	private void initTitle() {
		String text = scriptDirectory.getName();
		if (collapsed) {
			text += " (" + fileCount + ")";
		}

		title.setText(text);

		FxIconUtils.setIconNoStyleWithSize(buttonCollapse, "folder_vertical_open.png");
	}

	private void fillFiles() {
		contentGrid.getChildren().clear();
		filePanes.clear();

		fileCount = 0;

		File directory = scriptDirectory.getDirectory();
		fillDirectoryRecursive(directory);
	}

	private void fillDirectoryRecursive(File directory) {
		List<File> files = ScriptDirectoryReader.getScriptFilesOfDirectory(directory, null);

		fileCount += files.size();

		if (collapsed) {
			return;
		}

		int y = 0;
		for (File file : files) {
			FileMetadata metadata = FileMetadataManager.getCombined(file.getAbsolutePath());

			String relativePath = FileUtils.getRelativePath(file, directory);

			PaneScriptPathFile paneFile = new PaneScriptPathFile(contentGrid, file, relativePath, y, metadata);

			filePanes.add(paneFile);

			y++;
		}
	}

	public int filter(String text) {
		int visibleCount = 0;

		for (PaneScriptPathFile pane : filePanes) {
			File file = pane.getFile();
			String filename = file.getName().toLowerCase();

			boolean kept = false;

			if (text != null && filename.contains(text)) {
				kept = true;
			}
			if (text == null) {
				kept = true;
			}

			pane.setVisible(kept);

			if (kept) {
				visibleCount++;
			}
		}

		return visibleCount;
	}

	public int getFileCount() {
		return fileCount;
	}

	public Node getContent() {
		if (gridPaneDirectory.getChildren().isEmpty()) {
			gridPaneDirectory.getStyleClass().add("pane_directory_bar_item");

			Pane panePush = new Pane();
			FxLayoutUtils.setFillWidth(panePush);

			titlePane.add(title, 0, 0);
			titlePane.add(panePush, 1, 0);
			titlePane.add(buttonCollapse, 2, 0);

			titlePane.getStyleClass().add("directory_bar_item_title_wrapper");

			title.getStyleClass().add("directory_bar_item_title");
			contentGrid.getStyleClass().add("directory_bar_item_content");

			gridPaneDirectory.getChildren().add(titlePane);

			if (fileCount > 0) {
				gridPaneDirectory.getChildren().add(contentGrid);
			}
		}

		return gridPaneDirectory;
	}

	public ScriptDirectory getScriptDirectory() {
		return scriptDirectory;
	}

}
