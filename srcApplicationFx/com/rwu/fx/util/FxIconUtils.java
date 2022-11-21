package com.rwu.fx.util;

import com.rwu.log.Log;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class FxIconUtils {

	public static int ICON_SIZE_MEDIUM = 20;
	public static int ICON_SIZE_SMALL = 16;

	private static Class<?> imagesClass;

	public static void initImagesClass(Class<?> imagesClassNew) {
		imagesClass = imagesClassNew;
	}

	public static void setIcon(Button button, String filename) {
		setIconNoStyle(button, filename);

		setButtonStyle(button);
	}

	public static void setIconNoStyle(Button button, String filename) {
		if (filename == null) {
			button.setGraphic(null);
		} else {
			InputStream resourceAsStream = imagesClass.getResourceAsStream(filename);
			if (resourceAsStream == null) {
				Log.warn("Cannot laod icon from file: " + filename);
			}

			button.setGraphic(new ImageView(new Image(resourceAsStream)));
		}
	}

	public static void setIconNoStyleWithSize(ButtonBase button, String filename) {
		setIconNoStyleWithSize(button, filename, ICON_SIZE_SMALL);
	}

	public static void setIconNoStyleWithSize(ButtonBase button, String filename, int size) {
		if (filename == null) {
			button.setGraphic(null);
		} else {
			InputStream resourceAsStream = imagesClass.getResourceAsStream(filename);
			if (resourceAsStream == null) {
				Log.warn("Cannot laod icon from file: " + filename);
			}

			ImageView view = new ImageView(new Image(resourceAsStream));
			view.setFitHeight(size);
			view.setFitWidth(size);

			button.setGraphic(view);

			ObservableList<String> classes = button.getStyleClass();
			if (!classes.contains("button_flat")) {
				classes.add("button_flat");
			}
		}
	}

	public static void setIcon(Button button, Image image) {
		button.setGraphic(new ImageView(image));

		setButtonStyle(button);
	}

	public static void setIcon(ToggleButton button, String filename) {
		button.setGraphic(new ImageView(new Image(imagesClass.getResourceAsStream(filename))));

		//setButtonStyle(button);
	}

	public static void setIcon(MenuItem menuItem, String filename) {
		setIcon(menuItem, imagesClass, filename);
	}

	public static void setIcon(MenuItem menuItem, Class<?> clazz, String filename) {
		menuItem.setGraphic(new ImageView(new Image(clazz.getResourceAsStream(filename))));
	}

	/**
	 * Set style for button
	 *
	 * - No border by default - Background created border
	 *
	 * Source:
	 * https://stackoverflow.com/questions/30680570/javafx-button-border-and-hover?rq=1
	 *
	 * Reference:
	 * https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
	 */
	public static void setButtonStyle(ButtonBase button) {
		button.getStyleClass().add("button_default");

		// -fx-border-style: solid inside;
		// -fx-border-insets: 5;
		// -fx-border-radius: 5;
		// -fx-background-radius: 0;
	}

	public static void setButtonStyleFlat(ButtonBase button) {
		button.getStyleClass().add("button_flat");

		/*
		.button_flat {
    		-fx-background-color: transparent;
		}

		.button_flat:focused {
			-fx-background-color: #bbbbbb;
		}

		.button_flat:pressed {
			-fx-background-color: #bbbbbb;
		}

		.button_flat:hover {
			-fx-background-color: -fx-outer-border;
		}
		 */
	}

	public static ImageView getImageView(String filename) {
		InputStream resourceAsStream = imagesClass.getResourceAsStream(filename);
		if (resourceAsStream == null) {
			Log.warn("Cannot laod image from file: " + filename);
		}

		ImageView view = new ImageView(new Image(resourceAsStream));
		return view;
	}
}
