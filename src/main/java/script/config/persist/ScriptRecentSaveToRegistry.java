package script.config.persist;

import com.rwu.application.config.AppConfigUtil;
import script.config.ConfigConstants;
import script.recent.ScriptRecentManager;

import java.util.List;

public class ScriptRecentSaveToRegistry {

    // List
    private static String PREFIX_EXECUTION = "recent_";

    public static void saveList(List<String> storeStrings) {
        AppConfigUtil.storeList(ConfigConstants.RECENT, PREFIX_EXECUTION, storeStrings, ScriptRecentManager.MAX_COUNT_READ_AND_WRITE, true);
    }

    public static List<String> readList() {
        List<String> list = AppConfigUtil.readList(ConfigConstants.RECENT, PREFIX_EXECUTION, ScriptRecentManager.MAX_COUNT_READ_AND_WRITE, true);
        return list;
    }
}
