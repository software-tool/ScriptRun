package script.frame;

import com.anchorage.docks.containers.StageFloatable;
import com.anchorage.docks.containers.common.DockCommons;
import com.anchorage.docks.containers.interfaces.DockContainer;
import com.anchorage.docks.containers.subcontainers.DockSplitterContainer;
import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.misc.DockUtilsGet;
import com.anchorage.docks.misc.DockUtilsTabs;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.DockNode.DockPosition;
import com.anchorage.docks.node.interfaces.DockNodeCloseRequestHandler;
import com.anchorage.docks.node.interfaces.IDockNodeListener;
import com.anchorage.docks.node.interfaces.IDockStationListener;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import com.anchorage.system.interfaces.IDockGlobalListener;
import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.Ln;
import com.rwu.misc.IntUtils;
import com.rwu.misc.ListUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import script.config.ConfigConstants;
import script.controller.ControllerPages;
import script.data.Script;
import script.data.ScriptDirectory;
import script.frame.details.PaneScriptDetails;
import script.frame.directory.PaneDirectory;
import script.frame.directory.PaneGroupsSidebar;
import script.frame.docking.DockNodeFind;
import script.frame.execute.IExecutePane;
import script.frame.execute.PaneGlobalOutput;
import script.frame.info.PaneWelcome;
import script.frame.intf.IContent;
import script.frame.menu.ScriptingMenubar;
import script.frame.options.PaneOptions;
import script.frame.pages.PageTypeEnum;
import script.frame.recent.PaneScriptRecentsAll;
import script.frame.state.ExecutionPanes;
import script.frame.state.OpenRequestDirectory;
import script.frame.state.OpenRequestScript;
import script.frame.state.OpenedPanes;
import script.lang.K;
import script.recent.ScriptRecent;
import script.state.SelectionState;
import script.util.Two;

import java.util.HashMap;
import java.util.Map;

/**
 * Content of the application
 */
public class PaneMainContent {

	private DockStation stationCenter = AnchorageSystem.createStation();
	private DockStation stationLeft = AnchorageSystem.createStation();

	// Splitter
	private DockSplitterContainer splitterHorz;
	private BorderPane borderPaneMain = new BorderPane();

	// Directories
	private PaneGroupsSidebar directories;

	private Map<PageTypeEnum, DockNode> customTabs = new HashMap<>();

	private Map<DockNode, PaneDirectory> directoryPanes = new HashMap<>();

	// Details
	private Map<DockNode, PaneScriptDetails> detailPanes = new HashMap<>();

	private Map<DockNode, PageTypeEnum> pageTypes = new HashMap<>();

	private ChangeListener<Tab> tabListener = null;

	private IDockNodeListener dockNodeListener = null;

	private Label infotext = new Label();

	private PaneGlobalOutput paneGlobalOutput;
	private DockNode dockNodeGlobalOutput;

	public PaneMainContent() {
		init();
		initListeners();

		updateScriptDirectories();

		stationCenter.addListener(new IDockStationListener() {
			@Override
			public void putDockFinished(DockNode dockNode) {
				Platform.runLater(() -> {
					ScriptingMenubar.inst.updateMenues();
				});

				applyTabListener();
			}

			@Override
			public void dockFinished(DockNode dockNode) {
				Platform.runLater(() -> {
					ScriptingMenubar.inst.updateMenues();
				});

				applyTabListener();
			}
		});

		AnchorageSystem.addGlobalListener(new IDockGlobalListener() {
			@Override
			public void floatableFocusGained(StageFloatable floatable) {
			}

			@Override
			public void closingDockNodeFinished(DockNode node, StageFloatable stageBefore, boolean floatingBefore) {
				//System.err.println("Close Global: " + node);
			}
		});
	}

	/**
	 * Open page for directory
	 */
	public void openDirectory(ScriptDirectory path, boolean addScriptOnOpen) {
		OpenRequestDirectory openRequest = new OpenRequestDirectory(path);
		PaneDirectory mostRecent = openRequest.getMostRecent();

		PaneDirectory directory;
		if (mostRecent != null) {
			directory = mostRecent;
		} else {
			directory = new PaneDirectory();
		}

		directory.fill(path);

		// Store
		SelectionState.userSelectedDirectory(path.getDirectory());

		String title = path.getName();

		DockNode dockNode;
		boolean tabs = openInTabs();
		if (tabs) {
			dockNode = prepareCustomTab(PageTypeEnum.ScriptDirectory, title, directory);
		} else {
			dockNode = applyToContentMain(PageTypeEnum.ScriptDirectory, title, directory);
		}

		// Store result
		directoryPanes.put(dockNode, directory);

		// Update directories
		ControllerPages.updateScriptDirectories();

		// Update
		onCurrentContentChanged();

		if (addScriptOnOpen) {
			directory.applyAddScript(true);
		}
	}

	/**
	 * Open details for script
	 */
	public void openDetails(Script script, boolean inNewTab, boolean openCodeEditor, ScriptRecent scriptRecent, boolean acceptExisting) {
		OpenRequestScript openRequest = new OpenRequestScript(script, PageTypeEnum.ScriptDetails);
		Two<PaneScriptDetails, IExecutePane> mostRecent = openRequest.getMostRecentScript();

		PaneScriptDetails paneDetails = null;
		if (acceptExisting && mostRecent.hasContent()) {
			// Script is edited/executed

			if (mostRecent.hasFirst()) {
				paneDetails = mostRecent.getFirst();
			}
			if (mostRecent.hasSecond()) {
				paneDetails = (PaneScriptDetails) mostRecent.getSecond();
			}
		} else {
			// Add new pane

			paneDetails = new PaneScriptDetails(script);

			// Values
			paneDetails.applyValues(scriptRecent);
		}

		String title = script.getName();

		DockNode dockNode;

		if (inNewTab) {
			dockNode = AnchorageSystem.createDock(title, paneDetails.getContent());

			dockNode.dock(stationCenter, DockPosition.CENTER);
		} else {
			// Main

			dockNode = applyToContentMain(PageTypeEnum.ScriptDetails, title, paneDetails);
		}

		// Store result
		detailPanes.put(dockNode, paneDetails);

		// Opened
		OpenedPanes.add(paneDetails);

		// Listener
		dockNode.setDockNodeListener(dockNodeListener);

		PaneScriptDetails paneDetailsFinal = paneDetails;
		dockNode.setCloseRequestHandler(new DockNodeCloseRequestHandler() {
			@Override
			public boolean canClose() {
				return paneDetailsFinal.requestClose();
			}
		});

		// Update
		onCurrentContentChanged();

		if (openCodeEditor) {
			paneDetails.triggerShowEditor();
		}
	}

	/**
	 * Welcome
	 */
	public void openWelcome() {
		PaneWelcome info = new PaneWelcome();

		String title = Ln.get(K.PageWelcome);

		boolean tabs = openInTabs();
		if (tabs) {
			prepareCustomTab(PageTypeEnum.Welcome, title, info);
		} else {
			applyToContentMain(PageTypeEnum.Welcome, title, info);
		}

		// Update
		onCurrentContentChanged();
	}

	/**
	 * Options
	 */
	public void openOptions() {
		PaneOptions options = new PaneOptions();

		String title = Ln.get(K.PageOptions);

		boolean tabs = openInTabs();
		if (tabs) {
			prepareCustomTab(PageTypeEnum.Options, title, options);
		} else {
			applyToContentMain(PageTypeEnum.Options, title, options);
		}

		// Update
		onCurrentContentChanged();
	}

	/**
	 * List of recents
	 */
	public void openRecents() {
		PaneScriptRecentsAll recentsAll = new PaneScriptRecentsAll();

		String title = Ln.get(K.PageScriptRecents);

		boolean tabs = openInTabs();
		if (tabs) {
			prepareCustomTab(PageTypeEnum.ScriptRecents, title, recentsAll);
		} else {
			applyToContentMain(PageTypeEnum.ScriptRecents, title, recentsAll);
		}

		// Update
		onCurrentContentChanged();
	}

	public void applyContent(IExecutePane executePane) {
		String title = null;
		if (executePane.getScript() != null) {
			title = executePane.getScript().getName();
		}
		if (title == null) {
			title = executePane.getContentTitle();
		}

		PageTypeEnum pageType = executePane.getPageType();

		DockNode dockNode = applyToContentMain(pageType, title, executePane);

		// Type
		pageTypes.put(dockNode, pageType);

		// Store result
		if (pageType.isScriptResult()) {
			detailPanes.put(dockNode, (PaneScriptDetails)executePane);
		}

		// Listener
		dockNode.setDockNodeListener(dockNodeListener);

		// Update
		onCurrentContentChanged();
	}

	/**
	 * Run current script
	 */
	public void doRunSelected(boolean later) {
		DockNode selected = getDockNodeSelected();

		PaneScriptDetails result = detailPanes.get(selected);
		if (result != null) {
			if (later) {
				Platform.runLater(() -> {
					result.triggerRunScript();
				});
			} else {
				result.triggerRunScript();
			}
		}
	}

	public void doEditSelected() {
		DockNode selected = getDockNodeSelected();

		//		PaneCode code = codePanes.get(selected);
		//		if (code != null) {
		//			code.performRun();
		//		}

		PaneScriptDetails result = detailPanes.get(selected);
		if (result != null) {
			result.triggerShowEditor();
		}
	}

	/**
	 * Save
	 */
	public void doSaveSelected() {
		DockNode selected = getDockNodeSelected();

		PaneScriptDetails details = detailPanes.get(selected);
		if (details != null) {
			details.performSave();
		}
	}

	/**
	 * Close current page
	 */
	public void closeCurrentPage() {
		DockTabberContainer tabber = DockUtilsGet.getTabberRecursive(stationCenter);
		if (tabber == null) {
			ObservableList<Node> children = stationCenter.getChildren();

			if (!children.isEmpty()) {
				Node child = children.get(0);

				if (child instanceof DockNode) {
					((DockNode) child).undock();
				}
			}

			return;
		}

		Tab tabSelected = tabber.getSelectionModel().getSelectedItem();
		Node current = tabSelected.getContent();

		if (current instanceof DockNode) {
			((DockNode) current).undock();
		}
	}

	/**
	 * Main page is set
	 *
	 * - Content is replaced if already exists
	 * - With new title
	 */
	private DockNode applyToContentMain(PageTypeEnum pageType, String title, IContent content) {
		DockNode dockNode = customTabs.get(PageTypeEnum.Main);

		if (dockNode == null) {
			dockNode = AnchorageSystem.createDock("Main content", content.getContent());

			// Store
			customTabs.put(PageTypeEnum.Main, dockNode);
		} else {
			dockNode.getContent().setNodeContent(content.getContent());

			// Title
			DockUtilsTabs.setTabsTitle(stationCenter, dockNode, title, null);
		}

		// Close dock before
		DockNode previousDockNode = getSelectedDockNode();
		if (previousDockNode != null) {
			previousDockNode.undock();
		}

		dockNode.dock(stationCenter, DockPosition.CENTER);

		// Type
		pageTypes.put(dockNode, pageType);

		return dockNode;
	}

	private DockNode prepareCustomTab(PageTypeEnum pageType, String title, IContent content) {
		DockNode dockNode = customTabs.get(pageType);

		if (dockNode == null) {
			Node node = content.getContent();
			dockNode = AnchorageSystem.createDock(title, node);

			// Store
			customTabs.put(pageType, dockNode);
		}

		dockNode.dock(stationCenter, DockPosition.CENTER);

		return dockNode;
	}

	private boolean openInTabs() {
		return AppConfig.getBooleanValue(ConfigConstants.BASIC, ConfigConstants.OPEN_CONTENT_IN_TABS, false);
	}

	public void reportOutput(int id, String text) {
		boolean destinationFound = false;

		//Log.tmp("Output: " + id + ", " + text);

		if (id != -1) {
			// Find with id

			IExecutePane pane = getResultPaneRunning(id);
			if (pane != null) {
				pane.addOutput(text);

				destinationFound = true;
			}
		}

		if (!destinationFound) {
			Platform.runLater(() -> {
				initPaneGlobalOutput();

				paneGlobalOutput.addOutput(text);
			});
		}
	}

	private void initPaneGlobalOutput() {
		if (dockNodeGlobalOutput == null) {
			paneGlobalOutput = new PaneGlobalOutput();

			dockNodeGlobalOutput = prepareCustomTab(PageTypeEnum.GlobalOutput, Ln.get(K.Output), paneGlobalOutput);
		}

		// After close: Open again
		DockContainer parentContainer = dockNodeGlobalOutput.getParentContainer();
		if (parentContainer == null) {
			dockNodeGlobalOutput = prepareCustomTab(PageTypeEnum.GlobalOutput, Ln.get(K.Output), paneGlobalOutput);
		}
	}

	public void reportScriptFinished(int id) {
		IExecutePane pane = getResultPaneRunning(id);
		if (pane != null) {
			// Details

			pane.applyScriptFinished(id);
		}

		ExecutionPanes.cleanupExecutions();

		Platform.runLater(() -> {
			ControllerPages.updateExecutions();
		});
	}

	private IExecutePane getResultPaneRunning(int id) {
		for (IExecutePane result : ExecutionPanes.getPanesExecuting()) {
			if (result.hasInstId(id)) {
				return result;
			}
		}

		return null;
	}

	private void init() {
		directories = new PaneGroupsSidebar();
	}

	public void updateScriptDirectories() {
		directories.fill();
		//directoriesBar.fill();
	}

	public void updateExecutions() {
		directories.fillExecutions();
	}

	public void updateEditings() {
		directories.fillEditings();
	}

	public void onShutdown() {
		ObservableList<SplitPane.Divider> dividers = splitterHorz.getDividers();
		SplitPane.Divider divider = ListUtils.getFirst(dividers);
		if (divider != null) {
			int value = (int) (divider.getPosition() * 100.0);

			AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.MAIN_DIVIDER_POSITION, value + "");
		}
	}

	private void onCurrentContentChanged() {
		ScriptingMenubar.inst.updateMenues();
	}

	public Node getContent() {
		//		nodeScriptDirectories = AnchorageSystem.createDock(L.get(K.ContentScriptDirectories), directories.getContent());
		//		nodeScriptDirectories.getContent().hideBarPanel();
		//		nodeScriptDirectories.dock(stationLeft, DockPosition.CENTER);

		Node directoryContent = directories.getContent();

		stationLeft.getChildren().add(directoryContent);

		splitterHorz = DockCommons.createSplitter(stationLeft, stationCenter, DockNode.DockPosition.RIGHT, -1);

		//DockNode dockNodeMain = AnchorageSystem.createDock("Content", splitterHorz);
		//dockNodeMain.dock(stationMain, DockNode.DockPosition.CENTER);

		//borderPaneMain.setPadding(new Insets(0));
		borderPaneMain.setBottom(infotext);
		borderPaneMain.setCenter(splitterHorz);

		//		nodeScriptDirectoriesBar = AnchorageSystem.createDock(L.get(K.ContentScriptDirectories), directoriesBar.getContent());
		//		nodeScriptDirectoriesBar.getContent().hideBarPanel();
		//		nodeScriptDirectoriesBar.dock(stationTop, DockPosition.CENTER);

		// Tools, Content
		//splitterHorz = DockCommons.createSplitter(stationLeft, stationCenter, DockNode.DockPosition.RIGHT, -1);
		//splitterHorz = DockCommons.createSplitter(stationTop, stationCenter, DockNode.DockPosition.BOTTOM, -1);

		return borderPaneMain;
	}

	public void restoreDividerPosition() {
		double dividedPos = 0.15;

		String dividerStr = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.MAIN_DIVIDER_POSITION);
		Integer parsed = IntUtils.parse(dividerStr, -1);
		if (parsed != -1) {
			double dividedCalculated = (parsed.doubleValue() / 100.0);
			if (dividedCalculated > 0.05 && dividedCalculated < 0.8) {
				dividedPos = dividedCalculated;
			}
		}

		splitterHorz.setDividerPositions(dividedPos);
	}

	private void initListeners() {
		stationCenter.addListener(new IDockStationListener() {
			@Override
			public void putDockFinished(DockNode nodeSource) {
			}

			@Override
			public void dockFinished(DockNode nodeSource) {
				// Apply css
				boolean floating = nodeSource.floatingProperty().get();
				if (floating) {
					//StageFloatable floatableStage = nodeSource.getFloatableStage();
					//MessageBase.applyMainWindowIcon(floatableStage);

					nodeSource.getContent().showBarPanel();
				} else {
					nodeSource.getContent().hideBarPanel();
				}
			}
		});

		tabListener = new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
				Platform.runLater(() -> {
					ScriptingMenubar.inst.updateMenues();
				});
			}
		};

		dockNodeListener = new IDockNodeListener() {
			@Override
			public void dockNodeUndocked(DockNode dockNode) {
				//Log.tmp("Undock: " + dockNode);

				PaneScriptDetails paneResult = detailPanes.get(dockNode);
				if (paneResult != null) {
					detailPanes.remove(dockNode);

					pageTypes.remove(dockNode);
				}
			}

			@Override
			public void dockNodeBeforeCloseStage(StageFloatable stageFloatable) {
			}

			@Override
			public void afterDockNodeClosed(StageFloatable stageFloatable) {
				//Log.tmp("afterDockNodeClosed: " + stageFloatable);
			}
		};
	}

	private void applyTabListener() {
		DockTabberContainer tabber = DockUtilsGet.getTabberRecursive(stationCenter);
		if (tabber == null) {
			return;
		}

		tabber.getSelectionModel().selectedItemProperty().removeListener(tabListener);
		tabber.getSelectionModel().selectedItemProperty().addListener(tabListener);
	}

	public DockNode getDockNodeSelected() {
		DockNode dockNode = DockNodeFind.getCurrentDockNode(stationCenter);

		return dockNode;
	}

	public Script getSelected() {
		DockNode dockNode = getSelectedDockNode();

		PaneScriptDetails paneScriptDetails = detailPanes.get(dockNode);
		if (paneScriptDetails == null) {
			return null;
		}

		return paneScriptDetails.getScript();
	}

	private DockNode getSelectedDockNode() {
		DockNode dockNode = DockNodeFind.getCurrentDockNode(stationCenter);
		return dockNode;
	}

	public PageTypeEnum getDockNodeTypeSelected() {
		DockNode dockNode = DockNodeFind.getCurrentDockNode(stationCenter);
		return pageTypes.get(dockNode);
	}
}
