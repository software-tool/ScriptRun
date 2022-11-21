package script.frame.dialogs;

import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.misc.SystemUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogSystemInfo extends DialogBase {

	private String title;

	/**
	 * Third party licenses
	 */
	public DialogSystemInfo(Stage parent, String title) {
		super(parent);

		this.title = title;

		hasButtonCancel = false;
		minWidthOnOpen = 800;
		minHeightOnOpen = 750;

		init();

		initContent();
		dialog.setTitle(title);
	}

	private void init() {
	}

	@Override
	protected Pane getContent() {
		BorderPane pane = new BorderPane();

		VBox boxTop = new VBox();
		boxTop.setSpacing(14);

		Label label1 = new Label(title);

		String systemInfo = SystemUtils.getSystemInformation(false, null);

		TextArea area = new TextArea(systemInfo);
		area.setEditable(false);

		area.prefHeightProperty().bind(scrollPaneContent.heightProperty().subtract(30));
		area.prefWidthProperty().bind(scrollPaneContent.widthProperty().subtract(20));

		boxTop.getChildren().add(label1);

		pane.setTop(boxTop);
		pane.setCenter(area);

		return pane;
	}

	@Override
	protected boolean okPressed() {
		return true;
	}

}
