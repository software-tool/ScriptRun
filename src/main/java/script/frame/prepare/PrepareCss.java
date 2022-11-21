package script.frame.prepare;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.anchorage.docks.containers.common.AnchorageSettings;
import com.rwu.application.config.AppConfig;
import com.rwu.fx.fontsize.FontSize;
import com.rwu.fx.tabsize.TabSize;
import com.rwu.misc.StringUtils;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.StyleManager;

import javafx.scene.Scene;
import script.config.ConfigConstants;
import script.frame.code.editor.EditorFontSize;
import script.frame.css.CssStyles;
import script.frame.css.FxCssUtils;

public class PrepareCss {

	private static List<String> variableStylesheetsAdded = new ArrayList<>();

	private static final String COLOR_GREEN = "script/frame/css/color_green.css";
	private static final String COLOR_BLUE = "script/frame/css/color_blue.css";
	private static final String COLOR_DARK_GREEN = "script/frame/css/color_dark_green.css";

	public static void applyCss(Scene scene) {
		PlatformImpl.setDefaultPlatformUserAgentStylesheet();

		StyleManager.getInstance().addUserAgentStylesheet("AnchorFX.css");
		StyleManager.getInstance().addUserAgentStylesheet("script/frame/css/application.css");

		String colorScheme = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.COLOR_SCHEME);
		applyColorScheme(colorScheme);

		// Reset
		FxCssUtils.clearCss(scene);

		// Basic
		initCssBasic(scene);

		// Font
		initCssFont(scene);

		// Smooth
		initCssSmooth(scene);
	}

	public static void applyColorScheme(String scheme) {
		if ("green".equals(scheme) || scheme == null) {
			StyleManager.getInstance().addUserAgentStylesheet(COLOR_GREEN);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_BLUE);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_DARK_GREEN);
		}

		if ("blue".equals(scheme)) {
			StyleManager.getInstance().addUserAgentStylesheet(COLOR_BLUE);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_GREEN);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_DARK_GREEN);
		}

		if ("dark_green".equals(scheme)) {
			StyleManager.getInstance().addUserAgentStylesheet(COLOR_DARK_GREEN);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_BLUE);
			StyleManager.getInstance().removeUserAgentStylesheet(COLOR_GREEN);
		}
	}

	private static void initCssBasic(Scene scene) {
		// Dockzones
		URL resource = CssStyles.class.getResource("AnchorFX_dockzone.css");
		AnchorageSettings.DOCK_ZONES_CSS = resource;

		// CSS general
		//FxCssUtils.addCss(CssStyles.class, scene, "application.css");

		// AnchorFX
		//FxCssUtils.addCss(CssStyles.class, scene, "AnchorFX.css");
	}

	private static void initCssFont(Scene scene) {
		// Reset
		unsetCustomCss();

		// Font size
		String fontSize = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.INTERFACE_FONT_SIZE);
		if (fontSize == null) {
			fontSize = "100";
		}

		if (StringUtils.isValue(fontSize)) {
			String filename = "font-size-" + fontSize + ".css";

			FontSize.fontSizeFile = filename;
			FxCssUtils.addCss(FontSize.class, variableStylesheetsAdded, filename);
		}

		// Tab size
		String tabWidth = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_TAB_SIZE);
		if (tabWidth != null) {
			String filename = "tab-size-" + tabWidth + ".css";
			FxCssUtils.addCss(TabSize.class, variableStylesheetsAdded, filename);
		}

		// Editor
		String editorFontSize = AppConfig.getValue(ConfigConstants.BASIC, ConfigConstants.EDITOR_FONT_SIZE);
		if (editorFontSize == null) {
			editorFontSize = "16";
		}

		String filename = "font-size-" + editorFontSize + ".css";
		FxCssUtils.addCss(EditorFontSize.class, variableStylesheetsAdded, filename);
	}

	private static void unsetCustomCss() {
		for (String css : variableStylesheetsAdded) {
			FxCssUtils.removeCss(css);
		}
	}

	private static void initCssSmooth(Scene scene) {
		//		boolean smoothBasic = Config.getBooleanProperty(ConfigConstants.SMOOTH_BASIC, false);
		//		boolean smoothFields = Config.getBooleanProperty(ConfigConstants.SMOOTH_FIELDS, false);
		//
		//		boolean smoothTree = Config.getBooleanProperty(ConfigConstants.SMOOTH_TREE, false);
		//		boolean smoothText = Config.getBooleanProperty(ConfigConstants.SMOOTH_TEXT, false);
		//
		//		if (smoothBasic) {
		//			FxCssUtils.addCss(CssStyles.class, scene, "smooth_basic.css");
		//		}
		//		if (smoothFields) {
		//			FxCssUtils.addCss(CssStyles.class, scene, "smooth_fields.css");
		//		}
		//
		//		if (smoothTree) {
		//			FxCssUtils.addCss(CssStyles.class, scene, "smooth_tree.css");
		//		}
		//		if (smoothText) {
		//			FxCssUtils.addCss(CssStyles.class, scene, "smooth_text.css");
		//		}
	}
}
