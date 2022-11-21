package script.config.persist;

import org.json.JSONArray;
import org.json.JSONObject;

import script.config.entities.DirectoryConfig;
import script.data.ScriptDirectory;

public class DirectoryConfigPersist {

	// Values
	private static String TAGS = "tags";
	private static String DIRECTORY = "directory";

	private static String COLLAPSED = "collapsed";

	public static String persist(DirectoryConfig config) {
		JSONObject obj = new JSONObject();
		obj.put(TAGS, config.getTags());

		JSONArray arr = new JSONArray();

		for (String tag : config.getTags()) {
			arr.put(tag);
		}

		obj.put(TAGS, arr);

		obj.put(DIRECTORY, config.getDirectory().getPath());

		obj.put(COLLAPSED, config.isCollapsed());

		String objStr = obj.toString();
		return objStr;
	}

	public static DirectoryConfig read(String json) {
		JSONObject groupObj = new JSONObject(json);

		String directory = groupObj.getString(DIRECTORY);
		JSONArray tagsArr = groupObj.getJSONArray(TAGS);

		boolean collapsed = false;
		if (groupObj.has(COLLAPSED)) {
			collapsed = groupObj.getBoolean(COLLAPSED);
		}

		DirectoryConfig config = new DirectoryConfig(new ScriptDirectory(directory));
		config.setCollapsed(collapsed);

		tagsArr.forEach((e) -> {
			String value = (String) e;

			config.getTags().add(value);
		});

		return config;
	}
}
