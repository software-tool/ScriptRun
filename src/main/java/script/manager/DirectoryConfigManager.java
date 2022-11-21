package script.manager;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.rwu.application.config.AppConfig;
import com.rwu.application.config.AppConfigUtil;
import com.rwu.misc.ListUtils;
import com.rwu.misc.StringUtils;

import script.app.App;
import script.config.ConfigConstants;
import script.config.entities.DirectoryConfig;
import script.config.persist.DirectoryConfigPersist;
import script.data.DirectoryGroup;
import script.data.ScriptDirectory;
import script.state.SelectionState;

public class DirectoryConfigManager {

	private static List<DirectoryConfig> directories = new ArrayList<>();

	private static DirectoryConfigComparator directoryConfigComparator = new DirectoryConfigComparator();

	private static int MAX_GROUPS_COUNT = 100;

	public static String DEFAULT_GROUP_NAME = "Default";
	public static String TAG_ORDER = "tag_order";
	public static String TAG_HIDDEN = "tag_hidden";

	// List
	private static String PREFIX_DIRECTORY = "directory_";

	public static List<ScriptDirectory> getScriptPathes() {
		return directories.stream().map(config -> config.getDirectory()).collect(Collectors.toList());
	}

	public static DirectoryConfig addScriptPath(File directory) {
		DirectoryConfig config = getConfig(directory);
		if (config == null) {
			config = new DirectoryConfig(new ScriptDirectory(directory));

			directories.add(config);
		}

		store();

		return config;
	}

	public static void removeScriptPath(File directory) {
		Iterator<DirectoryConfig> it = directories.iterator();

		while (it.hasNext()) {
			DirectoryConfig current = it.next();

			if (current.getDirectory().getDirectory().equals(directory)) {
				it.remove();
			}
		}

		store();
	}

	public static void store() {
		List<String> storeStrings = new ArrayList<>();

		for (DirectoryConfig config : directories) {
			String objStr = DirectoryConfigPersist.persist(config);
			storeStrings.add(objStr);
		}

		AppConfigUtil.storeList(ConfigConstants.GROUPS, PREFIX_DIRECTORY, storeStrings, MAX_GROUPS_COUNT, true);
	}

	public static void read() {
		// Reset
		directories.clear();

		List<String> list = AppConfigUtil.readList(ConfigConstants.GROUPS, PREFIX_DIRECTORY, MAX_GROUPS_COUNT, true);
		for (String json : list) {
			DirectoryConfig config = DirectoryConfigPersist.read(json);
			directories.add(config);

			// Current group
			if (SelectionState.currentDirectoryGroup == null) {
				//SelectionState.currentDirectoryGroup = groupName;
			}
		}
	}

	public static void setTagOrder(List<String> tags) {
		AppConfig.setValue(ConfigConstants.GROUPS, TAG_ORDER, StringUtils.joinWithSemicolon(tags));
	}

	public static void setTagHidden(List<String> tags) {
		AppConfig.setValue(ConfigConstants.GROUPS, TAG_HIDDEN, StringUtils.joinWithSemicolon(tags));
	}

	public static void hideOthers(String visibleTag) {
		List<String> all = getGroupTitlesSorted();
		all.remove(visibleTag);

		setTagHidden(all);
	}

	public static void removeFromHidden(String group) {
		List<String> groupTitlesHidden = getGroupTitlesHidden();
		groupTitlesHidden.remove(group);

		setTagHidden(groupTitlesHidden);
	}

	public static void removeGroup(String group) {
		for (DirectoryConfig directory : directories) {
			directory.removeTag(group);
		}

		store();
	}

	public static void renameDirectoryGroup(String name, String newName) {
		/*DirectoryGroup found = getGroup(name);
		if (found != null) {
			found.setName(newName);
		}
		
		// Save
		store();*/
	}

	public static List<String> getTags(File directory) {
		DirectoryConfig config = getConfig(directory);
		return config.getTags();
	}

	public static void setTags(File directory, List<String> tags) {
		DirectoryConfig config = getConfig(directory);
		config.setTags(tags);

		// Save
		store();
	}

	private static DirectoryConfig getConfig(File directorySearched) {
		for (DirectoryConfig directory : directories) {
			if (directory.getDirectory().getDirectory().equals(directorySearched)) {
				return directory;
			}
		}

		return null;
	}

	public static void setCollapsed(File directorySearched, boolean collapsed) {
		DirectoryConfig config = getConfig(directorySearched);
		if (config != null) {
			config.setCollapsed(collapsed);
		}

		store();
	}

	public static List<DirectoryGroup> getGroups() {
		List<String> tags = getGroupTitlesSorted();

		Collections.sort(directories, directoryConfigComparator);

		if (directories.isEmpty()) {
			directories.add(new DirectoryConfig(new ScriptDirectory(App.getConfigDir())));
		}

		List<DirectoryGroup> groups = new ArrayList();
		for (String tag : tags) {
			DirectoryGroup tagGroup = new DirectoryGroup(tag);

			for (DirectoryConfig directory : directories) {
				if (directory.showInTag(tag)) {
					tagGroup.getConfigs().add(directory);
				}
			}

			groups.add(tagGroup);
		}

		return groups;
	}

	public static List<String> getGroupTitlesSorted() {
		List<String> tags = new ArrayList();

		boolean hasContentInDefault = false;

		for (DirectoryConfig directory : directories) {
			ListUtils.addIfNotExisting(tags, directory.getTags());

			if (directory.showInTag(DEFAULT_GROUP_NAME)) {
				hasContentInDefault = true;
			}
		}

		if (hasContentInDefault || tags.isEmpty()) {
			if (!tags.contains(DEFAULT_GROUP_NAME)) {
				tags.add(DEFAULT_GROUP_NAME);
			}
		}

		List<String> hidden = getGroupTitlesHidden();

		int visibleTags = 0;
		for(String tag : tags) {
			if (!hidden.contains(tag)) {
				visibleTags++;
			}
		}

		if (visibleTags == 0) {
			removeFromHidden(DEFAULT_GROUP_NAME);
		}

		String tagOrderStr = AppConfig.getValue(ConfigConstants.GROUPS, TAG_ORDER);
		List<String> tagOrder = StringUtils.split(tagOrderStr, ";", true, false);
		ListUtils.sortList(tags, tagOrder);

		return tags;
	}

	public static List<String> getGroupTitlesHidden() {
		String tagOrderStr = AppConfig.getValue(ConfigConstants.GROUPS, TAG_HIDDEN);
		List<String> tagOrder = StringUtils.split(tagOrderStr, ";", true, false);

		return tagOrder;
	}

}
