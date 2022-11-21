package script.config.persist;

import java.util.List;

public class ConfigPersistCommon {

	public static String persistStringList(List<String> values) {
		return String.join(";", values);
	}
}
