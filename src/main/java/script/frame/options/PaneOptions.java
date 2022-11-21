package script.frame.options;

import com.rwu.application.config.AppConfig;
import com.rwu.application.lang.LangHelper;
import com.rwu.application.lang.LangHelper.Language;
import com.rwu.application.lang.Ln;
import com.rwu.fx.fontsize.FontSize;
import com.rwu.fx.tabsize.TabSize;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxBorderUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxInteractionUtils;
import com.rwu.fx.util.FxScrollUtils;
import com.rwu.fx.window.AppWindowFx;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import script.ScriptingRun;
import script.config.ConfigConstants;
import script.controller.ControllerPages;
import script.frame.code.editor.EditorFontSize;
import script.frame.common.UiCommon;
import script.frame.intf.IContent;
import script.frame.prepare.PrepareCss;
import script.lang.K;

public class PaneOptions implements IContent {

	private static final int WIDTH_LABELS = 120;
	private static final int WIDTH_VALUES = 40;

	private VBox main = new VBox();

	private Label labelInterfaceTitle = new Label();
	private Label labelStartTitle = new Label();
	private Label labelEditorTitle = new Label();

	private Button buttonClose = new Button();

	// Interface

	private Label labelInterfaceFontSizeInfo = new Label();
	private Label labelInterfaceFontSize = new Label();

	private Button buttonInterfaceFontSizePlus = new Button("+");
	private Button buttonInterfaceFontSizeMinus = new Button("-");

	private Label labelLanguage = new Label();
	private Label labelLanguageInfo = new Label();

	private RadioButton radioDe = new RadioButton("German");
	private RadioButton radioEn = new RadioButton("English");
	private RadioButton radioEs = new RadioButton("Spanish");

	private Label labelColorScheme = new Label();

	private Button buttonColorGreen = new Button();
	private Button buttonColorBlue = new Button();
	private Button buttonColorDarkGreen = new Button();

	// Editor

	private Label labelEditorFontSizeInfo = new Label();
	private Label labelEditorFontSize = new Label();

	private Button buttonEditorFontSizePlus = new Button("+");
	private Button buttonEditorFontSizeMinus = new Button("-");

	private Label labelEditorTabSizeInfo = new Label();
	private Label labelEditorTabSize = new Label();

	private Button buttonEditorTabSizePlus = new Button("+");
	private Button buttonEditorTabSizeMinus = new Button("-");

	public PaneOptions() {
		initLang();
		initActions();
		initValues();
	}

	private void initLang() {
		labelInterfaceTitle.getStyleClass().add("titles");
		labelStartTitle.getStyleClass().add("titles");
		labelEditorTitle.getStyleClass().add("titles");

		labelInterfaceTitle.setText(Ln.get(K.UserInterface));
		labelEditorTitle.setText(Ln.get(K.CodeEditor));

		labelLanguage.setText(Ln.colon(K.Language));
		labelLanguageInfo.setText(Ln.get(K.LanguageInfoRestart));
		labelColorScheme.setText(Ln.colon(K.ColorScheme));

		labelInterfaceFontSize.setText(getCurrentFontSize() + " %");
		labelEditorFontSize.setText(getCurrentEditorFontSize());
		labelEditorTabSize.setText(getCurrentTabSize());

		buttonColorGreen.setText(Ln.get(K.ColorGreen));
		buttonColorBlue.setText(Ln.get(K.ColorBlue));
		buttonColorDarkGreen.setText(Ln.get(K.ColorDarkGreen));

		FxInteractionUtils.setToggleGroup(radioDe, radioEn, radioEs);

		FxTooltip.setTooltip(buttonClose, Ln.get(K.MenuClosePage));
		FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png");
	}

	private void initActions() {
		buttonClose.setOnAction(e -> {
			ControllerPages.closeCurrentPage();
		});

		// Interface

		buttonInterfaceFontSizePlus.setOnAction(e -> {
			String fontSize = getCurrentFontSize();

			String newValue = FontSize.getNext(fontSize);
			setNewFontSize(newValue);
		});

		buttonInterfaceFontSizeMinus.setOnAction(e -> {
			String fontSize = getCurrentFontSize();

			String newValue = FontSize.getPrevious(fontSize);
			setNewFontSize(newValue);
		});

		radioDe.setOnAction(e -> {
			onLanguageChanged();
		});
		radioEn.setOnAction(e -> {
			onLanguageChanged();
		});
		radioEs.setOnAction(e -> {
			onLanguageChanged();
		});

		buttonColorGreen.setOnAction(e -> {
			onColorSchemeSelected("green");
		});

		buttonColorBlue.setOnAction(e -> {
			onColorSchemeSelected("blue");
		});

		buttonColorDarkGreen.setOnAction(e -> {
			onColorSchemeSelected("dark_green");
		});

		// Editor

		buttonEditorFontSizePlus.setOnAction(e -> {
			String fontSize = getCurrentEditorFontSize();

			String newValue = EditorFontSize.getNext(fontSize);
			setNewEditorFontSize(newValue);
		});

		buttonEditorFontSizeMinus.setOnAction(e -> {
			String fontSize = getCurrentEditorFontSize();

			String newValue = EditorFontSize.getPrevious(fontSize);
			setNewEditorFontSize(newValue);
		});

		// Editor: Tabs

		buttonEditorTabSizePlus.setOnAction(e -> {
			String fontSize = getCurrentTabSize();

			String newValue = TabSize.getNext(fontSize);
			setNewTabSize(newValue);
		});

		buttonEditorTabSizeMinus.setOnAction(e -> {
			String fontSize = getCurrentTabSize();

			String newValue = TabSize.getPrevious(fontSize);
			setNewTabSize(newValue);
		});
	}

	private void initValues() {
		Language lang = LangHelper.language;

		if (lang == Language.GERMAN) {
			radioDe.setSelected(true);
		} else if (lang == Language.ENGLISH) {
			radioEn.setSelected(true);
		} else if (lang == Language.SPANISH) {
			radioEs.setSelected(true);
		}
	}

	private void onLanguageChanged() {
		String key = null;
		if (radioDe.isSelected()) {
			key = LangHelper.getLangSelection(Language.GERMAN);
			LangHelper.language = Language.GERMAN;
		} else if (radioEn.isSelected()) {
			key = LangHelper.getLangSelection(Language.ENGLISH);
			LangHelper.language = Language.ENGLISH;
		} else if (radioEs.isSelected()) {
			key = LangHelper.getLangSelection(Language.SPANISH);
			LangHelper.language = Language.SPANISH;
		}

		if (key != null) {
			AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.USER_SELECTED_LANGUAGE, key);
		}

		ScriptingRun.applyLang();
		initLang();
	}

	private void onColorSchemeSelected(String scheme) {
		PrepareCss.applyColorScheme(scheme);

		AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.COLOR_SCHEME, scheme);
	}

	private String getCurrentFontSize() {
		String fontSize = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.INTERFACE_FONT_SIZE);
		if (fontSize == null) {
			fontSize = "100";
		}

		return fontSize;
	}

	private String getCurrentEditorFontSize() {
		String fontSize = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_FONT_SIZE);
		if (fontSize == null) {
			fontSize = "16";
		}

		return fontSize;
	}

	private String getCurrentTabSize() {
		String fontSize = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_TAB_SIZE);
		if (fontSize == null) {
			fontSize = "4";
		}

		return fontSize;
	}

	private void setNewFontSize(String newValue) {
		AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.INTERFACE_FONT_SIZE, newValue);

		labelInterfaceFontSize.setText(newValue + " %");

		PrepareCss.applyCss(AppWindowFx.getStage().getScene());
	}

	private void setNewEditorFontSize(String newValue) {
		AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_FONT_SIZE, newValue);

		labelEditorFontSize.setText(newValue);

		PrepareCss.applyCss(AppWindowFx.getStage().getScene());
	}

	private void setNewTabSize(String newValue) {
		AppConfig.setValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_TAB_SIZE, newValue);

		labelEditorTabSize.setText(newValue);

		PrepareCss.applyCss(AppWindowFx.getStage().getScene());
	}

	@Override
	public Node getContent() {
		main.setPadding(UiCommon.getContentDefaultInsets());
		main.setSpacing(12);
		main.getStyleClass().add("pane_options");

		// Interface

		BorderPane line1 = new BorderPane();
		line1.setLeft(labelInterfaceTitle);
		line1.setRight(buttonClose);

		main.getChildren().add(line1);

		addInterface();

		// Editor

		main.getChildren().add(labelEditorTitle);

		addEditor();
		addEditorTabSize();

		ScrollPane scrollPane = new ScrollPane(main);
		FxScrollUtils.setFitTrue(scrollPane);
		FxBorderUtils.setBorderDisabled(scrollPane);

		return scrollPane;
	}

	private void addInterface() {
		labelInterfaceFontSizeInfo.setText(Ln.colon(K.FontSize));
		labelInterfaceFontSizeInfo.setMinWidth(WIDTH_LABELS);

		labelLanguage.setMinWidth(WIDTH_LABELS);
		labelColorScheme.setMinWidth(WIDTH_LABELS);

		labelInterfaceFontSize.setMinWidth(WIDTH_VALUES);

		HBox boxFontSize = new HBox();
		boxFontSize.setSpacing(4);

		boxFontSize.getChildren().add(labelInterfaceFontSizeInfo);
		boxFontSize.getChildren().add(labelInterfaceFontSize);

		boxFontSize.getChildren().add(buttonInterfaceFontSizeMinus);
		boxFontSize.getChildren().add(buttonInterfaceFontSizePlus);

		Label labelPlaceholder1 = new Label();
		labelPlaceholder1.setMinWidth(WIDTH_LABELS);

		VBox boxLanguages = new VBox();
		boxLanguages.setSpacing(2);
		boxLanguages.getChildren().add(radioEn);
		boxLanguages.getChildren().add(radioEs);
		boxLanguages.getChildren().add(radioDe);

		HBox boxLanguage = new HBox();
		HBox boxLanguageInfo = new HBox();
		boxLanguage.setSpacing(4);
		boxLanguageInfo.setSpacing(4);
		boxLanguage.getChildren().add(labelLanguage);
		boxLanguage.getChildren().add(boxLanguages);

		boxLanguageInfo.getChildren().add(labelPlaceholder1);
		boxLanguageInfo.getChildren().add(labelLanguageInfo);

		HBox boxColorScheme = new HBox();
		boxColorScheme.setSpacing(4);
		boxColorScheme.getChildren().add(labelColorScheme);
		boxColorScheme.getChildren().add(buttonColorGreen);
		boxColorScheme.getChildren().add(buttonColorBlue);
		boxColorScheme.getChildren().add(buttonColorDarkGreen);

		main.getChildren().add(boxFontSize);
		main.getChildren().add(boxLanguage);
		main.getChildren().add(boxLanguageInfo);
		main.getChildren().add(boxColorScheme);
	}

	private void addEditor() {
		labelEditorFontSizeInfo.setText(Ln.colon(K.FontSize));
		labelEditorFontSizeInfo.setMinWidth(WIDTH_LABELS);

		labelEditorFontSize.setMinWidth(WIDTH_VALUES);

		HBox box = new HBox();
		box.setSpacing(4);

		box.getChildren().add(labelEditorFontSizeInfo);
		box.getChildren().add(labelEditorFontSize);

		box.getChildren().add(buttonEditorFontSizeMinus);
		box.getChildren().add(buttonEditorFontSizePlus);

		main.getChildren().add(box);
	}

	private void addEditorTabSize() {
		labelEditorTabSizeInfo.setText(Ln.colon(K.TabSize));
		labelEditorTabSizeInfo.setMinWidth(WIDTH_LABELS);

		labelEditorTabSize.setMinWidth(WIDTH_VALUES);

		HBox box = new HBox();
		box.setSpacing(4);

		box.getChildren().add(labelEditorTabSizeInfo);
		box.getChildren().add(labelEditorTabSize);

		box.getChildren().add(buttonEditorTabSizeMinus);
		box.getChildren().add(buttonEditorTabSizePlus);

		main.getChildren().add(box);
	}
}
