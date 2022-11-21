package com.rwu.fx.util;

import java.io.File;
import java.net.URL;

import com.rwu.log.Log;
import com.sun.javafx.css.StyleManager;

import javafx.scene.Scene;
import xml.LogHandler;

/**
 * CSS reference: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html
 */
public class FxCssUtils {

	public static void clearCss(Scene scene) {
		scene.getStylesheets().clear();
	}

	public static void addCss(Class<?> clazz, Scene scene, String filename) {
		URL resource = clazz.getResource(filename);
		if (resource == null) {
			Log.warn("Cannot add css: " + filename);
			return;
		}

		String externalForm = resource.toExternalForm();
		scene.getStylesheets().add(externalForm);
	}

	public static void addCss(Scene scene, File file) {
		String externalForm = file.toURI().toString();
		scene.getStylesheets().add(externalForm);
	}

	public static void removeCss(Class<?> clazz, Scene scene, String filename) {
		URL resource = clazz.getResource(filename);
		if (resource == null) {
			Log.warn("Cannot add css: " + filename);
			return;
		}

		String externalForm = resource.toExternalForm();
		scene.getStylesheets().remove(externalForm);
	}

	/**
	 * Add global CSS
	 */
	public static void addCss(Class<?> clazz, String filename) {
		String packageName = clazz.getPackageName();
		packageName = packageName.replace(".", "/");

		String fullPath = packageName + "/" + filename;
		StyleManager.getInstance().addUserAgentStylesheet(fullPath);
	}

	/**
	 * Remove global CSS
	 */
	public static void removeCss(String cssPath) {
		StyleManager.getInstance().removeUserAgentStylesheet(cssPath);
	}
}
