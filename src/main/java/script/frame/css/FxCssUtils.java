package script.frame.css;

import java.util.List;

import com.sun.javafx.css.StyleManager;

import javafx.scene.Scene;

public class FxCssUtils {

	public static void clearCss(Scene scene) {
		scene.getStylesheets().clear();
	}

	public static void addCss(Class<?> clazz, List<String> list, String filename) {
		String packageName = clazz.getPackageName();
		packageName = packageName.replace(".", "/");

		String fullPath = packageName + "/" + filename;
		StyleManager.getInstance().addUserAgentStylesheet(fullPath);
	}

	public static void removeCss(String cssPath) {
		StyleManager.getInstance().removeUserAgentStylesheet(cssPath);
	}
}
