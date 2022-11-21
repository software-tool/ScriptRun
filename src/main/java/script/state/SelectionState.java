package script.state;

import java.io.File;

import com.rwu.application.config.AppConfig;

import script.config.ConfigConstants;

public class SelectionState {

	public static File currentDirectory = null;

	public static String currentDirectoryGroup = null;

	public static void userSelectedGroup(String directoryGroup) {
		AppConfig.setValue(ConfigConstants.RECENT, ConfigConstants.RECENT_GROUP, directoryGroup);

		currentDirectoryGroup = directoryGroup;
	}

	public static void userSelectedDirectory(File directory) {
		AppConfig.setValue(ConfigConstants.RECENT, ConfigConstants.RECENT_DIRECTORY, directory.getAbsolutePath());

		currentDirectory = directory;
	}
}
