package script.recent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rwu.application.config.AppConfigUtil;

import script.config.ConfigConstants;

public class RecentFileManager {

	private static int LIST_MAX_SIZE = 50;

	private static List<File> recentEntries = new ArrayList<>();

	public static void addEntry(File entry) {
		// Remove if existing
		while (recentEntries.remove(entry)) {
			// Nothing
		}

		recentEntries.add(entry);

		while (recentEntries.size() > LIST_MAX_SIZE) {
			recentEntries.remove(0);
		}

		storeList();
	}

	public static void removeEntry(File entry) {
		// Remove if existing
		while (recentEntries.remove(entry)) {
			// Nothing
		}

		storeList();
	}

	public static void readList() {
		List<String> list = AppConfigUtil.readList(ConfigConstants.RECENT, ConfigConstants.RECENT_FILE + "_", LIST_MAX_SIZE, true);

		for (String value : list) {
			recentEntries.add(new File(value));
		}
	}

	public static void storeList() {
		List<String> toStore = new ArrayList<>();
		for (File file : recentEntries) {
			toStore.add(file.getAbsolutePath());
		}

		AppConfigUtil.storeList(ConfigConstants.RECENT, ConfigConstants.RECENT_FILE + "_", toStore, LIST_MAX_SIZE, true);
	}

	public static List<File> getRecentEntries() {
		return recentEntries;
	}

}
