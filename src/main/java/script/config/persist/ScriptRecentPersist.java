package script.config.persist;

import com.rwu.log.Log;
import com.rwu.misc.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import script.config.entities.DirectoryConfig;
import script.data.ScriptDirectory;
import script.recent.ScriptRecent;
import script.recent.ScriptValue;

public class ScriptRecentPersist {

	private static String FILE = "file";
	private static String TIME = "time";
	private static String VALUES = "values";

	private static String INDEX = "index";
	private static String STRING_VALUE = "str_value";

	public static ScriptRecent read(String json) {
		JSONObject groupObj = new JSONObject(json);

		String filepath = groupObj.getString(FILE);
		String timeStr = groupObj.getString(TIME);
		JSONArray tagsArr = groupObj.getJSONArray(VALUES);

		ScriptRecent config = new ScriptRecent(filepath);

		config.setTime(DateUtils.parseDateTime(timeStr));

		tagsArr.forEach((e) -> {
			JSONObject obj = (JSONObject) e;

			try {
				ScriptValue parsed = read(obj);
				config.getValues().add(parsed);
			} catch (Exception ex) {
				Log.warn("Failed to parse values: " + obj);
			}
		});

		return config;
	}

	public static String persist(ScriptRecent config, boolean pretty) {
		JSONObject obj = new JSONObject();
		obj.put(FILE, config.getFile().getAbsoluteFile());

		obj.put(TIME, DateUtils.formatDateTime(config.getTime()));

		JSONArray arr = new JSONArray();
		for (ScriptValue scriptValue : config.getValues()) {
			arr.put(persist(scriptValue));
		}
		obj.put(VALUES, arr);

		if (pretty) {
			return obj.toString(2);
		}

		return obj.toString();
	}

	private static ScriptValue read(JSONObject obj) throws JSONException {
		int index = obj.getInt(INDEX);
		String stringValue = null;

		if (obj.has(STRING_VALUE)) {
			stringValue = obj.getString(STRING_VALUE);
		}

		ScriptValue scriptValue = new ScriptValue(index, stringValue);
		return scriptValue;
	}

	private static JSONObject persist(ScriptValue scriptValue) {
		JSONObject obj = new JSONObject();
		obj.put(INDEX, scriptValue.getIndex());
		obj.put(STRING_VALUE, scriptValue.getStringValue());
		return obj;
	}
}
