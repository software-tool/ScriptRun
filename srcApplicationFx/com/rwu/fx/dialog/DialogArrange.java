package com.rwu.fx.dialog;

import java.util.ArrayList;
import java.util.List;

import com.rwu.fx.dialog.base.DialogBase;
import com.rwu.fx.dialog.base.FxDialogBase;
import com.rwu.fx.util.FxCssUtils;
import com.rwu.fx.util.FxLayoutUtils;
import com.rwu.fx.util.FxStyleUtils;
import com.rwu.misc.ListUtils;

import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class DialogArrange extends DialogBase {

	private GridPane gridPane = new GridPane();

	private String introduction;

	private List<String> values;
	private List<String> hidden;

	private List<CheckBox> checkboxes = new ArrayList();
	private List<Label> valueLabels = new ArrayList();

	private Label introductionLabel;

	private String dragStart;
	private String dragOver;

	private boolean hasCheckboxes;

	public DialogArrange(Window parent, String title, String introduction, List<String> values, List<String> hidden, boolean hasCheckboxes) {
		super(parent);

		this.introduction = introduction;

		this.values = values;
		this.hidden = hidden;

		this.hasCheckboxes = hasCheckboxes;

		minHeightOnOpen = 320;

		dialog.setTitle(title);
	}

	@Override
	public Pane getContent() {
		gridPane.setBackground(null);

		FxLayoutUtils.setDefaultColumnSpacing(gridPane);

		updateContent();

		return gridPane;
	}

	private void updateContent() {
		gridPane.getChildren().clear();
		checkboxes.clear();
		valueLabels.clear();

		dragStart = null;
		dragOver = null;

		int x = hasCheckboxes ? 1 : 0;

		int y = 0;
		if (introduction != null) {
			introductionLabel = new Label(introduction);
			introductionLabel.setPadding(INSETS_SPACE_BELOW);

			gridPane.add(introductionLabel, x, y);

			GridPane.setColumnSpan(introductionLabel, 2);

			y++;
		}

		int i = 0;
		for (String value : values) {
			HBox entryPane = new HBox();
			entryPane.setPadding(new Insets(1, 6, 1, 6));

			entryPane.setUserData(value);
			entryPane.setSpacing(6);

			entryPane.getStyleClass().add("content_draggable");

			Label valueLabel = new Label(value);
			valueLabel.setPadding(INSETS_SPACE_BELOW);
			valueLabel.setUserData(value);

			valueLabels.add(valueLabel);

			if (hasCheckboxes) {
				CheckBox box = new CheckBox();

				boolean selected = !(hidden.contains(value));
				box.setSelected(selected);

				checkboxes.add(box);

				entryPane.getChildren().add(box);

				i++;
			}

			entryPane.getChildren().add(valueLabel);

			gridPane.add(entryPane, x, y);

			y++;
		}

		gridPane.setOnDragDetected(evt -> {
			EventTarget target = evt.getTarget();
			if (target instanceof Label) {
				Label label = (Label) target;

				label.getStyleClass().add("content_drag_start");

				String valueSelected = label.getUserData().toString();
				dragStart = valueSelected;
			} else if (target instanceof Text) {
				Text label = (Text) target;

				label.getParent().getStyleClass().add("content_drag_start");

				String valueSelected = label.getText();
				dragStart = valueSelected;
			} else if (target instanceof HBox) {
				HBox box = (HBox) target;

				box.getStyleClass().add("content_drag_start");

				String valueSelected = (String)box.getUserData();
				dragStart = valueSelected;
			}
			//System.out.println("src: " + src);

			gridPane.startFullDrag();
		});

		gridPane.setOnMouseDragOver(evt -> {
			EventTarget target = evt.getTarget();
			if (target instanceof Label) {
				Label label = (Label) target;

				resetDragOver();

				label.getStyleClass().add("content_drag_over");

				String valueSelected = label.getUserData().toString();
				dragOver = valueSelected;
			} else if (target instanceof Text) {
				Text label = (Text) target;

				resetDragOver();

				label.getParent().getStyleClass().add("content_drag_over");

				String valueSelected = label.getText();
				dragOver = valueSelected;
			} else if (target instanceof HBox) {
				HBox box = (HBox) target;

				String valueSelected = (String)box.getUserData();
				dragOver = valueSelected;
			}

			//System.out.println("over: " + evt.getTarget() + ", " + evt.getSceneY() + ", " + evt.getY());
		});

		gridPane.setOnMouseDragReleased(evt -> {
			finishDrag();
		});

		gridPane.setOnMouseReleased(evt -> {
			finishDrag();
		});
	}

	private void resetDragOver() {
		// Reset
		for (Label valueLabel : valueLabels) {
			valueLabel.getStyleClass().remove("content_drag_over");
		}
		introductionLabel.getStyleClass().remove("content_drag_over");
	}

	private void finishDrag() {
		if (dragStart == null || dragOver == null) {
			return;
		}

		for (Label valueLabel : valueLabels) {
			valueLabel.getStyleClass().remove("content_drag_start");
			valueLabel.getParent().getStyleClass().remove("content_drag_start");
		}

		if (!dragStart.equals(dragOver)) {
			//System.out.println("Finish: " + dragStart + ", " + dragOver);

			boolean toTop = dragOver.equals(introductionLabel.getText());

			applyHidden();
			moveValue(dragStart, dragOver, toTop);

			updateContent();
		}
	}

	private void applyHidden() {
		hidden = getHidden();
	}

	private void moveValue(String moving, String target, boolean toTop) {
		int pos = values.indexOf(moving);
		int targetPos = values.indexOf(target) + 1;

		if (toTop) {
			targetPos = 0;
		}

		if (pos == targetPos) {
			return;
		}

		List<String> result = new ArrayList();

		int i = -1;
		for (String value : values) {
			i++;

			if (targetPos == i) {
				ListUtils.addIfNotExisting(result, moving);
				ListUtils.addIfNotExisting(result, value);
				continue;
			}

			if (pos == i) {
				continue;
			}

			ListUtils.addIfNotExisting(result, value);
		}

		ListUtils.addIfNotExisting(result, moving);

		values = result;
	}

	@Override
	protected boolean okPressed() {
		return true;
	}

	@Override
	public boolean open() {
		initContent();

		FxCssUtils.addCss(DialogArrange.class, dialog.getScene(), "dialog_common.css");

		return super.open();
	}

	public List<String> getValues() {
		return values;
	}

	public List<String> getHidden() {
		List<String> result = new ArrayList();

		int i = 0;
		for (String value : values) {
			CheckBox box = checkboxes.get(i);

			if (!box.isSelected()) {
				result.add(value);
			}

			i++;
		}

		return result;
	}
}
