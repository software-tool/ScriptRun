package script.frame.scripts;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import script.controller.ControllerScripts;
import script.data.Script;
import script.frame.css.CssConstants;
import script.lang.K;
import script.metadata.file.FileMetadata;

import java.io.File;

/**
 * Pane for file
 */
public class PaneScriptPathFile {

	private GridPane parent;

	private Pane paneTitle = new Pane();
	private Label title = new Label();

	private Button buttonRun = new Button();
	private Button buttonEdit = new Button();

	private File file;
	private String relativePath;
	private int y;
	private FileMetadata metadata;

	public PaneScriptPathFile(GridPane parent, File file, String relativePath, int y, FileMetadata metadata) {
		this.parent = parent;
		this.file = file;
		this.relativePath = relativePath;
		this.y = y;
		this.metadata = metadata;

		init();

		initContent();
	}

	private void init() {
		buttonRun.getStyleClass().add("sidebar_button_run");
		buttonEdit.getStyleClass().add("sidebar_button_edit");

		FxIconUtils.setIconNoStyleWithSize(buttonRun, "control_play_blue.png");
		FxIconUtils.setIconNoStyleWithSize(buttonEdit, "text_prose.png");

		FxTooltip.setTooltip(buttonRun, Ln.get(K.RunNow));
		FxTooltip.setTooltip(buttonEdit, Ln.get(K.ScriptShowCode));

		paneTitle.setOnMouseClicked(e -> {
			Script script = new Script(file.getParentFile(), file.getName());

			if (!script.equals(ControllerScripts.getSelected())) {
				ControllerScripts.openDetails(script, false, false);
			}

			e.consume();
		});
		paneTitle.hoverProperty().addListener((observer, oldValue, newValue) -> onPaneHover(observer, newValue));

		buttonRun.setOnMouseClicked(e -> {
			Script script = new Script(file.getParentFile(), file.getName());

			if (!script.equals(ControllerScripts.getSelected())) {
				ControllerScripts.openDetails(script, false, false);
			}
			ControllerScripts.doRunSelected(true);

			e.consume();
		});

		buttonEdit.setOnMouseClicked(e -> {
			Script script = new Script(file.getParentFile(), file.getName());

			if (!script.equals(ControllerScripts.getSelected())) {
				ControllerScripts.openDetails(script, false, true);
			}
			ControllerScripts.doEditSelected();

			e.consume();
		});
	}

	/**
	 * Hover on pane
	 *
	 * (Cannot modify classes of multiple controls, will result in uncontrolled redrawing)
	 */
	private void onPaneHover(ObservableValue<? extends Boolean> observer, boolean hover) {
		//System.out.println("Hover: " + hover);

		if (hover) {
			paneTitle.getStyleClass().add("sidebar_button_title_hover");
		} else {
			paneTitle.getStyleClass().remove("sidebar_button_title_hover");
		}
	}

	public File getFile() {
		return file;
	}

	public void setVisible(boolean visible) {
		paneTitle.setManaged(visible);
		paneTitle.setVisible(visible);

		buttonRun.setManaged(visible);
		buttonRun.setVisible(visible);

		buttonEdit.setManaged(visible);
		buttonEdit.setVisible(visible);
	}

	private void initContent() {
		String scriptTitle = null;
		String filename = file.getName();

		if (metadata != null) {
			scriptTitle = metadata.getTitle();
		}

		title.getStyleClass().add(CssConstants.SCRIPT_FILE_TITLE);

		if (scriptTitle != null) {
			title.setText(scriptTitle);
		} else {
			title.setText((relativePath != null ? relativePath + "/" : "") + filename);
		}

		paneTitle.getStyleClass().add(CssConstants.SCRIPT_FILE_TITLE_WRAPPER);
		paneTitle.getChildren().add(title);
		FxLayoutUtils.setFillWidth(paneTitle);

		parent.add(paneTitle, 0, y);
		parent.add(buttonRun, 1, y);
		parent.add(buttonEdit, 2, y);
	}
}
