package script.frame.details;

import com.anchorage.docks.containers.StageFloatable;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.DockNode.DockPosition;
import com.anchorage.docks.node.interfaces.IDockNodeListener;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import com.rwu.application.lang.Ln;
import com.rwu.fx.dialog.DialogArrange;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxContextMenuUtils;
import com.rwu.fx.util.FxControlUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.log.Log;
import com.rwu.misc.DateUtils;
import com.rwu.misc.StringUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import script.Output;
import script.controller.ControllerFileSave;
import script.controller.ControllerPages;
import script.data.Script;
import script.execute.*;
import script.execute.intf.IScriptResultListener;
import script.execute.result.ScriptError;
import script.execute.result.ScriptRunning;
import script.frame.code.PaneCodeOnly;
import script.frame.common.UiCommon;
import script.frame.css.CssConstants;
import script.frame.details.contextmenu.ContextMenuDetailsTitle;
import script.frame.details.contextmenu.ContextMenuMore;
import script.frame.details.contextmenu.ContextMenuSnippetsInput;
import script.frame.details.contextmenu.ContextMenuSnippetsOutput;
import script.frame.details.input.IInputParent;
import script.frame.details.input.PaneInput;
import script.frame.details.input.PaneInputs;
import script.frame.execute.IExecutePane;
import script.frame.intf.IContent;
import script.frame.listener.ICodeEditListener;
import script.frame.manage.ICloseRequest;
import script.frame.pages.PageTypeEnum;
import script.frame.state.CodeUnsavedPanes;
import script.frame.state.ExecutionPanes;
import script.frame.state.HasChangesPanes;
import script.input.InputConfig;
import script.lang.K;
import script.manager.ScriptReader;
import script.metadata.directory.DirectoryMetadataManager;
import script.metadata.file.FileMetadata;
import script.metadata.file.FileMetadataFromRegistryManager;
import script.metadata.file.FileMetadataManager;
import script.recent.RecentFileManager;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;
import script.recent.ScriptValue;
import script.repeat.IRepeatListeners;
import script.repeat.RepeatThread;
import script.repeat.RepeatUtils;
import script.util.FileUtils;
import script.util.Lists;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Details of script
 */
public class PaneScriptDetails implements IScriptResultListener, IRepeatListeners, ICloseRequest, IContent, IExecutePane {

	private DockStation stationTop = new DockStation();
	private DockStation stationBottom = new DockStation();

	private VBox paneFixedSections = new VBox();
	private BorderPane paneMain = new BorderPane();

	private BorderPane containerScriptRun = new BorderPane();

	private Script script;
	private ScriptPreparation scriptPreparation = new ScriptPreparation();
	private ScriptInst inst;

	// Title

	private Label labelTitle = new Label();
	private Label labelSubtitle = new Label();

	private Label labelPath = new Label();
	private Label labelDescription = new Label();

	private Label labelMetainfo = new Label();

	private Button buttonClose = new Button();

	// Script

	private PaneCodeOnly paneCodeOnly;
	private PaneCodeOnly paneFullScript;

	private DockNode dockNodeCode;
	private DockNode dockNodeInput;
	private DockNode dockNodeOutput;
	private DockNode dockNodeOutputHtml;

	private DockNode dockNodeFullScript;

	// Execute

	private Button buttonRun = new Button();

	private PaneOutput paneOutput = new PaneOutput();
	private PaneOutputHtml paneOutputHtml = new PaneOutputHtml();

	// Inputs

	private PaneInputs paneInputs = new PaneInputs();

	// Options

	private CheckBox checkRepeat = new CheckBox();
	private ChoiceBox<String> choiceSeconds = new ChoiceBox<>();

	private int secondsCounter = 0;

	private Button buttonAddInput = new Button();
	private Button buttonMore = new Button("..");

	@Deprecated
	private ToggleButton buttonToggleEdit = new ToggleButton();
	private ToggleButton buttonToggleShowInput = new ToggleButton();
	private ToggleButton buttonToggleRepeat = new ToggleButton();
	private ToggleButton buttonToggleShowCode = new ToggleButton();
	private ToggleButton buttonToggleShowOutput = new ToggleButton();

	// Inputs

	private List<InputConfig> inputsConfig = new ArrayList<>();

	// History

	private Label runInformation = new Label();
	private Button buttonStop = new Button();

	// Dynamic

	private VBox boxRepeat = new VBox();

	private Button buttonSaveCode = new Button();
	private Button buttonSelectSnippetsInput = new Button();
	private Button buttonSelectSnippetsOutput = new Button();
	private Button buttonReloadFieldsFromScript = new Button();

	private Button buttonIncludes = new Button();
	private Button buttonShowFullScript = new Button();

	private Label labelIncludes = new Label();

	// File changed

	private VBox boxFileChanged = new VBox();

	private Label labelFileChanged = new Label();

	private Button buttonReloadFileContent = new Button();

	// Context menus

	private ContextMenuDetailsTitle contextMenuDetailsTitle;
	private ContextMenuMore contextMenuMore;
	private ContextMenuSnippetsInput contextMenuSnippetsInput;
	private ContextMenuSnippetsOutput contextMenuSnippetsOutput;

	// Execution

	private ScriptRunning scriptRunning = null;

	// User has changed something
	private long lastEditedTime = 0;
	private List<Date> executionTimes = new ArrayList();

	// Last time user saved
	private long lastSaveTime = -1;

	public PaneScriptDetails(Script script) {
		this.script = script;

		contextMenuDetailsTitle = new ContextMenuDetailsTitle(this);
		contextMenuMore = new ContextMenuMore(this);
		contextMenuSnippetsInput = new ContextMenuSnippetsInput(this);
		contextMenuSnippetsOutput = new ContextMenuSnippetsOutput(this);

		init();
		initLang();
		initActions();

		choiceSeconds.getItems().addAll("1 s", "2 s", "3 s", "4 s", "5 s", "6 s", "7 s", "8 s", "10 s", "20 s", "30 s", "60", "2 m", "5 m", "10 m", "15 m",
				"30 m", "60 m");
		choiceSeconds.getItems().addAll("1 h", "2 h", "3 h", "4 h", "5 h", "6 h", "7 h", "8 h", "12 h", "24 h");

		choiceSeconds.getSelectionModel().select("10 s");

		initValues();

		onEditingChanged();
	}

	private void init() {
		paneFixedSections.setPadding(UiCommon.getContentDefaultInsets());
		paneFixedSections.setSpacing(6);

		stationTop.setBackground(null);
		stationBottom.setBackground(null);

		CssConstants.setTitle(labelTitle);
		CssConstants.setSubtitle(labelSubtitle);

		labelMetainfo.setText(script.getLinesCount() + " " + Ln.get(K.LinesCount));

		buttonStop.setDisable(true);
		buttonSaveCode.setDisable(true);

		buttonToggleShowInput.setSelected(true);

		labelIncludes.setPadding(UiCommon.getLabelSpaceTop());

		FxIconUtils.setButtonStyleFlat(buttonStop);

		FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png");

		FxIconUtils.setIconNoStyleWithSize(buttonRun, "control_play_blue.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonSaveCode, "save.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonSelectSnippetsInput, "textfield.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonSelectSnippetsOutput, "source_code.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonReloadFieldsFromScript, "refresh_all.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonIncludes, "document_break.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonShowFullScript, "text_prose.png", FxIconUtils.ICON_SIZE_MEDIUM);

		FxIconUtils.setIconNoStyleWithSize(buttonToggleEdit, "edit_button.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonToggleShowInput, "edit_button.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonToggleRepeat, "arrow_repeat.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonAddInput, "plus.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonToggleShowCode, "text_prose.png", FxIconUtils.ICON_SIZE_MEDIUM);
		FxIconUtils.setIconNoStyleWithSize(buttonToggleShowOutput, "align_compact.png", FxIconUtils.ICON_SIZE_MEDIUM);
	}

	private void initLang() {
		buttonRun.setText(Ln.get(K.RunScript));
		buttonStop.setText(Ln.get(K.StopScript));

		checkRepeat.setText(Ln.get(K.RepeatExecution));

		labelFileChanged.setText(Ln.get(K.ScriptFileWasChanged));
		buttonReloadFileContent.setText(Ln.get(K.ScriptFileReloadChanges));

		FxTooltip.setTooltip(buttonAddInput, Ln.dots(K.AddInput));
		FxTooltip.setTooltip(buttonToggleRepeat, Ln.get(K.RepeatExecution));
		FxTooltip.setTooltip(buttonToggleShowCode, Ln.get(K.ScriptShowCode));
		FxTooltip.setTooltip(buttonToggleShowOutput, Ln.get(K.ShowOutput));

		FxTooltip.setTooltip(buttonClose, Ln.get(K.MenuClosePage));
		FxTooltip.setTooltip(buttonIncludes, Ln.get(K.ScriptsIncluded));
		FxTooltip.setTooltip(buttonShowFullScript, Ln.get(K.DockFullScript));

		FxTooltip.setTooltip(buttonSelectSnippetsInput, Ln.dots(K.AddInput));
		FxTooltip.setTooltip(buttonSelectSnippetsOutput, Ln.dots(K.AddOutput));
		FxTooltip.setTooltip(buttonReloadFieldsFromScript, Ln.dots(K.ReloadFieldsFromFile));
	}

	private void initActions() {
		// Title

		labelTitle.setOnContextMenuRequested(e -> {
			FxContextMenuUtils.showContextMenu(e, contextMenuDetailsTitle.getContextMenu(), labelTitle);
		});

		buttonClose.setOnAction(e -> {
			ControllerPages.closeCurrentPage();
		});

		// Options

		buttonRun.setOnAction(e -> {
			paneOutput.clearOutput();

			triggerRunScript();
		});
		buttonStop.setOnAction(e -> {
			triggerStopScript();
		});

		checkRepeat.setOnAction(e -> {
			boolean selected = checkRepeat.isSelected();

			if (selected) {
				triggerRunScript();

				storeIntervalSeconds();

				RepeatThread.inst.addListener(this);
			} else {
				RepeatThread.inst.removeListener(this);
			}
		});

		choiceSeconds.setOnAction(e -> {
			// Seconds

			if (checkRepeat.isSelected()) {
				storeIntervalSeconds();
			}
		});

		buttonMore.setOnAction(e -> {
			FxContextMenuUtils.showContextMenu(contextMenuMore.getContextMenu(), buttonMore);
		});

		buttonToggleEdit.setOnAction(e -> {
			addInputSection();

			onEditingChanged();
		});

		buttonAddInput.setOnAction(e -> {
			addInput();
		});

		buttonToggleRepeat.setOnAction(e -> {
			setShowRepeat(buttonToggleRepeat.isSelected());
		});

		buttonToggleShowInput.setOnAction(e -> {
			setShowInput(buttonToggleShowInput.isSelected());
		});

		buttonToggleShowCode.setOnAction(e -> {
			setShowCode(buttonToggleShowCode.isSelected());
		});

		buttonToggleShowOutput.setOnAction(e -> {
			applyShowOutput(buttonToggleShowOutput.isSelected(), true);
		});

		// Basics

		buttonSaveCode.setOnAction(e -> {
			performSave();
		});

		buttonSelectSnippetsInput.setOnAction(e -> {
			contextMenuSnippetsInput.initEntries();

			FxContextMenuUtils.showContextMenu(contextMenuSnippetsInput.getContextMenu(), buttonSelectSnippetsInput);
		});

		buttonSelectSnippetsOutput.setOnAction(e -> {
			contextMenuSnippetsOutput.initEntries();

			FxContextMenuUtils.showContextMenu(contextMenuSnippetsOutput.getContextMenu(), buttonSelectSnippetsOutput);
		});

		buttonReloadFieldsFromScript.setOnAction(e -> {
			updateInputs();
		});

		buttonIncludes.setOnAction(e -> {
			editIncludes();
		});

		buttonShowFullScript.setOnAction(e -> {
			setShowFullScript(true);
		});

		buttonReloadFileContent.setOnAction(e -> {
			String filetext = readFileTextFromFile();
			paneCodeOnly.setText(filetext);

			FxControlUtils.setVisible(boxFileChanged, false);
		});
	}

	private void onEditingChanged() {
		boolean editing = buttonToggleEdit.isSelected();

		FxControlUtils.setVisible(buttonAddInput, buttonToggleEdit.isSelected());

		paneInputs.setEditing(editing);
	}

	private void initValues() {
		ObservableList<String> items = choiceSeconds.getItems();

		labelPath.setText(script.getFile().getParent());

		String path = getScriptFullPath();

		FileMetadata fileCustom = FileMetadataManager.getCombined(path);
		if (fileCustom != null) {
			// Seconds

			for (String item : items) {
				int seconds = RepeatUtils.parseInterval(item);

				if (seconds == fileCustom.getRepeatSeconds()) {
					choiceSeconds.getSelectionModel().select(item);

					break;
				}
			}
		}

		updateInputs();

		updateTitle();

		onIncludesChanges();

		setShowRepeat(false);
	}

	private void updateTitle() {
		String path = getScriptFullPath();

		FileMetadata fileCustom = FileMetadataManager.getCombined(path);

		String title = script.getName();

		if (fileCustom != null) {
			// Title

			String titleCustom = fileCustom.getTitle();
			if (StringUtils.isValue(titleCustom)) {
				title = titleCustom;
			}
		}

		labelTitle.setText(title);

		FxTooltip.setTooltip(labelTitle, script.getFile().getName());
		FxTooltip.setTooltip(labelPath, script.getFile().getName());
	}

	private void updateInputs() {
		FileMetadata fileCustom = FileMetadataManager.getCombined(getScriptFullPath());
		if (fileCustom != null) {
			inputsConfig.clear();
			inputsConfig.addAll(fileCustom.getInputs());

			updateInputs(true);
		}
	}

	/**
	 * Update list of input fields
	 */
	private void updateInputs(boolean applyRecent) {
		paneInputs.reset();

		IInputParent removeHandler = new IInputParent() {
			@Override
			public void remove(InputConfig config) {
				inputsConfig.remove(config);

				updateInputs(applyRecent);
			}

			@Override
			public int getIndex(InputConfig config) {
				return inputsConfig.indexOf(config);
			}

			@Override
			public void storeInputs() {
				PaneScriptDetails.this.storeInputs();
			}
		};

		List<ScriptRecent> recents = ScriptRecentManager.getRecents(script.getFile());
		if (applyRecent) {
			ScriptRecent recent = null;
			if (!recents.isEmpty()) {
				recent = recents.get(recents.size()-1);
			}

			if (recent != null) {
				int i=0;
				for(InputConfig inputConfig : inputsConfig) {
					ScriptValue forIndex = recent.getForIndex(i);

					if (forIndex != null) {
						inputConfig.setInputText(forIndex.getStringValue());
					}

					i++;
				}
			}
		}

		paneInputs.fill(inputsConfig, recents, removeHandler, buttonToggleEdit.isSelected());

		storeInputs();
	}

	/**
	 * Save time interval in configuration
	 */
	@Deprecated
	private void storeIntervalSeconds() {
		String intervalStr = choiceSeconds.getSelectionModel().getSelectedItem();
		if (intervalStr == null) {
			return;
		}

		File file = script.getFile();
		int interval = getIntervalSeconds();

		//System.err.println("Store interval: " + interval);

		FileMetadataManager.setIntervalSeconds(file.getAbsolutePath(), interval);
	}

	/**
	 * Execute
	 */
	public void triggerRunScript() {
		if (!buttonSaveCode.isDisable()) {
			// Save
			performSave();
		}

		// Inputs
		boolean ok = checkInputs();
		if (!ok) {
			return;
		}

		if (scriptRunning != null && scriptRunning.isRunning()) {
			// Is still running
			return;
		}

		// Repeat
		secondsCounter = 0;

		onExecutionStarted();

		File filepath = script.getFile();

		ScriptFile file = createScriptFile();
		file.setInputs(inputsConfig);

		inst = new ScriptInst(scriptPreparation);

		ScriptStartResult startResult = ScriptRun.runScriptInThread(null, inst, file);
		scriptRunning = startResult.getScriptRunning();

		// Recent
		ScriptRecent scriptRecent = startResult.getScriptRecent();
		if (scriptRecent != null) {
			executionTimes.add(scriptRecent.getTime());
		}

		List<ScriptError> error = scriptRunning.getAndResetError();

		String errorText = ScriptError.getDisplayInformation(error);
		if (errorText != null) {
			// Only compile errors visible (Execution in Thread)

			paneOutput.setText(errorText);
			applyShowOutput(true, true);

			onExecutionEnded();
		} else {
			paneOutput.clearOutput();
		}

		RecentFileManager.addEntry(filepath);

		ExecutionPanes.add(this);
	}

	@Override
	public void triggerStopScript() {
		scriptRunning.interruptThread();

		onExecutionEnded();
	}

	public void triggerShowEditor() {
		buttonToggleShowCode.setSelected(true);

		setShowCode(buttonToggleShowCode.isSelected());
	}

	private void onExecutionStarted() {
		buttonStop.setDisable(false);
		buttonRun.setDisable(true);
	}

	private void onExecutionEnded() {
		buttonStop.setDisable(true);
		buttonRun.setDisable(false);
	}

	public void setNewTitle(String title) {
		updateTitle();
	}

	public void setShowInput(boolean showInput) {
		addInputSection();

		if (showInput) {
			dockNodeInput.dock(stationTop, DockPosition.CENTER, 1);
		} else {
			if (dockNodeInput != null) {
				dockNodeInput.undock();
			}
		}
	}

	public void setShowRepeat(boolean showRepeat) {
		boxRepeat.setVisible(showRepeat);
	}

	public void setShowCode(boolean showCode) {
		if (showCode) {
			String scriptText = readFileTextFromFile();

			ICodeEditListener listener = new ICodeEditListener() {
				@Override
				public void textChanged() {
					PaneScriptDetails.this.lastEditedTime = new Date().getTime();

					CodeUnsavedPanes.add(PaneScriptDetails.this);
					ControllerPages.updateEditings();

					buttonSaveCode.setDisable(false);
				}
			};

			if (paneCodeOnly == null) {
				paneCodeOnly = new PaneCodeOnly(listener);
			}

			BorderPane paneTop = new BorderPane();

			Pane space = new Pane();
			Pane space2 = new Pane();
			Pane space3 = new Pane();

			space.setMinWidth(12);
			space2.setMinWidth(12);
			space3.setMinWidth(12);

			Pane spaceSmall1 = new Pane();
			spaceSmall1.setMinWidth(4);

			HBox boxButtons = new HBox();
			boxButtons.getChildren().add(buttonSaveCode);
			boxButtons.getChildren().add(space);
			boxButtons.getChildren().add(buttonSelectSnippetsInput);
			boxButtons.getChildren().add(buttonSelectSnippetsOutput);
			boxButtons.getChildren().add(space2);
			boxButtons.getChildren().add(buttonReloadFieldsFromScript);
			boxButtons.getChildren().add(space3);
			boxButtons.getChildren().add(buttonIncludes);
			boxButtons.getChildren().add(spaceSmall1);
			boxButtons.getChildren().add(labelIncludes);
			boxButtons.getChildren().add(buttonShowFullScript);
			paneTop.setLeft(boxButtons);

			BorderPane codeContainer = new BorderPane();
			codeContainer.setTop(paneTop);
			codeContainer.setCenter(paneCodeOnly.getContent());

			//codeContainer.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

			dockNodeCode = AnchorageSystem.createDock(Ln.get(K.DockCode), null, codeContainer);
			dockNodeCode.getContent().setHideTitle();
			dockNodeCode.dock(stationBottom, DockPosition.TOP, 1);

			dockNodeCode.getContent().showBarPanel();

			dockNodeCode.setDockNodeListener(new IDockNodeListener() {
				@Override
				public void dockNodeUndocked(DockNode dockNode) {
				}

				@Override
				public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
				}

				@Override
				public void afterDockNodeClosed(StageFloatable stageFloatable) {
					updateToggleButtons();
				}
			});

			paneCodeOnly.setText(scriptText);

			HasChangesPanes.setHasChanges(this);
		} else {
			if (dockNodeCode != null) {
				dockNodeCode.undock();
			}
		}
	}

	public void setShowFullScript(boolean showFullScript) {
		if (dockNodeFullScript == null) {
			paneFullScript = new PaneCodeOnly(null);

			dockNodeFullScript = AnchorageSystem.createDock(Ln.get(K.DockFullScript), null, paneFullScript.getContent());
			dockNodeFullScript.getContent().setHideTitle();

			dockNodeFullScript.getContent().showBarPanel();

			dockNodeFullScript.setDockNodeListener(new IDockNodeListener() {
				@Override
				public void dockNodeUndocked(DockNode dockNode) {
				}

				@Override
				public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
				}

				@Override
				public void afterDockNodeClosed(StageFloatable stageFloatable) {
					dockNodeFullScript = null;
					paneFullScript = null;
				}
			});
		}

		if (showFullScript) {
			ScriptRun.prepareScript(scriptPreparation, createScriptFile());
			paneFullScript.setText(scriptPreparation.getScript());

			dockNodeFullScript.dock(stationBottom, DockPosition.CENTER, 1);
		} else {
			if (dockNodeFullScript != null) {
				dockNodeFullScript.undock();
			}
		}
	}

	private String readFileTextFromFile() {
		String scriptText = ScriptReader.readScript(script.getFile());

		updateLastSaveTimeFromFile();

		return scriptText;
	}

	private void updateLastSaveTimeFromFile() {
		lastSaveTime = script.getFile().lastModified();
	}

	/**
	 * Save input fields configuratiom
	 *
	 * @deprecated Do not use registry for fields
	 */
	public void storeInputs() {
		//FileMetadataManager.setInputs(getPath(), inputsConfig);
	}

	@Override
	public Node getContent() {
		if (paneMain.getCenter() == null) {
			// Above
			addTopInfo();

			// Execute
			addRunSection();

			// Input
			addInputSection();

			// Output
			addScriptRunSection();

			containerScriptRun.setPadding(new Insets(10));
			containerScriptRun.setTop(stationTop);
			containerScriptRun.setCenter(stationBottom);

			//containerScriptRun.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

			paneMain.setTop(paneFixedSections);
			paneMain.setCenter(containerScriptRun);

			//paneMain.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, null, null)));

			paneMain.getStyleClass().add("pane_script_details");
		}

		return paneMain;
	}

	/**
	 * Info above
	 */
	private void addTopInfo() {
		BorderPane paneTopInfo = new BorderPane();

		HBox boxTitle = new HBox();
		VBox boxInfo = new VBox();

		HBox boxInfoWrapper = new HBox();
		boxInfoWrapper.setSpacing(8);

		boxFileChanged.setSpacing(8);

		boxTitle.getChildren().add(labelTitle);
		boxTitle.getChildren().add(labelSubtitle);
		boxTitle.getChildren().add(labelDescription);

		boxInfo.getChildren().add(labelPath);
		boxInfo.getChildren().add(labelMetainfo);

		boxFileChanged.getChildren().add(labelFileChanged);
		boxFileChanged.getChildren().add(buttonReloadFileContent);

		boxInfoWrapper.getChildren().add(boxFileChanged);
		boxInfoWrapper.getChildren().add(boxInfo);
		boxInfoWrapper.getChildren().add(buttonClose);

		paneTopInfo.setLeft(boxTitle);
		paneTopInfo.setRight(boxInfoWrapper);

		paneFixedSections.getChildren().add(paneTopInfo);

		FxControlUtils.setVisible(boxFileChanged, false);
	}

	/**
	 * Execute
	 */
	private void addRunSection() {
		BorderPane section = new BorderPane();

		boxRepeat.setSpacing(4);
		boxRepeat.getChildren().add(checkRepeat);
		boxRepeat.getChildren().add(choiceSeconds);

		Pane space1 = new Pane();
		Pane space2 = new Pane();

		space1.setMinWidth(6);
		space2.setMinWidth(6);

		HBox container = new HBox();
		HBox right = new HBox();
		right.setSpacing(8);
		container.setSpacing(8);

		container.setAlignment(Pos.TOP_LEFT);
		right.setAlignment(Pos.TOP_LEFT);

		container.getChildren().add(buttonRun);
		container.getChildren().add(buttonStop);
		container.getChildren().add(runInformation);

		// Repeat
		right.getChildren().add(boxRepeat);

		//right.getChildren().add(buttonAddInput);
		//right.getChildren().add(buttonToggleEdit);

		right.getChildren().add(buttonToggleRepeat);
		right.getChildren().add(space1);

		right.getChildren().add(buttonToggleShowInput);
		right.getChildren().add(buttonToggleShowOutput);
		right.getChildren().add(space2);

		right.getChildren().add(buttonToggleShowCode);

		//right.getChildren().add(buttonMore);

		section.setLeft(container);
		section.setRight(right);

		paneFixedSections.getChildren().add(section);
	}

	/**
	 * Output
	 */
	@Deprecated
	private void addScriptRunSection() {
	}

	@Override
	public void addOutput(String text) {
		Platform.runLater(() -> {
			paneOutput.appendText(text);

			applyShowOutput(true, true);
		});
	}

	public void addInput() {
		InputConfig config = new InputConfig();
		this.inputsConfig.add(config);

		updateInputs(true);
	}

	/**
	 * Input
	 */
	private void addInputSection() {
		if (dockNodeInput == null) {
			dockNodeInput = AnchorageSystem.createDock(Ln.get(K.DockInput), null, paneInputs.getContent());
			dockNodeInput.getContent().setHideTitle();
			dockNodeInput.dock(stationTop, DockPosition.CENTER, 1);

			dockNodeInput.getContent().showBarPanel();

			dockNodeInput.setDockNodeListener(new IDockNodeListener() {
				@Override
				public void dockNodeUndocked(DockNode dockNode) {
				}

				@Override
				public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
				}

				@Override
				public void afterDockNodeClosed(StageFloatable stageFloatable) {
					dockNodeInput = null;

					updateToggleButtons();
				}
			});
		}
	}

	private void applyShowOutput(boolean show, boolean setHasChanges) {
		if (show) {
			if (dockNodeOutput == null) {
				dockNodeOutput = AnchorageSystem.createDock(Ln.get(K.DockOutput), null, paneOutput.getContent());
				dockNodeOutput.getContent().setHideTitle();
				dockNodeOutput.dock(stationBottom, DockPosition.BOTTOM, 0.5);

				dockNodeOutput.getContent().showBarPanel();

				dockNodeOutput.setDockNodeListener(new IDockNodeListener() {
					@Override
					public void dockNodeUndocked(DockNode dockNode) {
					}

					@Override
					public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
					}

					@Override
					public void afterDockNodeClosed(StageFloatable stageFloatable) {
						dockNodeOutput = null;

						updateToggleButtons();
					}
				});
			}

			if (show && setHasChanges) {
				HasChangesPanes.setHasChanges(this);
			}
		} else {
			if (dockNodeOutput != null) {
				dockNodeOutput.undock();
				dockNodeOutput = null;
			}
		}
	}

	private void applyShowOutputHtml(boolean show) {
		if (show) {
			if (dockNodeOutputHtml == null) {
				paneOutputHtml.init();

				dockNodeOutputHtml = AnchorageSystem.createDock(Ln.get(K.DockOutputHtml), null, paneOutputHtml.getContent());
				dockNodeOutputHtml.getContent().setHideTitle();
				dockNodeOutputHtml.dock(stationBottom, DockPosition.BOTTOM, 0.5);

				dockNodeOutputHtml.getContent().showBarPanel();

				dockNodeOutputHtml.setDockNodeListener(new IDockNodeListener() {
					@Override
					public void dockNodeUndocked(DockNode dockNode) {
					}

					@Override
					public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
					}

					@Override
					public void afterDockNodeClosed(StageFloatable stageFloatable) {
						dockNodeOutputHtml = null;
					}
				});
			}
		} else {
			if (dockNodeOutputHtml != null) {
				dockNodeOutputHtml.undock();
				dockNodeOutputHtml = null;
			}
		}
	}

	private void editIncludes() {
		List<String> filenames = DirectoryMetadataManager.getIncludeFilesList().stream().map(file -> file.getAbsolutePath()).collect(Collectors.toList());

		List<String> includesNotSelected = new ArrayList();
		includesNotSelected.addAll(filenames);

		String path = getScriptFullPath();
		FileMetadata fileMetadata = FileMetadataFromRegistryManager.get(path);
		if (fileMetadata != null) {
			includesNotSelected.removeAll(fileMetadata.getIncludes());
		}

		DialogArrange arrange = new DialogArrange(AppWindowFx.getStage(), Ln.get(K.SelectIncludes), Ln.get(K.SelectIncludesInfo), filenames,
				includesNotSelected, true);
		boolean result = arrange.open();
		if (!result) {
			return;
		}

		if (fileMetadata == null) {
			fileMetadata = FileMetadataFromRegistryManager.getOrCreate(path);
		}

		fileMetadata.getIncludes().clear();

		List<String> values = new ArrayList(arrange.getValues());
		values.removeAll(arrange.getHidden());

		fileMetadata.getIncludes().addAll(values);

		FileMetadataFromRegistryManager.store();

		// Update
		onIncludesChanges();
	}

	/**
	 * Script has terminated (from Thread)
	 */
	@Override
	public void applyScriptFinished(int id) {
		Platform.runLater(() -> {
			applyRunHistory();

			// Error
			List<ScriptError> error = scriptRunning.getAndResetError();

			String errorText = ScriptError.getDisplayInformation(error);
			if (errorText != null) {
				// Only compile errors visible (Execution in Thread)

				paneOutput.appendText(errorText);
				applyShowOutput(true, true);
			}

			// Output
			Output output = inst.getOutput();
			if (output.hasHtml()) {
				applyShowOutputHtml(true);

				paneOutputHtml.loadContent(output.getHtml());
			}

			onExecutionEnded();
		});
	}

	private int getIntervalSeconds() {
		String intervalStr = choiceSeconds.getSelectionModel().getSelectedItem();

		int interval = RepeatUtils.parseInterval(intervalStr);
		return interval;
	}

	public void performSave() {
		if (paneCodeOnly == null) {
			return;
		}

		File file = script.getFile();
		String text = paneCodeOnly.getText();

		try {
			FileUtils.writeToFile(file, text);

			updateLastSaveTimeFromFile();

			buttonSaveCode.setDisable(true);

			CodeUnsavedPanes.remove(this);
			ControllerPages.updateEditings();

			ControllerFileSave.onFileSaved(file);

			afterSaved();
		} catch (Exception e) {
			Log.error("Failed to write file", e);
		}
	}

	private void afterSaved() {
		updateTitle();

		updateInputs(false);
	}

	@Override
	public boolean hasInstId(int id) {
		return inst.getInstId() == id;
	}

	@Override
	public void tick() {
		secondsCounter++;

		int interval = getIntervalSeconds();
		if (interval == -1) {
			// Invalid

			return;
		}

		if (secondsCounter >= interval) {
			triggerRunScript();

			secondsCounter = 0;
		}
	}

	@Override
	public boolean requestClose() {
		// TODO: Not when script is executed
		return true;
	}

	@Override
	public boolean hasRunningScript() {
		if (scriptRunning == null) {
			return false;
		}

		return scriptRunning.isRunning();
	}

	@Override
	public boolean equalsExecution(PageTypeEnum pageType, Script scriptOther) {
		if (pageType != getPageType()) {
			return false;
		}

		return scriptOther.equals(script);
	}

	public boolean equalsScript(Script scriptOther) {
		return scriptOther.equals(script);
	}

	@Override
	public PageTypeEnum getPageType() {
		return PageTypeEnum.ScriptDetails;
	}

	public Script getScript() {
		return script;
	}

	@Override
	public String getContentTitle() {
		return script.getName();
	}

	@Override
	public long getStartTime() {
		if (scriptRunning == null) {
			return 0;
		}

		ScriptInstThread thread = scriptRunning.getThread();
		if (thread.isAlive()) {
			return thread.getStartTime();
		}

		return 0;
	}

	private void applyRunHistory() {
		List<Date> last5 = Lists.last(executionTimes, 5);

		StringBuilder sb = new StringBuilder();
		for(Date date : last5) {
			if (sb.length() > 0) {
				sb.append(", ");
			}

			String formatted = DateUtils.formatTimeOnly(date);
			sb.append(formatted);
		}

		runInformation.setText(sb.toString());
	}

	public long getLastEditedTime() {
		return lastEditedTime;
	}

	public void applyValues(ScriptRecent scriptRecent) {
		if (scriptRecent == null) {
			return;
		}

		updateInputs(false);

		paneInputs.applyRecent(scriptRecent);
	}

	private boolean checkInputs() {
		int i=0;
		for(InputConfig inputConfig : inputsConfig) {
			boolean mandatory = inputConfig.isMandatory();

			PaneInput paneAt = paneInputs.getPaneAt(i);
			if (mandatory && paneAt.getValue() == null) {
				paneAt.focusField();
				return false;
			}

			i++;
		}

		return true;
	}

	public void checkSavedOutside() {
		if (lastSaveTime == -1) {
			// Noch nicht aktiv
			return;
		}

		if (paneCodeOnly == null) {
			return;
		}

		long fileChanged = script.getFile().lastModified();

		//Log.tmp("Compare: file=" + fileChanged + ", last save=" + lastSaveTime);

		if (fileChanged > lastSaveTime) {
			// Changed

			setFileHasChangedOutside();
		}
	}

	public void insertTextAtCursor(String text) {
		paneCodeOnly.insertAtCursor(text);
	}

	public List<InputConfig> getInputsConfig() {
		return inputsConfig;
	}

	private void onIncludesChanges() {
		labelIncludes.setText(null);
		FxTooltip.setTooltip(labelIncludes, null);

		FileMetadata fileMetadata = FileMetadataFromRegistryManager.get(getScriptFullPath());
		if (fileMetadata == null) {
			return;
		}

		if (!fileMetadata.getIncludes().isEmpty()) {
			String text = Ln.colonSpace(K.ScriptsIncluded) + fileMetadata.getIncludes().size();
			labelIncludes.setText(text);

			String tooltip = Ln.colon(K.ScriptsIncluded) + "\n" + StringUtils.joinWith(fileMetadata.getIncludes(), "\n");
			FxTooltip.setTooltip(labelIncludes, tooltip);
		}
	}

	private void setFileHasChangedOutside() {
		FxControlUtils.setVisible(boxFileChanged, true);
	}

	private void updateToggleButtons() {
		boolean codeShown = isCodeShowing();
		boolean inputShown = isInputShowing();
		boolean outputShown = isOutputShowing();

		if (!codeShown) {
			buttonToggleShowCode.setSelected(false);
		}
		if (!inputShown) {
			buttonToggleEdit.setSelected(false);
			buttonToggleShowInput.setSelected(false);

			onEditingChanged();
		}
		if (!outputShown) {
			buttonToggleShowOutput.setSelected(false);
		}
	}

	private ScriptFile createScriptFile() {
		File filepath = script.getFile();
		return new ScriptFile(filepath);
	}

	private boolean isCodeShowing() {
		return dockNodeCode != null && dockNodeCode.isDockDocked();
	}

	private boolean isInputShowing() {
		return dockNodeInput != null && dockNodeInput.isDockDocked();
	}

	private boolean isOutputShowing() {
		return dockNodeOutput != null && dockNodeOutput.isDockDocked();
	}

	public String getScriptFullPath() {
		String path = script.getFile().getAbsolutePath();
		return path;
	}
}
