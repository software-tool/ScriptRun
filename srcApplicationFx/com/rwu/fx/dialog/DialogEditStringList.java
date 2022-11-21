package com.rwu.fx.dialog;

import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxCssUtils;
import com.rwu.fx.util.FxLayoutUtils;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class DialogEditStringList extends DialogBase {

	private GridPane gridPane = new GridPane();

	private String introduction;

	private List<String> values;
	private List<String> activated;

	private Button buttonAddField = new Button("+");

	private List<TextField> fields = new ArrayList();

	private String textNew;

	public DialogEditStringList(Window parent, String title, String introduction, List<String> values, List<String> activated, String textNew, String textButtonAdd) {
		super(parent);

		this.introduction = introduction;

		this.values = values;
		this.activated = activated;

		this.textNew = textNew;

		dialog.setTitle(title);

		init(textButtonAdd);
		initActions();

		addField();
	}

	private void init(String textButtonAdd) {
		FxTooltip.setTooltip(buttonAddField, textButtonAdd);
	}

	private void initActions() {
		buttonAddField.setOnAction(e -> {
			addField();
		});
	}

	private void addField() {
		TextField field = new TextField();
		field.setMaxWidth(250);

		fields.add(field);

		updateContent();
	}

	@Override
	public Pane getContent() {
		FxLayoutUtils.setDefaultColumnSpacing(gridPane);

		updateContent();

		return gridPane;
	}

	private void updateContent() {
		gridPane.getChildren().clear();

		int y = 0;
		if (introduction != null) {
			Label introductionLabel = new Label(introduction);
			introductionLabel.setPadding(INSETS_SPACE_BELOW);

			gridPane.add(introductionLabel, 0, y);

			GridPane.setColumnSpan(introductionLabel, 2);

			y++;
		}

		int i = 0;
		for (String value : values) {
			CheckBox checkbox = new CheckBox();
			checkbox.setPadding(INSETS_SPACE_BELOW);

			checkbox.setSelected(activated.contains(value));

			checkbox.setOnAction(e -> {
				applyActivated(checkbox.isSelected(), value);
			});

			gridPane.add(checkbox, 0, y);

			Label valueLabel = new Label(value);
			valueLabel.setPadding(INSETS_SPACE_BELOW);
			valueLabel.setUserData(value);
			valueLabel.setLabelFor(checkbox);

			gridPane.add(valueLabel, 1, y);

			y++;
		}

		for (TextField field : fields) {
			Label labelNew = new Label(textNew);

			gridPane.add(labelNew, 0, y);
			gridPane.add(field, 1, y);

			y++;
		}

		gridPane.add(buttonAddField, 1, y);
	}

	private void applyActivated(boolean selected, String value) {
		if (selected && !activated.contains(value)) {
			this.activated.add(value);
		}

		if (!selected) {
			this.activated.remove(value);
		}

		updateContent();
	}

	@Override
	protected boolean okPressed() {
		return true;
	}

	@Override
	public boolean open() {
		initContent();

		FxCssUtils.addCss(DialogEditStringList.class, dialog.getScene(), "dialog_common.css");

		return super.open();
	}

	public List<String> getResult() {
		List<String> result = new ArrayList();

		result.addAll(activated);

		for (TextField field : fields) {
			String fieldText = field.getText().trim();

			if (!fieldText.isEmpty()) {
				result.add(fieldText);
			}
		}

		return result;
	}
}
