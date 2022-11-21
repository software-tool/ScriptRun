package script.repeat;

import java.util.HashMap;
import java.util.Map;

import com.rwu.misc.IntUtils;

public class RepeatUtils {

	private static Map<String, Integer> parsed = new HashMap<>();

	public static int parseInterval(String intervalStr) {
		if (intervalStr == null) {
			return -1;
		}

		int result = -1;

		Integer existing = parsed.get(intervalStr);
		if (existing != null) {
			return existing;
		}

		if (result == -1 && intervalStr.contains("m")) {
			// Minutes

			Integer parsed = IntUtils.parse(prepareNumber(intervalStr), null);
			if (parsed != null) {
				result = parsed.intValue() * 60;
			}
		}

		if (result == -1 && intervalStr.contains("h")) {
			// Hours

			Integer parsed = IntUtils.parse(prepareNumber(intervalStr), null);
			if (parsed != null) {
				result = parsed.intValue() * 60 * 60;
			}
		}

		// Seconds

		if (result == -1) {
			Integer parsed = IntUtils.parse(prepareNumber(intervalStr), null);
			if (parsed != null) {
				result = parsed.intValue();
			}
		}

		if (result != -1) {
			// Save

			parsed.put(intervalStr, result);
		}

		return result;
	}

	private static String prepareNumber(String str) {
		str = str.replace("m", "");
		str = str.replace("s", "");
		str = str.replace("h", "");
		str = str.replace(" ", "");

		return str;
	}
}
