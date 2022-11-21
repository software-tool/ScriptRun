package script.frame.dialogs;

import com.rwu.application.lang.Ln;
import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.misc.SystemUtils;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import script.app.App;
import script.lang.K;

public class DialogAbout extends DialogBase {

	private String title;
	private String url;
	private String thirdParty;
	private String thirdPartyIcons;

	Label labelWebsite = new Label();
	Label labelConfigDir = new Label();
	Label labelRecentDir = new Label();

	Label labelSystemInfo = new Label();

	private Hyperlink linkWebsite = new Hyperlink();
	private Hyperlink linkOpenConfigDirectory = new Hyperlink();
	private Hyperlink linkOpenRecentDirectory = new Hyperlink();

	private Hyperlink linkOpenSystemInfo = new Hyperlink();

	/**
	 * Third party licenses
	 */
	public DialogAbout(Stage parent, String title, String url, String thirdParty, String thirdPartyIcons) {
		super(parent);

		this.title = title;
		this.url = url;
		this.thirdParty = thirdParty;
		this.thirdPartyIcons = thirdPartyIcons;

		hasButtonCancel = false;
		minWidthOnOpen = 600;
		minHeightOnOpen = 550;

		init();

		initContent();
		dialog.setTitle(title);
	}

	private void init() {
		labelWebsite.setText(Ln.colonSpace(K.LabelWebsite));
		labelConfigDir.setText(Ln.colonSpace(K.LabelConfigDir));
		labelRecentDir.setText(Ln.colonSpace(K.LabelRecentDir));
		labelSystemInfo.setText(Ln.colonSpace(K.YourSystem));

		linkWebsite.setText(url);
		linkOpenConfigDirectory.setText(App.getConfigDir().getAbsolutePath());
		linkOpenRecentDirectory.setText(App.getScriptRecentDir().getAbsolutePath());

		linkOpenSystemInfo.setText("Show System Information");

		linkWebsite.setOnAction((e) -> {
			SystemUtils.openUrl(linkWebsite.getText());
		});
		linkOpenConfigDirectory.setOnAction((e) -> {
			SystemUtils.openDirectory(App.getConfigDir());
		});
		linkOpenRecentDirectory.setOnAction((e) -> {
			SystemUtils.openDirectory(App.getScriptRecentDir());
		});

		linkOpenSystemInfo.setOnAction((e) -> {
			DialogSystemInfo dialog = new DialogSystemInfo(null, "System Information");
			dialog.open();
		});
	}

	@Override
	protected Pane getContent() {
		BorderPane pane = new BorderPane();

		GridPane gridPaneLinks = new GridPane();

		VBox boxTop = new VBox();
		boxTop.setSpacing(14);

		Label label1 = new Label(title);

		TextArea area = new TextArea(thirdParty + "\n\n" + thirdPartyIcons);
		area.setEditable(false);

		area.prefHeightProperty().bind(scrollPaneContent.heightProperty().subtract(160));
		area.prefWidthProperty().bind(scrollPaneContent.widthProperty().subtract(20));

		int y = 0;

		gridPaneLinks.add(labelWebsite, 0, y);
		gridPaneLinks.add(linkWebsite, 1, y);

		y++;
		gridPaneLinks.add(labelConfigDir, 0, y);
		gridPaneLinks.add(linkOpenConfigDirectory, 1, y);

		y++;
		gridPaneLinks.add(labelRecentDir, 0, y);
		gridPaneLinks.add(linkOpenRecentDirectory, 1, y);

		y++;
		gridPaneLinks.add(labelSystemInfo, 0, y);
		gridPaneLinks.add(linkOpenSystemInfo, 1, y);

		boxTop.getChildren().add(label1);
		boxTop.getChildren().add(gridPaneLinks);

		pane.setTop(boxTop);
		pane.setCenter(area);

		return pane;
	}

	@Override
	protected boolean okPressed() {
		return true;
	}

}
