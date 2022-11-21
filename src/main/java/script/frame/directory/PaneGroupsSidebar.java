package script.frame.directory;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.*;
import com.rwu.misc.EqualsUtil;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import script.config.entities.DirectoryConfig;
import script.controller.ControllerPages;
import script.data.DirectoryGroup;
import script.data.ScriptDirectory;
import script.frame.PaneMain;
import script.frame.context.ContextMenuScriptDirectory;
import script.frame.context.ContextMenuScriptGroup;
import script.frame.details.PaneScriptDetails;
import script.frame.directory.bar.PaneSidebarDirectory;
import script.frame.execute.IExecutePane;
import script.frame.intf.IContent;
import script.frame.state.CodeUnsavedPanes;
import script.frame.state.ExecutionPanes;
import script.lang.K;
import script.manager.DirectoryConfigManager;
import script.state.SelectionState;
import script.state.SidebarState;

import java.util.ArrayList;
import java.util.List;

/**
 * List of directories
 */
public class PaneGroupsSidebar implements IContent {

	private static double INDICATOR_IMAGE_SIZE = 20;
	private static int GAP_WIDTH = 5;

	private BorderPane paneMain = new BorderPane();

	private VBox vboxSidebar = new VBox();

	private VBox vboxEditingStatus = new VBox();
	private VBox vboxExecutionStatus = new VBox();

	private Label filterDirectoriesInfo = new Label();
	private Label filterFilesInfo = new Label();
	private TextField fieldFilter = new TextField();

	private ContextMenuScriptDirectory contextMenu = new ContextMenuScriptDirectory();
	private ContextMenuScriptGroup contextMenuGroups = new ContextMenuScriptGroup();

	private List<PaneSidebarDirectory> directoryEntries = new ArrayList();

	public PaneGroupsSidebar() {
		init();
	}

	private void init() {
		fieldFilter.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				String text = fieldFilter.getText().toLowerCase();

				int directoriesTotal = 0;
				int directoriesVisible = 0;

				int filesTotal = 0;
				int filesVisible = 0;

				for (PaneSidebarDirectory entry : directoryEntries) {
					boolean matchDirectoryName = false;
					String textToFind = text;

					if (text.isEmpty()) {
						matchDirectoryName = true;
					}

					// Directory name
					if (entry.getScriptDirectory().getName().toLowerCase().contains(text)) {
						matchDirectoryName = true;
					}

					if (matchDirectoryName) {
						textToFind = null;
					}

					int visibleAfterFiltering = entry.filter(textToFind);
					boolean hasContent = (visibleAfterFiltering > 0);

					boolean showDirectory = matchDirectoryName || hasContent;

					entry.getContent().setVisible(showDirectory);
					entry.getContent().setManaged(showDirectory);

					directoriesTotal++;
					if (showDirectory) {
						directoriesVisible++;
					}

					filesTotal += entry.getFileCount();
					filesVisible += visibleAfterFiltering;
				}

				boolean showFilterInfo = (directoriesTotal != directoriesVisible || filesTotal != filesVisible);
				if (text.isEmpty()) {
					// Reset filter info
					showFilterInfo = false;
				}

				if (showFilterInfo) {
					// Filtered

					filterDirectoriesInfo.setText(directoriesVisible + "/" + directoriesTotal + " Dirs");
					filterFilesInfo.setText(filesVisible + "/" + filesTotal + " Files");
				} else {
					filterDirectoriesInfo.setText("");
					filterFilesInfo.setText("");
				}
			}
		});
	}

	public void fill() {
		List<DirectoryGroup> groups = DirectoryConfigManager.getGroups();
		List<String> hiddenList = DirectoryConfigManager.getGroupTitlesHidden();

		vboxSidebar.getChildren().clear();

		directoryEntries.clear();

		for (DirectoryGroup group : groups) {
			List<DirectoryConfig> configs = group.getConfigs();

			String tag = group.getTag();

			boolean groupVisible = !(hiddenList.contains(tag));
			if (SidebarState.showAllTags) {
				groupVisible = true;
			}

			if (!groupVisible) {
				continue;
			}

			if (!configs.isEmpty()) {
				Pane titlePane = createGroupTitle(group.getTag());

				vboxSidebar.getChildren().add(titlePane);
			}

			// Directories

			for (DirectoryConfig config : configs) {
				ScriptDirectory scriptDirectory = config.getDirectory();

				boolean isCurrent = EqualsUtil.isEqual(SelectionState.currentDirectory, scriptDirectory.getDirectory());

				PaneSidebarDirectory paneScriptPath = new PaneSidebarDirectory(scriptDirectory, contextMenu, config.isCollapsed());
				Node node = paneScriptPath.getContent();

				if (isCurrent) {
					node.getStyleClass().add("pane_script_directory_selected");
				}

				vboxSidebar.getChildren().add(node);

				directoryEntries.add(paneScriptPath);
			}
		}
	}

	public void fillEditings() {
		vboxEditingStatus.getChildren().clear();

		for(PaneScriptDetails pane : CodeUnsavedPanes.getScriptsInEditing()) {
			GridPane gridExecution = new GridPane();
			gridExecution.setPadding(new Insets(2, 6, 2, 10));
			gridExecution.getStyleClass().add("sidebar-editing-row");

			gridExecution.setOnMouseClicked(e -> {
				PaneMain.inst.applyContent(pane);
			});

			ImageView image = FxIconUtils.getImageView("pencil.png");
			image.setFitWidth(INDICATOR_IMAGE_SIZE);
			image.setFitHeight(INDICATOR_IMAGE_SIZE);

			Label label = new Label(pane.getScript().getName());
			label.setPadding(new Insets(0, 0, 0, 6));

			Pane panePush = new Pane();
			Button buttonClose = new Button();

			FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png", (int)INDICATOR_IMAGE_SIZE);

			FxTooltip.setTooltip(buttonClose, Ln.get(K.IgnoreEditing));

			buttonClose.setOnAction(e -> {
				CodeUnsavedPanes.remove(pane);
				ControllerPages.updateEditings();
			});

			FxTooltip.setTooltip(image, Ln.get(K.ScriptHasUnsavedChanges));

			FxLayoutUtils.setFillWidth(panePush);

			int x = 0;
			gridExecution.add(image, x++, 0);
			gridExecution.add(label, x++, 0);
			gridExecution.add(panePush, x++, 0);
			gridExecution.add(buttonClose, x++, 0);

			vboxEditingStatus.getChildren().add(gridExecution);
		}
	}

	public void fillExecutions() {
		vboxExecutionStatus.getChildren().clear();

		ExecutionPanes.cleanupExecutions();

		for(IExecutePane pane : ExecutionPanes.getPanesExecuting()) {
			GridPane gridExecution = new GridPane();
			gridExecution.setPadding(new Insets(2, 6, 2, 10));
			gridExecution.getStyleClass().add("sidebar-execution-row");

			gridExecution.setOnMouseClicked(e -> {
				PaneMain.inst.applyContent(pane);
			});

			Pane space1 = new Pane();
			space1.setMinWidth(GAP_WIDTH);
			space1.setMaxWidth(GAP_WIDTH);

			ImageView image = FxIconUtils.getImageView("image-loader.gif");
			image.setFitWidth(INDICATOR_IMAGE_SIZE);
			image.setFitHeight(INDICATOR_IMAGE_SIZE);

			String title = null;
			if (pane.getScript() != null) {
				title = pane.getScript().getName();
			}
			if (title == null) {
				title = pane.getContentTitle();
			}

			Label label = new Label(title);

			Pane panePush = new Pane();
			Button buttonStop = new Button();

			FxIconUtils.setIconNoStyleWithSize(buttonStop, "close.png", (int)INDICATOR_IMAGE_SIZE);

			buttonStop.setOnAction(e -> {
				pane.triggerStopScript();
			});

			FxTooltip.setTooltip(image, Ln.get(K.ScriptIsExecuted));
			FxTooltip.setTooltip(buttonStop, Ln.get(K.StopScript));

			FxLayoutUtils.setFillWidth(panePush);

			int x = 0;
			gridExecution.add(label, x++, 0);
			gridExecution.add(panePush, x++, 0);
			gridExecution.add(image, x++, 0);
			gridExecution.add(space1, x++, 0);
			gridExecution.add(buttonStop, x++, 0);

			vboxExecutionStatus.getChildren().add(gridExecution);
		}
	}

	private Pane createGroupTitle(String groupTag) {
		Text text = new Text();
		text.setText(groupTag);
		text.getStyleClass().add("script_group_title");

		HBox titlePane = new HBox();
		titlePane.getStyleClass().add("script_group_title_wrapper");
		titlePane.getChildren().add(text);

		titlePane.setOnContextMenuRequested(e -> {
			FxContextMenuUtils.showContextMenu(e, contextMenuGroups.getContextMenu(groupTag), titlePane);
		});

		return titlePane;
	}

	@Override
	public Node getContent() {
		GridPane top = new GridPane();
		top.setHgap(5);

		Pane panePush = new Pane();

		VBox boxStatus = new VBox();
		boxStatus.getChildren().add(vboxEditingStatus);
		boxStatus.getChildren().add(vboxExecutionStatus);

		fieldFilter.setPromptText(Ln.get(K.Filter));
		fieldFilter.setMaxWidth(120);

		int y = 0;

		top.add(panePush, 0, y);
		top.add(filterDirectoriesInfo, 1, y);
		top.add(filterFilesInfo, 2, y);
		top.add(fieldFilter, 3, y);
		top.add(new Label(""), 4, y);

		paneMain.getStyleClass().add("pane_sidebar");

		vboxSidebar.setSpacing(0);

		paneMain.setTop(top);
		paneMain.setCenter(vboxSidebar);
		paneMain.setBottom(boxStatus);

		ScrollPane scrollPane = new ScrollPane(paneMain);
		FxScrollUtils.setFitTrue(scrollPane);
		FxBorderUtils.setBorderDisabled(scrollPane);

		FxLayoutUtils.setFillWidth(panePush);

		return scrollPane;
	}
}
