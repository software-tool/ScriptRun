package script.frame.css;

import javafx.scene.control.Label;

public class CssConstants {

	private static String TITLE = "titles";
	private static String SUBTITLE = "subtitles";

	public static String SCRIPT_FILE_TITLE_WRAPPER = "script_directory_file_title_wrapper";
	public static String SCRIPT_FILE_TITLE = "script_directory_file_title";

	public static void setTitle(Label label) {
		label.getStyleClass().add(TITLE);
	}

	public static void setSubtitle(Label label) {
		label.getStyleClass().add(SUBTITLE);
	}
}
