package script.frame.recent;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import script.recent.RecentFileManager;

public class PaneRecent {

	private HBox pane = new HBox();

	private File file;

	private Label labelPath = new Label();

	private Button buttonRemove = new Button("X");

	public PaneRecent(IScriptRecentContainer parent, File file) {
		this.file = file;

		labelPath.setText(file.getAbsolutePath());

		buttonRemove.setOnAction(e -> {
			RecentFileManager.removeEntry(file);

			parent.fill();
		});
	}

	public Node getContent() {
		pane.setSpacing(15);

		pane.getChildren().add(buttonRemove);
		pane.getChildren().add(labelPath);

		return pane;
	}
}
