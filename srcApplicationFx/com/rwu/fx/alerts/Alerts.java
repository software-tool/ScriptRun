package com.rwu.fx.alerts;

import com.rwu.fx.window.AppWindowFx;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Helper class for dialogs
 *
 * JavaFx-Dialogs: https://code.makery.ch/blog/javafx-dialogs-official/
 */
public class Alerts {

	public static void showError(String message, String title) {
		showErrorDialog(title, Arrays.asList(message));
	}

	public static void showWarning(String message, String title) {
		showWarningDialog(title, Arrays.asList(message));
	}

	public static void showInformation(String message, String title) {
		showInformationDialog(title, Arrays.asList(message));
	}

	/**
	 * Yes, No
	 */
	public static ButtonType showConfirmYesNo(String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showQuestionDialog(title, mainMessage, secondaryMessage, false, ButtonType.YES, ButtonType.NO);
		return option;
	}

	/**
	 * Yes, No, Cancel
	 */
	public static ButtonType showConfirmYesNoCancel(String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showQuestionDialog(title, mainMessage, secondaryMessage, false, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		return option;
	}

	/**
	 * Is Yes: Yes, No, Cancel
	 */
	public static boolean showConfirmYesNoCancelIsYes(String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showConfirmYesNoCancel(mainMessage, secondaryMessage, title);

		return option == ButtonType.YES;
	}

	/**
	 * Is Yes: Yes, No
	 */
	public static boolean showConfirmDialogYesNo(String mainMessage, String secondaryMessage, String title) {
		ButtonType option = showConfirmYesNo(mainMessage, secondaryMessage, title);

		return option == ButtonType.YES;
	}

	/**
	 * If there is a page change (FxBrowser), then it needs to be done in: in Platform.runLater
	 *
	 * If not: alert.showAndWait() throws Exception "showAndWait() not allowed during animation or layout processing"
	 */
	private static ButtonType showQuestionDialog(final String title, final String subtitle, final String text, boolean cancelIsDefaultButton, ButtonType... buttonTypes) {
		final Alert alert = new Alert(AlertType.CONFIRMATION, "aaa", buttonTypes);
		alert.setTitle(title);
		alert.setHeaderText(subtitle);
		alert.setContentText(text);
		initOwner(alert);

		// Ok is default-Button automatically
		if (cancelIsDefaultButton) {
			setDefaultButton(alert, ButtonType.CANCEL);
		}

		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

	private static void showErrorDialog(String title, List<String> messages) {
		showMessageDialog(AlertType.ERROR, title, messages);
	}

	private static void showWarningDialog(String title, List<String> messages) {
		showMessageDialog(AlertType.WARNING, title, messages);
	}

	private static void showInformationDialog(String title, List<String> messages) {
		showMessageDialog(AlertType.INFORMATION, title, messages);
	}

	public static void showMessageDialog(AlertType alertType, String title, List<String> messages) {
		final Alert alert = new Alert(alertType);
		alert.setTitle(title);
		initOwner(alert);

		StringBuilder sb = new StringBuilder();
		if (messages.size() == 1) {
			sb.append(messages.get(0));
		} else {
			sb.append("These errors occurred:\n\n");

			for (String message : messages) {
				sb.append(message);
				sb.append("\n");
			}
		}

		alert.setHeaderText(null);
		alert.setContentText(sb.toString());

		if (alertType == AlertType.ERROR) {
			// No log
			// LogHandler.warn("Error occurred: " + sb.toString());
		}

		alert.showAndWait();
	}

	/**
	 * The effect of InitOwner is that the icon of the other window will be applied
	 */
	private static void initOwner(Alert alert) {
		alert.initOwner(AppWindowFx.getStage());
	}

	private static Alert setDefaultButton(Alert alert, ButtonType defBtn) {
		DialogPane pane = alert.getDialogPane();
		for (ButtonType t : alert.getButtonTypes()) {
			((javafx.scene.control.Button) pane.lookupButton(t)).setDefaultButton(t == defBtn);
		}
		return alert;
	}
}
