package script.frame.scripts;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxContextMenuUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.misc.StringUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import script.controller.ControllerScripts;
import script.metadata.file.FileMetadata;
import script.metadata.file.FileMetadataManager;
import script.data.Script;
import script.frame.directory.PaneDirectory;
import script.lang.K;

/**
 * Pane for Script, for Listing
 */
public class PaneScript {

	private PaneDirectory parent;

	private GridPane gridPaneScriptInDirectory = new GridPane();

	private VBox boxContent = new VBox();

	private VBox boxInfo = new VBox();
	private VBox boxMetainfo = new VBox();

	private Label labelTitle = new Label();
	private Label labelMetainfo = new Label();

	private Label labelSubtitle = new Label();

	private Script script;

	private Button buttonRun = new Button();
	private Button buttonEdit = new Button();

	private ContextMenu contextMenuContent = new ContextMenu();

	public PaneScript(PaneDirectory parent, Script script) {
		this.parent = parent;
		this.script = script;

		init();
		initContextMenues();
		initContent();
		initActions();
	}

	private void init() {
		boxInfo.getStyleClass().add("pane_script_info");
		buttonRun.getStyleClass().add("pane_script_run");
		buttonEdit.getStyleClass().add("pane_script_edit");

		FxIconUtils.setIconNoStyleWithSize(buttonRun, "control_play_blue.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonEdit, "edit_button.png", FxIconUtils.ICON_SIZE_MEDIUM);

		FxTooltip.apply(buttonRun, Ln.get(K.RunNow));

		boxInfo.setMinWidth(250);
	}

	private void initContextMenues() {
		MenuItem itemOpenInTab = new MenuItem(Ln.get(K.OpenInNewTab));

		itemOpenInTab.setOnAction(e -> {
			ControllerScripts.openDetails(script, true, false);
		});

		contextMenuContent.getItems().add(itemOpenInTab);
	}

	private void initContent() {
		boxInfo.setSpacing(6);

		boxContent.getChildren().add(labelTitle);
		boxContent.getChildren().add(labelSubtitle);

		// Info

		boxInfo.getChildren().add(boxContent);
		boxInfo.getChildren().add(boxMetainfo);
		boxMetainfo.getChildren().add(labelMetainfo);

		labelMetainfo.setText(script.getLinesCount() + " " + Ln.get(K.LinesCount));

		updateTitle();
	}

	private void updateTitle() {
		String filename = script.getName();

		String title = filename;
		String subtitle = null;

		String path = script.getFile().getAbsolutePath();
		FileMetadata fileCustom = FileMetadataManager.getCombined(path);

		if (fileCustom != null) {
			String customTitle = fileCustom.getTitle();

			if (StringUtils.isValue(customTitle)) {
				title = customTitle;
				subtitle = filename;
			}
		}

		labelTitle.setText(title);
		labelSubtitle.setText(subtitle);
	}

	private void initActions() {
		buttonRun.setOnAction(e -> {
			parent.runScript(script.getFile());
		});

		boxInfo.setOnMouseClicked(e -> {
			MouseButton button = e.getButton();

			if (button == MouseButton.PRIMARY) {
				ControllerScripts.openDetails(script, false, false);
			}
		});
		boxInfo.setOnContextMenuRequested(e -> {
			FxContextMenuUtils.showContextMenu(e, contextMenuContent, boxInfo);
		});

		buttonEdit.setOnAction(e -> {
			ControllerScripts.openDetails(script, false, true);
		});
	}

	public Node getContent() {
		gridPaneScriptInDirectory.getStyleClass().add("pane_script_in_directory");

		gridPaneScriptInDirectory.add(boxInfo, 0, 0);
		gridPaneScriptInDirectory.add(buttonRun, 1, 0);
		gridPaneScriptInDirectory.add(buttonEdit, 2, 0);

		GridPane.setMargin(boxInfo, new Insets(0, 9, 0, 0));
		GridPane.setMargin(buttonRun, new Insets(0, 9, 0, 0));

		return gridPaneScriptInDirectory;
	}
}
