package script.frame.details.input;

import com.rwu.application.lang.Ln;
import com.rwu.fx.dialog.MultilineInputDialog;
import com.rwu.fx.dialog.OneValueInputDialog;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxContextMenuUtils;
import com.rwu.fx.util.FxControlUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxInteractionUtils;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.misc.StringUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import script.input.InputConfig;
import script.input.InputModeEnum;
import script.input.InputModeSize;
import script.input.InputSelectionItemConfig;
import script.lang.K;
import script.recent.ScriptValue;

import java.util.ArrayList;
import java.util.List;

public class PaneInput {

	private IInputParent parent;

	private HBox boxStart = new HBox();
	private HBox boxField = new HBox();
	private HBox boxInfo = new HBox();

	private HBox containerInputField = new HBox();

	private Label labelInput = new Label();

	private Label labelDescription = new Label();

	private Button buttonRemove = new Button();
	private Button buttonChangeTitle = new Button();
	private Button buttonEditDescription = new Button();
	private Button buttonSelectType = new Button();
	private Button buttonSelectSize = new Button();

	private TextArea areaInput = new TextArea();
	private TextField fieldInput = new TextField();

	private List<RadioButton> radioSelection = new ArrayList();

	private ToggleGroup selectionToggleGroup;

	private InputConfig configuration;
	private ScriptValue scriptValue;

	private ContextMenu contextMenuType = new ContextMenu();
	private ContextMenu contextMenuSize = new ContextMenu();

	public PaneInput(IInputParent parent, InputConfig configuration, ScriptValue scriptValue) {
		this.parent = parent;
		this.configuration = configuration;
		this.scriptValue = scriptValue;

		init();
		initLang();
		initActions();
		initContextMenus();

		prepareContent();

		initValues();
	}

	public void prepareContent() {
		boxStart.setSpacing(8);
		boxField.setSpacing(8);
		boxInfo.setSpacing(8);

		boxStart.setPadding(new Insets(3, 0, 3, 0));
		boxField.setPadding(new Insets(3, 0, 3, 10));
		boxInfo.setPadding(new Insets(3, 0, 3, 10));

		boxStart.getChildren().add(buttonRemove);
		boxStart.getChildren().add(labelInput);
		boxStart.getChildren().add(buttonChangeTitle);

		boxField.getChildren().add(containerInputField);
		boxField.getChildren().add(labelDescription);
		boxField.getChildren().add(buttonEditDescription);

		boxField.getChildren().add(buttonSelectType);
		boxField.getChildren().add(buttonSelectSize);

		updateOnModeChanged();

		fillContent();
	}

	private void init() {
		FxTooltip.setTooltip(buttonRemove, Ln.get(K.ScriptRemoveInput));

		FxIconUtils.setIconNoStyleWithSize(buttonSelectSize, "panel_resize_actual.png");

		FxIconUtils.setIconNoStyleWithSize(buttonRemove, "bin_closed.png");
		FxIconUtils.setIconNoStyleWithSize(buttonChangeTitle, "edit_button.png");
		FxIconUtils.setIconNoStyleWithSize(buttonEditDescription, "edit_button.png");
	}

	private void initLang() {
		FxTooltip.setTooltip(buttonChangeTitle, Ln.get(K.InputChangeLabel));
	}

	private void initActions() {
		areaInput.textProperty().addListener(e -> {
			configuration.setInputText(getValue());
		});

		fieldInput.textProperty().addListener(e -> {
			configuration.setInputText(getValue());
		});

		buttonRemove.setOnAction(e -> {
			parent.remove(configuration);
		});

		buttonChangeTitle.setOnAction(e -> {
			String preset = configuration.getLabel();

			OneValueInputDialog dialog = new OneValueInputDialog(AppWindowFx.getStage(), Ln.get(K.InputChangeLabel), null, Ln.get(K.Label), preset);
			if (dialog.open()) {
				String value = dialog.getValue1();

				configuration.setLabel(value);

				updateLabel();
			}
		});

		buttonEditDescription.setOnAction(e -> {
			String preset = configuration.getDescription();

			MultilineInputDialog dialog = new MultilineInputDialog(AppWindowFx.getStage(), Ln.get(K.InputChangeDescription), null, Ln.get(K.Description),
					preset);
			if (dialog.open()) {
				String value = dialog.getValue1();

				configuration.setDescription(value);

				updateLabel();
			}
		});

		buttonSelectType.setOnAction(e -> {
			FxContextMenuUtils.showContextMenu(contextMenuType, buttonSelectType);
		});

		buttonSelectSize.setOnAction(e -> {
			FxContextMenuUtils.showContextMenu(contextMenuSize, buttonSelectSize);
		});

	}

	private void initContextMenus() {
		MenuItem itemSizeSmall1 = new MenuItem();
		MenuItem itemSizeMedium1 = new MenuItem();
		MenuItem itemSizeBig1 = new MenuItem();

		MenuItem itemTypeLine = new MenuItem();
		MenuItem itemTypeMultiline = new MenuItem();
		MenuItem itemTypeSelection = new MenuItem();

		itemTypeLine.setText(Ln.get(K.InputModeTextfield));
		itemTypeMultiline.setText(Ln.get(K.InputModeTextarea));
		itemTypeSelection.setText(Ln.get(K.InputModeSelection));

		itemSizeSmall1.setText(Ln.get(K.InputSizeSmall1));
		itemSizeMedium1.setText(Ln.get(K.InputSizeMedium1));
		itemSizeBig1.setText(Ln.get(K.InputSizeBig1));

		contextMenuType.getItems().add(itemTypeLine);
		contextMenuType.getItems().add(itemTypeMultiline);
		contextMenuType.getItems().add(itemTypeSelection);

		contextMenuSize.getItems().add(itemSizeSmall1);
		contextMenuSize.getItems().add(itemSizeMedium1);
		contextMenuSize.getItems().add(itemSizeBig1);

		itemTypeLine.setOnAction(e -> {
			setModeSelected(InputModeEnum.Textfield);
		});
		itemTypeMultiline.setOnAction(e -> {
			setModeSelected(InputModeEnum.Textarea);
		});
		itemTypeSelection.setOnAction(e -> {
			setModeSelected(InputModeEnum.Selection);
		});

		itemSizeSmall1.setOnAction(e -> {
			setSize(InputModeSize.small1);
		});

		itemSizeMedium1.setOnAction(e -> {
			setSize(InputModeSize.medium1);
		});

		itemSizeBig1.setOnAction(e -> {
			setSize(InputModeSize.big1);
		});
	}

	private void setModeSelected(InputModeEnum setting) {
		configuration.setInputMode(setting);

		updateOnModeChanged();
	}

	private void setSize(InputModeSize size) {
		configuration.setSize(size);

		updateOnModeChanged();
	}

	private void updateLabel() {
		String configLabel = configuration.getLabel();
		String configDescription = configuration.getDescription();

		boolean mandatory = configuration.isMandatory();

		String labelShown = Ln.get(K.InputTitle);
		if (StringUtils.isValue(configLabel)) {
			labelShown = configLabel;
		}

		String label = labelShown;
		if (mandatory) {
			label += "*";
		}
		label += ":";

		labelInput.setText(label);
		labelDescription.setText(configDescription);
	}

	private void updateOnModeChanged() {
		InputModeEnum inputMode = configuration.getInputMode();

		fillContent();

		if (inputMode.isTextfield()) {
			FxIconUtils.setIconNoStyleWithSize(buttonSelectType, "textfield_rename.png");
		} else if (inputMode.isTextarea()) {
			FxIconUtils.setIconNoStyleWithSize(buttonSelectType, "text_align_left.png");
		} else if (inputMode.isSelection()) {
			FxIconUtils.setIconNoStyleWithSize(buttonSelectType, "radiobutton_group.png");
		}

		parent.storeInputs();
	}

	private void initValues() {
		String textPreset = configuration.getInputText();

		if (scriptValue != null) {
			textPreset = scriptValue.getStringValue();
		}

		setValue(textPreset);

		updateLabel();
	}

	public void setEditing(boolean editing) {
		FxControlUtils.setVisible(buttonRemove, editing);
		FxControlUtils.setVisible(buttonChangeTitle, editing);
		FxControlUtils.setVisible(buttonEditDescription, editing);

		FxControlUtils.setVisible(buttonSelectType, editing);
		FxControlUtils.setVisible(buttonSelectSize, editing);
	}

	private void fillContent() {
		containerInputField.getChildren().clear();
		radioSelection.clear();

		InputModeEnum inputMode = configuration.getInputMode();

		if (inputMode.isTextfield()) {
			containerInputField.getChildren().add(fieldInput);
		} else if (inputMode.isTextarea()) {
			containerInputField.getChildren().add(areaInput);
		} else if (inputMode.isSelection()) {
			VBox boxRadios = new VBox();
			boxRadios.setSpacing(5);

			List<InputSelectionItemConfig> selectionItems = configuration.getSelectionItems();
			for (InputSelectionItemConfig selectionItem : selectionItems) {
				RadioButton radio = new RadioButton();
				radio.setText(selectionItem.getText());
				radio.setUserData(selectionItem.getKeyword());

				radioSelection.add(radio);

				boxRadios.getChildren().add(radio);
			}

			selectionToggleGroup = FxInteractionUtils.setToggleGroupForList(radioSelection);
			selectionToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
					configuration.setInputText(getValue());
				}
			});

			containerInputField.getChildren().add(boxRadios);
		}

		InputModeSize size = configuration.getSize();

		fieldInput.getStyleClass().remove("input_size_small1");
		fieldInput.getStyleClass().remove("input_size_medium1");
		fieldInput.getStyleClass().remove("input_size_big1");

		areaInput.getStyleClass().remove("input_size_small1");
		areaInput.getStyleClass().remove("input_size_medium1");
		areaInput.getStyleClass().remove("input_size_big1");

		if (size == InputModeSize.small1) {
			fieldInput.getStyleClass().add("input_size_small1");
			areaInput.getStyleClass().add("input_size_small1");
		} else if (size == InputModeSize.medium1) {
			fieldInput.getStyleClass().add("input_size_medium1");
			areaInput.getStyleClass().add("input_size_medium1");
		} else if (size == InputModeSize.big1) {
			fieldInput.getStyleClass().add("input_size_big1");
			areaInput.getStyleClass().add("input_size_big1");
		}

		areaInput.getStyleClass().remove("input_height_small1");
		areaInput.getStyleClass().remove("input_height_medium1");
		areaInput.getStyleClass().remove("input_height_big1");

		if (inputMode.isTextarea()) {
			if (size == InputModeSize.small1) {
				areaInput.getStyleClass().add("input_height_small1");
			} else if (size == InputModeSize.medium1) {
				areaInput.getStyleClass().add("input_height_medium1");
			} else if (size == InputModeSize.big1) {
				areaInput.getStyleClass().add("input_height_big1");
			}
		}
	}

	public void setValue(String value) {
		InputModeEnum inputMode = configuration.getInputMode();
		if (inputMode.isTextfield()) {
			fieldInput.setText(value);
		} else if (inputMode.isTextarea()) {
			areaInput.setText(value);
		} else if (inputMode.isSelection()) {
			for (RadioButton radioButton : radioSelection) {
				String userData = radioButton.getUserData().toString();
				if (userData == null) {
					continue;
				}

				if (userData.equals(value)) {
					radioButton.setSelected(true);
					break;
				}
			}
		}
	}

	public String getValue() {
		InputModeEnum inputMode = configuration.getInputMode();

		String value = null;
		if (inputMode.isTextfield()) {
			value = fieldInput.getText();
		} else if (inputMode.isTextarea()) {
			value = areaInput.getText();
		} else if (inputMode.isSelection()) {
			for (RadioButton radioButton : radioSelection) {
				boolean selected = radioButton.isSelected();
				if (selected) {
					value = radioButton.getUserData().toString();
				}
			}
		}

		if (value != null && value.trim().isEmpty()) {
			value = null;
		}

		return value;
	}

	public void focusField() {
		Platform.runLater(() -> {
			InputModeEnum inputMode = configuration.getInputMode();

			if (inputMode.isTextfield()) {
				fieldInput.requestFocus();
			} else if (inputMode.isTextarea()) {
				areaInput.requestFocus();
			} else if (inputMode.isSelection()) {
				radioSelection.get(0).requestFocus();
			}
		});
	}

	public HBox getBoxStart() {
		return boxStart;
	}

	public HBox getBoxInfo() {
		return boxInfo;
	}

	public HBox getBoxField() {
		return boxField;
	}
}
