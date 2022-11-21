package com.rwu.fx.dialog;

import com.rwu.fx.alerts.AlertsBase;
import com.rwu.fx.controls.ControlFactory;
import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.fx.dialog.validate.IInputValidation;
import com.rwu.fx.util.FxLayoutUtils;
import com.rwu.misc.Pair;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class MultilineInputDialog extends DialogBase {

	private static final int ERROR_FIELD_1 = 1;

	private TextArea text1;

	private String introduction;

	private String labeltext1 = null;
	private String value1 = null;
	private String defaultValue1;

	private String desc1 = null;

	private Label label1;

	private IInputValidation validationField1;

	public MultilineInputDialog(Window parent, String title, String introduction, String labeltext1, String defaultValue1) {
		super(parent);

		if (defaultValue1 == null) {
			defaultValue1 = "";
		}

		this.introduction = introduction;

		this.labeltext1 = labeltext1;

		this.defaultValue1 = defaultValue1;

		dialog.setTitle(title);
	}

	public void setValidationField1(IInputValidation validationField1) {
		this.validationField1 = validationField1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getValue1() {
		return value1;
	}

	@Override
	public Pane getContent() {
		GridPane gridPane = new GridPane();
		FxLayoutUtils.setDefaultColumnSpacing(gridPane);

		int y = 0;

		if (introduction != null) {
			Label introductionLabel = new Label(introduction);
			introductionLabel.setPadding(INSETS_SPACE_BELOW);

			gridPane.add(introductionLabel, 0, y);

			GridPane.setColumnSpan(introductionLabel, 2);
		}

		label1 = new Label(labeltext1 + ":");
		text1 = new TextArea(defaultValue1);

		y++;
		gridPane.add(label1, 0, y);
		gridPane.add(text1, 1, y);

		if (desc1 != null) {
			// Description

			y++;

			Label labelDesc1 = ControlFactory.createLabel(desc1);
			labelDesc1.setPadding(INSETS_SPACE_ABOVE);

			gridPane.add(labelDesc1, 0, y);
			GridPane.setColumnSpan(labelDesc1, 2);
		}

		return gridPane;
	}

	@Override
	protected boolean okPressed() {
		value1 = getValueNotNull(text1);

		int fieldWithError = validate(value1);
		if (fieldWithError == ERROR_FIELD_1) {
			text1.requestFocus();
			return false;
		}

		return true;
	}

	@Override
	public boolean open() {
		initContent();

		return super.open();
	}

	/**
	 * Check input
	 * 
	 * @return (-1) No error, (1) Error first field
	 */
	private int validate(String input1) {
		// Field 1

		if (validationField1 != null) {
			Pair<String, String> error = validationField1.validate(input1);
			if (error != null) {
				// Input error

				AlertsBase.openInformation(dialog, error.getFirst(), error.getSecond());

				return ERROR_FIELD_1;
			}
		}

		return -1;
	}

}
