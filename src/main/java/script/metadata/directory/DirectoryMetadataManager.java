package script.metadata.directory;

import com.rwu.application.config.AppConfigUtil;
import com.rwu.log.Log;
import com.rwu.misc.EqualsUtil;
import org.json.JSONException;
import org.json.JSONObject;
import script.config.ConfigConstants;

import java.util.ArrayList;
import java.util.List;

public class DirectoryMetadataManager {

	private static int LIST_MAX_SIZE = 100;

	// Values
	private static String PATH = "path";
	private static String SHOW_ALL_FILES = "all_files";
	private static String DISPLAY_MODE = "display";

	private static String TITLE = "title";
	private static String DESCRIPTION = "description";

	private static List<DirectoryMetadata> metadataEntries = new ArrayList<>();

	public static void setTitle(String path, String title) {
		DirectoryMetadata found = getOrCreate(path);

		found.setTitle(title);

		store();
	}

	public static void setDescription(String path, String description) {
		DirectoryMetadata found = getOrCreate(path);

		found.setDescription(description);

		store();
	}

	public static DirectoryMetadata get(String path) {
		for (DirectoryMetadata custom : metadataEntries) {
			if (EqualsUtil.isEqual(custom.getPath(), path)) {
				return custom;
			}
		}

		return null;
	}

	private static DirectoryMetadata getOrCreate(String path) {
		DirectoryMetadata found = get(path);
		if (found == null) {
			found = new DirectoryMetadata(path);
			metadataEntries.add(found);
		}

		return found;
	}

	/**
	 * Alle Konfigurationen lesen
	 */
	public static void read() {
		metadataEntries.clear();

		List<String> list = AppConfigUtil.readList(ConfigConstants.DIRECTORY_METADATA, ConfigConstants.CUSTOM_DIRECTORY + "_", LIST_MAX_SIZE, true);
		for (String value : list) {
			DirectoryMetadata custom = readCustom(value);

			metadataEntries.add(custom);
		}
	}

	private static DirectoryMetadata readCustom(String value) {
		// Read
		try {
			JSONObject parsed = new JSONObject(value);

			String path = parsed.getString(PATH);

			String title = null;
			String description = null;

			boolean showAllFiles = false;
			if (parsed.has(SHOW_ALL_FILES)) {
				showAllFiles = parsed.getBoolean(SHOW_ALL_FILES);
			}

			String displayModeStr = null;
			if (parsed.has(DISPLAY_MODE)) {
				displayModeStr = parsed.getString(DISPLAY_MODE);
			}

			// Title, Description

			if (parsed.has(TITLE)) {
				title = parsed.getString(TITLE);
			}

			if (parsed.has(DESCRIPTION)) {
				description = parsed.getString(DESCRIPTION);
			}

			DirectoryMetadata custom = new DirectoryMetadata(path);
			custom.setShowAllFiles(showAllFiles);
			custom.setDisplayMode(DirectoryDisplayMode.fromConfig(displayModeStr));
			custom.setTitle(title);
			custom.setDescription(description);

			return custom;
		} catch (JSONException e) {
			Log.warn("Failed to read directory configuration", e);

			return null;
		}
	}

	/**
	 * Save all configurations
	 */
	public static void store() {
		List<String> toStore = new ArrayList<>();
		for (DirectoryMetadata custom : metadataEntries) {
			JSONObject obj = new JSONObject();

			String title = custom.getTitle();
			String description = custom.getDescription();

			obj.put(PATH, custom.getPath());

			if (custom.getShowAllFiles()) {
				obj.put(SHOW_ALL_FILES, true);
			}

			DirectoryDisplayMode displayMode = custom.getDisplayMode();
			if (displayMode != null && !displayMode.isLarge()) {
				obj.put(DISPLAY_MODE, DirectoryDisplayMode.toConfig(displayMode));
			}

			// Title, Description

			if (title != null) {
				obj.put(TITLE, title);
			}

			if (description != null) {
				obj.put(DESCRIPTION, description);
			}

			String storeStr = obj.toString();
			toStore.add(storeStr);
		}

		AppConfigUtil.storeList(ConfigConstants.DIRECTORY_METADATA, ConfigConstants.CUSTOM_DIRECTORY + "_", toStore, LIST_MAX_SIZE, true);
	}
}
