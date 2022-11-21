package com.rwu.fx.dialog.base;

import com.rwu.application.lang.generic.LCommon;
import com.rwu.fx.fontsize.FontSize;
import com.rwu.fx.util.FxBorderUtils;
import com.rwu.fx.util.FxCssUtils;
import com.rwu.fx.util.FxStyleUtils;
import com.rwu.misc.AppBasic;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class DialogBase {

	private static int TEXTFIELD_WIDTH_MIN = 200;

	public static Image APP_ICON_IMAGE = null;

	private static int BUTTONS_MIN_WIDTH = 80;

	private static int MIN_WIDTH = 80;
	private static int MIN_HEIGHT = 100;

	private static int PADDING_OUTSIDE = 6;
	private static Insets PADDING_BUTTONS = new Insets(9, 5, 5, 5);

	protected static Insets INSETS_SPACE_ABOVE = new Insets(7, 0, 0, 0);
	protected static Insets INSETS_SPACE_BELOW = new Insets(0, 0, 7, 0);

	protected Stage dialog;
	protected Window owner;

	protected ScrollPane scrollPaneContent = new ScrollPane();

	private int width;
	private int height;

	protected Button buttonOk;
	protected Button buttonCancel;

	protected Button buttonExtra;

	protected boolean okPressed = false;

	private boolean modal = true;
	private boolean addScrollPane = true;

	/** On open: minimum size */
	protected double minWidthOnOpen = -1;
	protected double minHeightOnOpen = -1;

	protected boolean calculatedSizeAsMinSize = false;

	/** Cancel yes/no */
	protected boolean hasButtonCancel = true;

	/** Ok -> yes */
	protected boolean isButtonYes = false;

	/** Other texts for Ok/Cancel */
	protected String textButtonOk = null;
	protected String textButtonCancel = null;

	protected IDialogCallback callback;

	private String dialogKey;

	/** Content */
	protected abstract Node getContent();

	/** When Ok */
	protected abstract boolean okPressed();

	protected void onCancel() {
		// Empty, can be overwridden
	}

	/**
	 * Constructor, size is calculated
	 */
	protected DialogBase(Window owner) {
		this(owner, -1, -1, null, true);
	}

	/**
	 * Constructor
	 * 
	 * @deprecated
	 */
	@Deprecated
	protected DialogBase(Window owner, int width, int height) {
		this(owner, width, height, null, true);
	}

	/**
	 * Constructor
	 */
	protected DialogBase(Window owner, int width, int height, String dialogKey, boolean modal) {
		this.owner = owner;
		this.width = width;
		this.height = height;
		this.dialogKey = dialogKey;
		this.modal = modal;

		initBase();
	}

	protected void initBase() {
		dialog = new Stage();
		dialog.setMinWidth(MIN_WIDTH);
		dialog.setMinHeight(MIN_HEIGHT);

		if (owner != null) {
			dialog.initOwner(owner);

			if (modal) {
				dialog.initModality(Modality.APPLICATION_MODAL);
			}
		}

		if (width != -1) {
			dialog.setWidth(width);
		}
		if (height != -1) {
			dialog.setHeight(height);
		}

		if (dialogKey != null) {
			restoreSize();
		}
	}

	protected void initContent() {
		// Components
		initComponents();

		// Actions
		initActions();

		// Content

		GridPane main = new GridPane();
		main.setPadding(new Insets(PADDING_OUTSIDE));

		Node content = getContent();

		if (addScrollPane) {
			FxBorderUtils.setBorderDisabled(scrollPaneContent);
			scrollPaneContent.setContent(content);

			GridPane.setVgrow(scrollPaneContent, Priority.ALWAYS);
			GridPane.setHgrow(scrollPaneContent, Priority.ALWAYS);

			GridPane.setFillHeight(scrollPaneContent, true);

			main.add(scrollPaneContent, 0, 0);
		} else {
			main.add(content, 0, 0);

			GridPane.setVgrow(content, Priority.ALWAYS);
			GridPane.setHgrow(content, Priority.ALWAYS);
		}

		main.add(getButtons(), 0, 1);

		Scene scene = new Scene(main);
		initScene(scene);

		dialog.setScene(scene);
	}

	private void initComponents() {
		buttonOk = new Button(LCommon.get("OK"));
		buttonCancel = new Button(LCommon.get("CANCEL"));

		if (isButtonYes) {
			buttonOk.setText(LCommon.get("YES"));
		}
		if (textButtonCancel != null) {
			buttonCancel.setText(textButtonCancel);
		}
		if (textButtonOk != null) {
			buttonOk.setText(textButtonOk);
		}

		if (buttonExtra != null) {
			buttonExtra.setMinWidth(BUTTONS_MIN_WIDTH);
		}

		buttonOk.setMinWidth(BUTTONS_MIN_WIDTH);
		buttonCancel.setMinWidth(BUTTONS_MIN_WIDTH);

		buttonOk.setDefaultButton(true);
	}

	/**
	 * Buttons bar
	 */
	private Node getButtons() {
		int xButtonOk = 1;
		int xButtonCancel = 2;

		int xButtonExtra = -1;

		if (buttonExtra != null) {
			xButtonExtra = 2;
			xButtonCancel = 3;
		}

		if (AppBasic.useMacButtonOrder) {
			xButtonOk = 2;
			xButtonCancel = 1;

			if (buttonExtra != null) {
				xButtonExtra = 2;
				xButtonOk = 3;
			}
		}

		Label labelPush = new Label();
		GridPane.setHgrow(labelPush, Priority.ALWAYS);

		GridPane buttonPane = new GridPane();
		buttonPane.setPadding(PADDING_BUTTONS);
		buttonPane.setHgap(5);

		// Push
		buttonPane.add(labelPush, 0, 0);

		// Ok
		buttonPane.add(buttonOk, xButtonOk, 0);

		// Extra
		if (buttonExtra != null) {
			buttonPane.add(buttonExtra, xButtonExtra, 0);
		}

		// Cancel
		if (hasButtonCancel) {
			buttonPane.add(buttonCancel, xButtonCancel, 0);
		}

		return buttonPane;
	}

	private void initActions() {
		buttonOk.setOnAction((ActionEvent event) -> {
			boolean close = okPressed();

			if (callback != null) {
				callback.onDialogConfirm();
			}

			if (close) {
				okPressed = true;
				hideNow();
			}
		});
		buttonCancel.setOnAction((ActionEvent event) -> {
			onCancel();

			hideNow();
		});

	}

	private void initScene(Scene scene) {
		// Icon
		dialog.getIcons().add(APP_ICON_IMAGE);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					dialog.close();
				}
			}
		});

		// Font size
		if (FontSize.fontSizeFile != null) {
			FxCssUtils.addCss(FontSize.class, scene, FontSize.fontSizeFile);
		}
	}

	private void restoreSize() {
		// Open
	}

	public boolean open() {
		if (minWidthOnOpen != -1 || minHeightOnOpen != -1) {

			FxDialogBase.setMinWidth(dialog, minWidthOnOpen);
			FxDialogBase.setMinHeight(dialog, minHeightOnOpen);

			FxDialogBase.setWidth(dialog, minWidthOnOpen);
			FxDialogBase.setHeight(dialog, minHeightOnOpen);
		}

		if (calculatedSizeAsMinSize) {
			// Set calculated size as minimum size

			Platform.runLater(() -> {
				double currWidth = dialog.getWidth();
				double currHeight = dialog.getHeight();

				boolean validWidth = (currWidth != Double.NaN && currWidth > MIN_WIDTH);
				boolean validHeight = (currHeight != Double.NaN && currHeight > MIN_HEIGHT);

				if (validWidth) {
					FxDialogBase.setMinWidth(dialog, currWidth);
				}
				if (validHeight) {
					FxDialogBase.setMinHeight(dialog, currHeight);
				}
			});
		}

		if (modal) {
			dialog.showAndWait();
		} else {
			// Mac: Menu entry remains marked

			dialog.show();
		}

		return okPressed;
	}

	protected void hideNow() {
		dialog.hide();
	}

	public void setAddScrollPane(boolean addScrollPane) {
		this.addScrollPane = addScrollPane;
	}

	public void setCallback(IDialogCallback callback) {
		this.callback = callback;
	}

	protected String getValueNotNull(TextInputControl field) {
		String value = field.getText();
		if (value == null) {
			return "";
		}

		value = value.trim();

		return value;
	}

	public Stage getDialog() {
		return dialog;
	}

	public static void setBold(Node... nodes) {
		for (Node node : nodes) {
			FxStyleUtils.addStyle(node, "-fx-font-weight: bold;");
		}
	}

	public static void setMonospace(Node... nodes) {
		for (Node node : nodes) {
			FxStyleUtils.addStyle(node, "-fx-font-family: monospace;");
		}
	}

	public static void setSize(int size, Node... nodes) {
		for (Node node : nodes) {
			FxStyleUtils.addStyle(node, "-fx-font-size: " + size + ";");
		}
	}

	public static void setDefaultTextFieldWidth(TextField... textFields) {
		for (TextField field : textFields) {
			field.setMinWidth(TEXTFIELD_WIDTH_MIN);
		}
	}
}
