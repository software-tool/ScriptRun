package com.rwu.fx.alerts;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.rwu.fx.window.AppWindowFx;
import com.rwu.log.Log;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AlertsBase {

	public static void openInformation(Stage stage, String title, String message) {
		showMessageDialog(stage, AlertType.INFORMATION, title, null, Arrays.asList(message));
	}

	public static void openInformation(Stage stage, String title, String headerText, String message) {
		showMessageDialog(stage, AlertType.INFORMATION, title, headerText, Arrays.asList(message));
	}

	public static ButtonType showConfirmYesNoCancel(Window stage, String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showQuestionDialog(stage, title, mainMessage, secondaryMessage, false, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		return option;
	}

	public static ButtonType showConfirmYesNo(Window stage, String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showQuestionDialog(stage, title, mainMessage, secondaryMessage, false, ButtonType.YES, ButtonType.NO);
		return option;
	}

	/**
	 * Confirm with Yes, No
	 */
	public static Boolean showConfirmDialogYesNo(Window stage, String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showConfirmYesNo(stage, mainMessage, secondaryMessage, title);

		if (option == ButtonType.CANCEL) {
			return null;
		}

		if (option == ButtonType.YES) {
			return true;
		}

		return false;
	}

	/**
	 * Confirm with Yes, No, Cancel
	 */
	public static Boolean showConfigDialogYesNo(Window stage, String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showConfirmYesNoCancel(stage, mainMessage, secondaryMessage, title);

		if (option == ButtonType.CANCEL) {
			return null;
		}

		if (option == ButtonType.YES) {
			return true;
		}

		return false;
	}

	private static void showMessageDialog(Window stage, AlertType alertType, String title, String headerText, List<String> messages) {
		final Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.initOwner(stage);

		// applyMainWindowIcon(alert);

		StringBuilder sb = new StringBuilder();
		if (messages.size() == 1) {
			sb.append(messages.get(0));
		} else {
			for (String message : messages) {
				sb.append(message);
				sb.append("\n");
			}
		}

		alert.setHeaderText(headerText);
		alert.setContentText(sb.toString());

		// if (alertType == AlertType.ERROR) {
		// LogHandler.warn("Error occurred: " + sb.toString());
		// }

		alert.showAndWait();
	}

	private static ButtonType showQuestionDialog(Window stage, String title, String subtitle, String text, boolean cancelIsDefaultButton, ButtonType... buttonTypes) {
		final Alert alert = new Alert(AlertType.CONFIRMATION, null, buttonTypes);
		alert.setTitle(title);
		alert.setHeaderText(subtitle);
		alert.setContentText(text);
		alert.initOwner(stage);

		// applyMainWindowIcon(alert);

		// Ok is Default-Button automatically
		if (cancelIsDefaultButton) {
			setDefaultButton(alert, ButtonType.CANCEL);
		}

		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

	private static void applyMainWindowIcon(Alert alert) {
		Stage stageParent = AppWindowFx.getStage();
		if (stageParent == null) {
			Log.warn("Main window not available");
			return;
		}

		ObservableList<Image> icons = stageParent.getIcons();

		if (icons.isEmpty()) {
			Log.warn("No icon in main window");
			return;
		}

		Stage alertWindow = (Stage) alert.getDialogPane().getScene().getWindow();
		alertWindow.getIcons().add(icons.get(0));
	}

	public static void applyMainWindowIcon(Stage stage) {
		Stage stageParent = AppWindowFx.getStage();
		if (stageParent == null) {
			Log.warn("Main window not available");
			return;
		}

		ObservableList<Image> icons = stageParent.getIcons();
		if (icons.isEmpty()) {
			Log.warn("No icon in main window");
			return;
		}

		stage.getIcons().add(icons.get(0));
	}

	private static Alert setDefaultButton(Alert alert, ButtonType defBtn) {
		DialogPane pane = alert.getDialogPane();
		for (ButtonType t : alert.getButtonTypes()) {
			((javafx.scene.control.Button) pane.lookupButton(t)).setDefaultButton(t == defBtn);
		}
		return alert;
	}
}
