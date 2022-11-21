package script;

import com.anchorage.docks.containers.common.AnchorageSettings;
import com.anchorage.docks.node.ui.DockUIPanel;
import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.LangHelper;
import com.rwu.application.lang.LangHelper.Language;
import com.rwu.application.lang.LanguageCommon;
import com.rwu.application.lang.Ln;
import com.rwu.application.lang.generic.ILangProvider;
import com.rwu.application.lang.generic.LCommon;
import com.rwu.fx.alerts.Alerts;
import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxWindowUtils;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.log.Log;
import com.rwu.misc.FileGetUtils;
import com.rwu.misc.ListUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import script.app.App;
import script.config.ConfigConstants;
import script.config.PrepareConfig;
import script.controller.ControllerOutput;
import script.controller.ControllerPath;
import script.data.ScriptDirectory;
import script.execute.ScriptMonitorThread;
import script.frame.PaneMain;
import script.frame.details.PaneScriptDetails;
import script.frame.drop.FrameDrop;
import script.frame.execute.IExecutePane;
import script.frame.img.GuiImages;
import script.frame.prepare.PrepareCss;
import script.frame.state.CodeUnsavedPanes;
import script.frame.state.ExecutionPanes;
import script.lang.K;
import script.log.LogScriptRun;
import script.manager.DirectoryConfigManager;
import script.metadata.directory.DirectoryMetadataManager;
import script.metadata.file.FileMetadataFromRegistryManager;
import script.recent.RecentFileManager;
import script.recent.ScriptRecentManager;
import script.repeat.RepeatThread;
import script.state.SelectionState;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ScriptingRun extends Application {

	private static String[] args = null;

	private static final double DEFAULT_WIDTH = 750;
	private static final double DEFAULT_HEIGHT = 550;

	private static final String WINDOW_KEY = "main_window_size";

	@Override
	public void start(Stage primaryStage) {
		try {
			Log.initLog(new LogScriptRun("\n", App.getLogFile()));

			// Configuration
			PrepareConfig.prepare();

			// Language
			initLang();

			// Icon
			Image icon = new Image(GuiImages.class.getResourceAsStream("appicon.png"));
			primaryStage.getIcons().add(icon);
			AnchorageSettings.DOCK_ZONES_ICON = icon;
			DialogBase.APP_ICON_IMAGE = icon;

			// Init
			initApplication();

			// initCloseListeners(primaryStage);

			// Store stage
			AppWindowFx.setPrimaryStage(primaryStage);

			// Window
			PaneMain window = new PaneMain();
			Scene scene = new Scene(window.createContent(), DEFAULT_WIDTH, DEFAULT_HEIGHT);

			// Focus Listener
			// initFocusedListener(primaryStage);

			initCloseListeners(AppWindowFx.getStage());

			// Css

			PrepareCss.applyCss(scene);

			// Scene
			initScene(scene);

			// No title
			DockUIPanel.SHOW_NEW_BAR_PANELS = false;

			primaryStage.setTitle(Ln.get(K.AppTitle));

			// Window size
			FxWindowUtils.restoreSize(primaryStage, AppConfig.getValue(ConfigConstants.BASIC, WINDOW_KEY), true, DEFAULT_WIDTH, DEFAULT_HEIGHT);

			// Show
			primaryStage.setScene(scene);
			primaryStage.show();

			// Startup
			onStartup();

			// After Start
			postStartup();
		} catch (Exception e) {
			Log.error("Failed to start", e);
		}
	}

	private void initLang() {
		LanguageCommon.initLanguageFromInstallation();

		// Configuration
		String languageInConfig = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.USER_SELECTED_LANGUAGE);
		if (languageInConfig != null) {
			Language langSelection = LangHelper.getLangSelection(languageInConfig);
			if (langSelection != null) {
				LangHelper.language = langSelection;
			}
		}

		applyLang();

		LCommon.provider = new ILangProvider() {
			@Override
			public String get(String key) {
				return Ln.get(key);
			}

			@Override
			public String get(String key, String parameter1) {
				return Ln.get(key, parameter1);
			}

			@Override
			public String get(String key, String parameter1, String parameter2) {
				return Ln.get(key, parameter1, parameter2);
			}
		};
	}

	private void initApplication() {
		// Tooltips
		FxTooltip.init();

		FxIconUtils.initImagesClass(GuiImages.class);

		// Paths
		DirectoryConfigManager.read();

		// Recents
		ScriptRecentManager.read();

		// Customs
		DirectoryMetadataManager.read();
		FileMetadataFromRegistryManager.read();

		// Read Recent
		RecentFileManager.readList();

		ControllerOutput.init();

		// Monitor Thread
		new ScriptMonitorThread().start();
		new RepeatThread().start();
	}

	private void initScene(Scene scene) {
		// Drag/Drop
		FrameDrop.initFileDrop(scene);
	}

	private void onStartup() {
		//ControllerPages.openInfo();
	}

	private void postStartup() {
		setInitialGroup();

		setInitialDirectory();

		PaneMain.inst.afterStartup();
	}

	private void setInitialGroup() {
		// From configuration
		String mostRecentGroup = AppConfig.getValue(ConfigConstants.RECENT, ConfigConstants.RECENT_GROUP);
		if (mostRecentGroup != null) {
			SelectionState.currentDirectoryGroup = mostRecentGroup;
		}
	}

	private void setInitialDirectory() {
		ScriptDirectory initialDirectory = null;

		// From configuration
		String mostRecentPath = AppConfig.getValue(ConfigConstants.RECENT, ConfigConstants.RECENT_DIRECTORY);
		if (mostRecentPath != null) {
			File file = FileGetUtils.getFileIfPossible(mostRecentPath);
			if (file != null) {
				initialDirectory = new ScriptDirectory(file);
			}
		}

		// 1. Path
		if (initialDirectory == null) {
			List<ScriptDirectory> scriptPathes = DirectoryConfigManager.getScriptPathes();
			initialDirectory = ListUtils.getFirst(scriptPathes);
		}

		// Initial directory
		if (initialDirectory != null) {
			ControllerPath.setCurrentDirectory(initialDirectory, false);
		}
	}

	/**
	 * Requests the close of the window
	 */
	public static boolean closeApplication(boolean force) {
		ExecutionPanes.cleanupExecutions();
		boolean hasExecutions = ExecutionPanes.hasPanesExecuting();
		List<PaneScriptDetails> editing = CodeUnsavedPanes.getScriptsInEditing();

		boolean canClose = false;

		if (hasExecutions) {
			ButtonType buttonType = Alerts.showConfirmYesNoCancel(Ln.get(K.ConfirmTerminateExecutionsMessage), null, Ln.get(K.ConfirmTerminateExecutionsTitle));
			if (buttonType == ButtonType.YES) {
				for(IExecutePane executePane : ExecutionPanes.getPanesExecuting()) {
					executePane.triggerStopScript();
				}

				canClose = true;
			} else {
			}
		} else {
			canClose = true;
		}

		if (!canClose) {
			return canClose;
		}

		if (!editing.isEmpty()) {
			ButtonType buttonType = Alerts.showConfirmYesNoCancel(Ln.get(K.ConfirmDiscardUnsaveChangesMessage), null, Ln.get(K.ConfirmDiscardUnsaveChangesTitle));
			if (buttonType == ButtonType.YES) {
				canClose = true;
			} else {
				canClose = false;
			}
		}

		if (!canClose) {
			return canClose;
		}

		ScriptRecentManager.store();

		Stage stageMain = AppWindowFx.getStage();

		// Window position
		String storeStr = FxWindowUtils.getSizeStored(stageMain);
		AppConfig.setValue(ConfigConstants.BASIC, WINDOW_KEY, storeStr);

		PaneMain.inst.onShutdown();

		// Tooltips
		FxTooltip.stopThread();

		// Threads
		RepeatThread.inst.shutdown();
		ScriptMonitorThread.inst.shutdown();

		// Close window
		stageMain.close();

		return true;
	}

	private void initCloseListeners(final Stage stage) {
		stage.setOnCloseRequest((WindowEvent we) -> {
			try {
				boolean ok = closeApplication(Scripting.useSwingMenu());
				if (ok) {
					System.exit(0);
				} else {
					// Do not close
					we.consume();
				}
			} catch (Throwable t) {
				Log.error("Error during shutdown of application", t);
			}
		});
	}

	public static void applyLang() {
		try {
			String path = Ln.getLangFile("/script/lang");

			InputStream is = Scripting.class.getResourceAsStream(path);
			if (is == null) {
				Alerts.showError("File does not exist: " + path, "Cannot Start");

				Log.error("File does not exist: " + path, new Exception());
				return;
			}

			Ln.readFromInputStream(is);
		} catch (Exception ex) {
			Log.error(ex);
		}
	}

	public static void main(String[] _args) {
		args = _args;

		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
