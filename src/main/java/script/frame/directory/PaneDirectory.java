package script.frame.directory;

import com.rwu.application.lang.Ln;
import com.rwu.fx.alerts.Alerts;
import com.rwu.fx.dialog.MultilineInputDialog;
import com.rwu.fx.dialog.OneValueInputDialog;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxBorderUtils;
import com.rwu.fx.util.FxContextMenuUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxScrollUtils;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.log.Log;
import com.rwu.misc.DateUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import script.app.App;
import script.controller.ControllerPages;
import script.controller.ControllerScripts;
import script.data.Script;
import script.data.ScriptDirectory;
import script.execute.ScriptFile;
import script.execute.ScriptInst;
import script.execute.ScriptRun;
import script.execute.intf.IScriptResultListener;
import script.execute.result.ScriptError;
import script.execute.result.ScriptRunning;
import script.frame.common.UiCommon;
import script.frame.execute.IExecutePane;
import script.frame.intf.IContent;
import script.frame.pages.PageTypeEnum;
import script.frame.scripts.PaneScript;
import script.frame.state.ExecutionPanes;
import script.frame.state.HasChangesPanes;
import script.lang.K;
import script.manager.ScriptDirectoryReader;
import script.metadata.directory.DirectoryMetadata;
import script.metadata.directory.DirectoryMetadataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Script-Directory: Shows content
 */
public class PaneDirectory implements IContent, IExecutePane, IScriptResultListener {

	private ScrollPane scrollPane = null;

	private VBox content = new VBox();

	// Title

	private Label labelTitle = new Label();
	private Label labelDescription = new Label();

	private Label labelPath = new Label();

	private Label labelAddScriptInfo = new Label();

	private Button buttonAddScript = new Button();
	private Button buttonSaveNewScript = new Button();
	private Button buttonCancelAddScript = new Button();

	private CheckBox checkIsIncludeDirectory = new CheckBox();

	private CheckMenuItem checkIsIncludeDir = new CheckMenuItem();

	private Button buttonMore = new Button();

	private TextField fieldNewScript = new TextField();

	private BorderPane paneLine2 = new BorderPane();

	private Button buttonClose = new Button();

	// Scripts

	private VBox boxScripts = new VBox();

	// Libraries

	private Label labelLibraries = new Label();

	private VBox boxLibraries = new VBox();

	private VBox boxAddScript = new VBox();
	private HBox boxAddScriptInput = new HBox();

	// Result

	private Label labelExecuted = new Label();

	private TextArea areaResult = new TextArea();

	private ScriptDirectory scriptDirectory = null;

	// Instances when executed
	private List<ScriptRunning> scriptsRunning = new ArrayList();

	// Edit
	private long lastChangesTime = 0;

	// Context

	private ContextMenu contextMenuMore = new ContextMenu();

	public PaneDirectory() {
		init();
		initContextMenus();
		initActions();

		applyButtonAdd();
	}

	private void init() {
		labelTitle.getStyleClass().add("titles");

		content.setSpacing(8);

		boxAddScript.setSpacing(8);
		boxAddScriptInput.setSpacing(8);

		buttonSaveNewScript.setText(Ln.get(K.SaveNewScript));
		buttonMore.setText("...");

		labelLibraries.setText(Ln.get(K.Libraries));

		labelAddScriptInfo.setText(Ln.get(K.AddScriptInfo));

		fieldNewScript.setOnKeyReleased((e) -> {
			if (e.getCode() == KeyCode.ENTER) {
				buttonSaveNewScript.fire();
			}
		});

		checkIsIncludeDirectory.setText(Ln.get(K.IsIncludeDirectory));

		FxTooltip.setTooltip(buttonClose, Ln.get(K.MenuClosePage));
		FxTooltip.setTooltip(buttonCancelAddScript, Ln.get(K.CANCEL));

		FxTooltip.setTooltip(checkIsIncludeDirectory, Ln.get(K.IsIncludeDirectory));

		FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png");
		FxIconUtils.setIconNoStyleWithSize(buttonCancelAddScript, "close.png");

		// Add

		boxAddScriptInput.getChildren().add(buttonCancelAddScript);
		boxAddScriptInput.getChildren().add(fieldNewScript);
		boxAddScriptInput.getChildren().add(buttonSaveNewScript);

		boxAddScript.getChildren().add(boxAddScriptInput);
		boxAddScript.getChildren().add(labelAddScriptInfo);
	}

	private void initContextMenus() {
		MenuItem itemEditTitle = new MenuItem(Ln.get(K.ChangeTitle));
		itemEditTitle.setOnAction(e -> {
			String preset = null;

			String path = scriptDirectory.getPath();
			DirectoryMetadata fileCustom = DirectoryMetadataManager.get(path);
			if (fileCustom != null) {
				preset = fileCustom.getTitle();
			}

			OneValueInputDialog dialog = new OneValueInputDialog(AppWindowFx.getStage(), Ln.get(K.InputTitle), null, Ln.get(K.ChangeTitleField), preset);
			boolean ok = dialog.open();
			if (!ok) {
				return;
			}

			String value = dialog.getValue1();
			DirectoryMetadataManager.setTitle(path, value);

			updateTitle();
		});

		MenuItem itemEditDescription = new MenuItem(Ln.get(K.ChangeDescription));
		itemEditDescription.setOnAction(e -> {
			String preset = null;

			String path = scriptDirectory.getPath();
			DirectoryMetadata fileCustom = DirectoryMetadataManager.get(path);
			if (fileCustom != null) {
				preset = fileCustom.getDescription();
			}

			MultilineInputDialog dialog = new MultilineInputDialog(AppWindowFx.getStage(), Ln.get(K.ChangeDescription), null, Ln.get(K.ChangeDescriptionField),
					preset);
			boolean ok = dialog.open();
			if (!ok) {
				return;
			}

			String value = dialog.getValue1();
			DirectoryMetadataManager.setDescription(path, value);

			updateTitle();
		});

		checkIsIncludeDir = new CheckMenuItem(Ln.get(K.IsIncludeDirectory));
		checkIsIncludeDir.setOnAction(e -> {
			DirectoryMetadataManager.setIsIncludeDirectory(scriptDirectory.getPath(), checkIsIncludeDir.isSelected());

			initValues();
		});

		contextMenuMore.getItems().add(itemEditTitle);
		contextMenuMore.getItems().add(itemEditDescription);
		contextMenuMore.getItems().add(new SeparatorMenuItem());
		contextMenuMore.getItems().add(checkIsIncludeDir);
	}

	private void initActions() {
		buttonAddScript.setOnAction(e -> {
			applyAddScript(false);
		});

		buttonSaveNewScript.setOnAction(e -> {
			String filename = fieldNewScript.getText().trim();
			if (filename.isEmpty()) {
				return;
			}

			if (!filename.contains(".")) {
				filename += ".groovy";
			}

			File newFile = new File(scriptDirectory.getDirectory(), filename);
			boolean created = false;
			try {
				if (newFile.exists()) {
					Alerts.showWarning(Ln.colonSpace(K.AddScriptExistsInfo) + filename, Ln.get(K.AddScriptExistsTitle));
					return;
				} else {
					File parent = newFile.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					created = newFile.createNewFile();
				}
			} catch (IOException e1) {
				Log.error("Failed to create file", e1);
			}

			if (created) {
				Script script = new Script(newFile.getParentFile(), filename);

				ControllerScripts.openDetails(script, false, true);

				ControllerPages.updateScriptDirectories();
			}

			applyCancelAddScript();

			reload();
		});

		buttonCancelAddScript.setOnAction(e -> {
			applyCancelAddScript();
		});

		buttonMore.setOnAction(e -> {
			FxContextMenuUtils.showContextMenu(contextMenuMore, buttonMore);
		});

		buttonClose.setOnAction(e -> {
			ControllerPages.closeCurrentPage();
		});

		checkIsIncludeDirectory.setOnAction(e -> {
			DirectoryMetadataManager.setIsIncludeDirectory(scriptDirectory.getPath(), checkIsIncludeDirectory.isSelected());

			initValues();
		});
	}

	private void initValues() {
		boolean isIncludeDirectory = false;

		DirectoryMetadata directoryMetadata = DirectoryMetadataManager.get(scriptDirectory.getPath());
		if (directoryMetadata != null) {
			isIncludeDirectory = directoryMetadata.isIncludeDirectory();
		}

		checkIsIncludeDir.setSelected(isIncludeDirectory);
		checkIsIncludeDirectory.setVisible(isIncludeDirectory);
		checkIsIncludeDirectory.setSelected(isIncludeDirectory);
	}

	private void updateTitle() {
		String title = Ln.get(K.ScriptDirectory);
		String description = null;

		if (scriptDirectory != null) {
			String path = scriptDirectory.getPath();
			DirectoryMetadata custom = DirectoryMetadataManager.get(path);

			if (custom != null) {
				title = custom.getTitle();

				description = custom.getDescription();
			} else {
				if (new File(path).equals(App.getConfigDir())) {
					description = Ln.get(K.PersonalDirDescription);
				}
			}
		}

		labelTitle.setText(title);
		labelDescription.setText(description);
	}

	/**
	 * Button to add
	 */
	private void applyButtonAdd() {
		FxTooltip.setTooltip(buttonAddScript, Ln.get(K.AddScript));
		FxIconUtils.setIconNoStyleWithSize(buttonAddScript, "plus.png");

		applyCancelAddScript();
	}

	/**
	 * Content to add script
	 */
	public void applyAddScript(boolean showInfoMessage) {
		paneLine2.setRight(boxAddScript);

		Platform.runLater(() -> {
			if (showInfoMessage) {
				Alerts.showInformation(Ln.get(K.AddScriptInPersonalDir), Ln.get(K.AddScript));
			}

			fieldNewScript.requestFocus();
		});
	}

	private void applyCancelAddScript() {
		paneLine2.setRight(buttonAddScript);
	}

	@Override
	public Node getContent() {
		if (scrollPane == null) {
			HBox boxLine1Right = new HBox();
			boxLine1Right.setSpacing(8);

			boxLine1Right.getChildren().add(labelPath);
			boxLine1Right.getChildren().add(buttonClose);

			BorderPane line1 = new BorderPane();
			line1.setLeft(labelTitle);
			line1.setRight(boxLine1Right);
			line1.setCenter(labelDescription);

			HBox boxButtonsRight = new HBox();
			boxButtonsRight.setSpacing(8);
			boxButtonsRight.getChildren().add(checkIsIncludeDirectory);
			boxButtonsRight.getChildren().add(buttonAddScript);
			boxButtonsRight.getChildren().add(buttonMore);

			areaResult.setVisible(false);

			paneLine2.setRight(boxButtonsRight);

			content.getChildren().add(line1);
			content.getChildren().add(paneLine2);

			content.getChildren().add(boxScripts);

			content.getChildren().add(labelLibraries);
			content.getChildren().add(boxLibraries);

			content.getChildren().add(areaResult);

			content.getChildren().add(labelExecuted);

			content.setPadding(UiCommon.getContentDefaultInsets());

			scrollPane = new ScrollPane(content);
			FxScrollUtils.setFitTrue(scrollPane);
			FxBorderUtils.setBorderDisabled(scrollPane);

			content.getStyleClass().add("pane_directory");
		}

		return scrollPane;
	}

	public void fill(ScriptDirectory scriptDirectory) {
		this.scriptDirectory = scriptDirectory;

		List<Script> scripts = scriptDirectory.readScripts();

		// Clear
		boxScripts.setSpacing(12);
		boxScripts.getChildren().clear();

		boxLibraries.getChildren().clear();

		for (Script script : scripts) {
			PaneScript paneScript = new PaneScript(this, script);

			boxScripts.getChildren().add(paneScript.getContent());
		}

		// Libraries
		List<File> files = ScriptDirectoryReader.getLibraryFilesOfDirectory(scriptDirectory.getDirectory());
		for (File file : files) {
			Label label = new Label(file.getName());

			boxLibraries.getChildren().add(label);
		}

		labelLibraries.setVisible(!boxLibraries.getChildren().isEmpty());

		// Texts
		labelPath.setText(scriptDirectory.getPath());

		initValues();

		// Reset
		//labelExecuted.setText("");

		updateTitle();
	}

	@Override
	public void triggerRunScript() {
		// Not applicable
	}

	@Override
	public void triggerStopScript() {
		for(ScriptRunning running : scriptsRunning) {
			running.interruptThread();
		}
	}

	public void runScript(File file) {
		ScriptInst instCurrent = new ScriptInst(null);
		ScriptFile scriptFileCurrent = new ScriptFile(file);

		ScriptRunning scriptRunning = ScriptRun.runScriptInThread(null, instCurrent, scriptFileCurrent).getScriptRunning();

		// Error
		List<ScriptError> error = scriptRunning.getAndResetError();

		String errorText = ScriptError.getDisplayInformation(error);
		if (errorText != null) {
			// Only compile errors visible (Execution in Thread)

			areaResult.setText(errorText);
			areaResult.setVisible(true);
		} else {
			areaResult.setText(null);
		}

		scriptsRunning.add(scriptRunning);

		lastChangesTime = new Date().getTime();

		ExecutionPanes.add(this);
		HasChangesPanes.setHasChanges(this);
	}

	@Override
	public void applyScriptFinished(int id) {
		ScriptRunning scriptRunning = null;
		for (ScriptRunning current : scriptsRunning) {
			if (current.getThread().matchInstId(id)) {
				scriptRunning = current;
				break;
			}
		}

		if (scriptRunning == null) {
			return;
		}

		scriptsRunning.remove(scriptRunning);

		final ScriptRunning scriptRunningFinal = scriptRunning;

		Platform.runLater(() -> {
			// Error
			List<ScriptError> error = scriptRunningFinal.getAndResetError();

			String errorText = ScriptError.getDisplayInformation(error);
			if (errorText != null) {
				areaResult.appendText(errorText);
				areaResult.setVisible(true);
			}

			labelExecuted.setText(scriptRunningFinal.getScriptFile().getFile().getName() + ": " + DateUtils.formatDateTimeWithSeconds(new Date()));
		});
	}

	@Override
	public boolean hasInstId(int id) {
		for (ScriptRunning scriptRunning : scriptsRunning) {
			if (scriptRunning.getThread().matchInstId(id)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void addOutput(String text) {
		Platform.runLater(() -> {
			areaResult.appendText(text);
			areaResult.setVisible(true);

			//EditingPanes.add(this);
			//ControllerPages.updateEditings();
		});
	}

	@Override
	public boolean hasRunningScript() {
		for (ScriptRunning scriptRunning : scriptsRunning) {
			if (scriptRunning.isRunning()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equalsExecution(PageTypeEnum pageType, Script scriptOther) {
		if (pageType != getPageType()) {
			return false;
		}

		return scriptOther.equals(getScript());
	}

	@Override
	public Script getScript() {
		for (ScriptRunning scriptRunning : scriptsRunning) {
			return scriptRunning.getScriptFile().asScript();
		}

		return null;
	}

	@Override
	public String getContentTitle() {
		return scriptDirectory.getName();
	}

	@Override
	public long getStartTime() {
		for (ScriptRunning scriptRunning : scriptsRunning) {
			if (scriptRunning.isRunning()) {
				return scriptRunning.getThread().getStartTime();
			}
		}

		return 0;
	}

	@Override
	public PageTypeEnum getPageType() {
		return PageTypeEnum.ScriptDirectory;
	}

	public boolean hasScriptRunningWithInstId(int id) {
		for (ScriptRunning current : scriptsRunning) {
			if (current.getThread().matchInstId(id) && current.isRunning()) {
				return true;
			}
		}

		return false;
	}

	private void reload() {
		fill(this.scriptDirectory);
	}

	public ScriptDirectory getScriptDirectory() {
		return scriptDirectory;
	}

	public boolean equalsDirectory(ScriptDirectory other) {
		return scriptDirectory.equals(other);
	}

	public long getLastChangesTime() {
		return lastChangesTime;
	}
}
