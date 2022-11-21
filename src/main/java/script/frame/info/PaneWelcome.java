package script.frame.info;

import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.Ln;
import com.rwu.fx.util.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import script.app.App;
import script.config.ConfigConstants;
import script.controller.ControllerPages;
import script.controller.ControllerPath;
import script.data.ScriptDirectory;
import script.frame.common.UiCommon;
import script.frame.intf.IContent;
import script.frame.recent.IScriptRecentContainer;
import script.frame.recent.PaneScriptRecents;
import script.lang.K;
import script.manager.DirectoryConfigManager;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;
import script.util.SystemUtil;

import java.net.URI;
import java.util.List;

public class PaneWelcome implements IContent {

	private VBox main = new VBox();

	private CheckBox checkShowIntroduction = new CheckBox();
	private CheckBox checkShowRecent = new CheckBox();

	// Info
	private Button buttonClose = new Button();

	private Label labelTitle = new Label();
	private Label labelIntroduction = new Label();

	// Groovy
	private Label labelGroovyTitle = new Label();
	private Label labelGroovyIntro = new Label();

	private Hyperlink linkGroovyMain = new Hyperlink();
	private Hyperlink linkGroovyUsage1 = new Hyperlink();
	private Hyperlink linkGroovyUsage2 = new Hyperlink();

	// Examples
	private Label labelExamplesTitle = new Label();
	private Label labelExamplesIntro = new Label();

	private Label labelExample1Title = new Label();
	private Label labelExample1Info = new Label();
	private Button buttonInitExample1 = new Button();

	private Label labelExample2Title = new Label();
	private Label labelExample2Info = new Label();
	private Button buttonInitExample2 = new Button();

	// Start
	private Label labelStartNowTitle = new Label();
	private Label labelPersonalDir = new Label();

	private Button buttonAddScript = new Button();

	// Recent
	private Label labelRecents = new Label();
	private VBox recents = new VBox();

	public PaneWelcome() {
		init();
		initValues();

		initActions();

		updateRecents();

		updateShow();
	}

	private void init() {
		labelTitle.getStyleClass().add("titles");
		labelGroovyTitle.getStyleClass().add("titles");
		labelExamplesTitle.getStyleClass().add("titles");
		labelStartNowTitle.getStyleClass().add("titles");

		FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png");

		checkShowIntroduction.setText(Ln.get(K.WelcomeShowInfo));
		checkShowRecent.setText(Ln.get(K.PageRecents));

		labelTitle.setText(Ln.get(K.IntroductionTitle));
		labelIntroduction.setText(Ln.get(K.Introduction));

		// Groovy

		labelGroovyTitle.setText(Ln.get(K.IntroGroovyTitle));
		labelGroovyIntro.setText(Ln.get(K.IntroGroovy));

		linkGroovyMain.setText(Ln.get(K.LinkGroovyMain));
		linkGroovyUsage1.setText(Ln.get(K.LinkGroovyUsage1));
		linkGroovyUsage2.setText(Ln.get(K.LinkGroovyUsage2));

		// Examples

		labelExamplesTitle.setText(Ln.get(K.Examples));
		labelExamplesIntro.setText(Ln.get(K.ExamplesIntroduction));

		labelExample1Title.setText(Ln.get(K.Example1Title));
		labelExample1Info.setText(Ln.get(K.Example1Info));

		labelExample2Title.setText(Ln.get(K.Example2Title));
		labelExample2Info.setText(Ln.get(K.Example2Info));

		// Start

		labelStartNowTitle.setText(Ln.get(K.StartNow));
		labelPersonalDir.setText(App.getConfigDir().getAbsolutePath());

		buttonAddScript.setText(Ln.dots(K.AddScript));

		// Recent

		labelRecents.setText(Ln.get(K.PageRecents));
	}

	private void initValues() {
		String hideInfo = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_INFO);
		checkShowIntroduction.setSelected(hideInfo == null);

		String hideRecent = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_RECENT);
		checkShowRecent.setSelected(hideRecent == null);
	}

	private void initActions() {
		buttonClose.setOnAction(e -> {
			ControllerPages.closeCurrentPage();
		});

		linkGroovyMain.setOnAction(e -> {
			SystemUtil.browse(URI.create(linkGroovyMain.getText()));
		});
		linkGroovyUsage1.setOnAction(e -> {
			SystemUtil.browse(URI.create(linkGroovyUsage1.getText()));
		});
		linkGroovyUsage2.setOnAction(e -> {
			SystemUtil.browse(URI.create(linkGroovyUsage2.getText()));
		});

		buttonAddScript.setOnAction(e -> {
			DirectoryConfigManager.addScriptPath(App.getConfigDir());

			ControllerPath.setCurrentDirectory(new ScriptDirectory(App.getConfigDir()), true);
		});

		checkShowIntroduction.setOnAction(e -> {
			boolean selected = checkShowIntroduction.isSelected();

			if (selected) {
				AppConfig.resetValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_INFO);
			} else {
				AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_INFO, ConfigConstants.YES);
			}

			updateShow();
		});

		checkShowRecent.setOnAction(e -> {
			boolean selected = checkShowRecent.isSelected();

			if (selected) {
				AppConfig.resetValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_RECENT);
			} else {
				AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_RECENT, ConfigConstants.YES);
			}

			updateShow();
		});
	}

	private void updateShow() {
		// Info

		String hideInfo = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_INFO);
		boolean showInfo = (hideInfo == null);

		FxControlUtils.setVisible(labelIntroduction, showInfo);

		FxControlUtils.setVisible(labelGroovyTitle, showInfo);
		FxControlUtils.setVisible(labelGroovyIntro, showInfo);
		FxControlUtils.setVisible(linkGroovyMain, showInfo);

		FxControlUtils.setVisible(labelStartNowTitle, showInfo);

		// Recent

		String hideRecent = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.HIDE_WELCOME_RECENT);
		boolean showRecent = (hideRecent == null);

		FxControlUtils.setVisible(labelRecents, showRecent);

		FxControlUtils.setVisible(recents, showRecent);
	}

	private void updateRecents() {
		IScriptRecentContainer container = new IScriptRecentContainer() {
			@Override
			public void fill() {
				updateRecents();
			}
		};

		List<ScriptRecent> recentsLimited = ScriptRecentManager.getRecentsLimited(5);

		PaneScriptRecents.applyGrouped(container, recents, recentsLimited);
	}

	@Override
	public Node getContent() {
		main.setPadding(UiCommon.getContentDefaultInsets());
		checkShowIntroduction.setPadding(new Insets(0, 6, 0, 0));
		checkShowRecent.setPadding(new Insets(0, 2, 0, 0));

		int y = 0;

		// Intro

		Pane panePush = new Pane();
		FxLayoutUtils.setFillWidth(panePush);

		GridPane paneInfo = new GridPane();

		int x = 0;

		paneInfo.add(labelTitle, x++, y);
		paneInfo.add(panePush, x++, y);
		paneInfo.add(checkShowIntroduction, x++, y);
		paneInfo.add(checkShowRecent, x++, y);
		paneInfo.add(buttonClose, x++, y);

		y++;
		paneInfo.add(labelIntroduction, 0, y);

		// Groovy

		y++;
		paneInfo.add(labelGroovyTitle, 0, y);
		y++;
		paneInfo.add(labelGroovyIntro, 0, y);

		y++;
		paneInfo.add(linkGroovyMain, 0, y);

		// Examples

		//		main.getChildren().add(labelExamplesTitle);
		//		main.getChildren().add(labelExamplesIntro);
		//
		//		addExample(main, labelExample1Title, labelExample1Info, buttonInitExample1);
		//		addExample(main, labelExample2Title, labelExample2Info, buttonInitExample2);

		// Start

		y++;
		paneInfo.add(labelStartNowTitle, 0, y);
		y++;
		paneInfo.add(labelPersonalDir, 0, y);

		y++;
		paneInfo.add(buttonAddScript, 0, y);

		y++;
		paneInfo.add(labelRecents, 0, y);

		main.getChildren().add(paneInfo);
		main.getChildren().add(recents);

		ScrollPane scrollPane = new ScrollPane(main);
		FxScrollUtils.setFitTrue(scrollPane);
		FxBorderUtils.setBorderDisabled(scrollPane);

		return scrollPane;
	}

	private void addExample(VBox parent, Label labelTitle, Label label, Button button) {
		HBox box = new HBox();
		VBox info = new VBox();

		box.setSpacing(11);
		info.setSpacing(3);

		labelTitle.getStyleClass().add("titles");

		button.setMinWidth(130);
		button.setMinHeight(70);

		info.getChildren().add(labelTitle);
		info.getChildren().add(label);

		box.getChildren().add(button);
		box.getChildren().add(info);

		parent.getChildren().add(box);
	}
}
