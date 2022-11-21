package script.frame;

import java.awt.Desktop;

import com.anchorage.docks.node.DockNode;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import script.Scripting;
import script.data.Script;
import script.data.ScriptDirectory;
import script.frame.execute.IExecutePane;
import script.frame.menu.ScriptingMenubar;
import script.frame.pages.PageTypeEnum;
import script.recent.ScriptRecent;

public class PaneMain {

	public static PaneMain inst = null;

	private BorderPane paneMain = new BorderPane();
	private PaneMainContent content = null;

	public void setCurrentDirectory(ScriptDirectory path, boolean addScriptOnOpen) {
		content.openDirectory(path, addScriptOnOpen);
	}

	public void doRunSelected(boolean later) {
		content.doRunSelected(later);
	}

	public void doEditSelected() {
		content.doEditSelected();
	}

	public void openDetails(Script script, boolean inNewTab, boolean openCodeEditor, ScriptRecent scriptRecent, boolean acceptExisting) {
		content.openDetails(script, inNewTab, openCodeEditor, scriptRecent, acceptExisting);
	}

	public void applyContent(IExecutePane executePane) {
		content.applyContent(executePane);
	}

	public void doSaveSelected() {
		content.doSaveSelected();
	}

	public void openWelcome() {
		content.openWelcome();
	}

	public void closeCurrentPage() {
		content.closeCurrentPage();
	}

	public void openOptions() {
		content.openOptions();
	}

	public void openRecents() {
		content.openRecents();
	}

	public void updateScriptDirectories() {
		content.updateScriptDirectories();
	}

	public void updateExecutions() {
		Platform.runLater(() -> {
			content.updateExecutions();
		});
	}

	public void updateEditings() {
		Platform.runLater(() -> {
			content.updateEditings();
		});
	}

	public void reportOutput(int id, String text) {
		content.reportOutput(id, text);
	}

	public void reportScriptFinished(int id) {
		content.reportScriptFinished(id);
	}

	public BorderPane createContent() {
		content = new PaneMainContent();

		paneMain.setPadding(new Insets(0));
		paneMain.setTop(createMenu());
		paneMain.setCenter(content.getContent());

		// Store
		inst = this;

		return paneMain;
	}

	public void onShutdown() {
		content.onShutdown();
	}

	public void afterStartup() {
		content.restoreDividerPosition();
	}

	public DockNode getDockNodeSelected() {
		return content.getDockNodeSelected();
	}

	public Script getSelected() {
		return content.getSelected();
	}

	public PageTypeEnum getDockNodeTypeSelected() {
		return content.getDockNodeTypeSelected();
	}

	protected MenuBar createMenu() {
		ScriptingMenubar menubar = new ScriptingMenubar();

		// AppWindow.editorMenubar = menubar;

		if (Scripting.useSwingMenu()) {
			Desktop.getDesktop().setDefaultMenuBar(menubar.getMenuBarSwing());
		}

		MenuBar bar = menubar.getMenuBar();
		bar.setPadding(new Insets(0));

		return bar;
	}
}
