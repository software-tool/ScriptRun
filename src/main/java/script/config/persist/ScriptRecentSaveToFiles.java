package script.config.persist;

import com.rwu.log.Log;
import script.app.App;
import script.recent.ScriptRecentManager;
import script.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptRecentSaveToFiles {

    private static String PREFIX_EXECUTION = "recent_";
    private static String ENDING = ".json";

    public static void saveList(List<String> storeStrings) {
        if (!App.getScriptRecentDir().exists() && !storeStrings.isEmpty()) {
            App.getScriptRecentDir().mkdirs();
        }

        for(int i=0; i < ScriptRecentManager.MAX_COUNT_READ_AND_WRITE; i++) {
            int fileIndex = i + 1;
            File file = new File(App.getScriptRecentDir(), PREFIX_EXECUTION + fileIndex + ENDING);

            boolean hasValue = storeStrings.size() > i;
            if (hasValue) {
                String value = storeStrings.get(i);

                try {
                    FileUtils.writeToFile(file, value);
                } catch (IOException e) {
                    Log.warn("Failed to write to file: " + file.getAbsolutePath(), e);
                }
            } else {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public static List<String> readList() {
        List<String> result = new ArrayList<>();
        if (!App.getScriptRecentDir().exists()) {
            return result;
        }

        for (int i = 0; i < ScriptRecentManager.MAX_COUNT; i++) {
            int fileIndex = i + 1;

            File file = new File(App.getScriptRecentDir(), PREFIX_EXECUTION + fileIndex + ENDING);
            if (file.exists()) {
                try {
                    String content = FileUtils.readFile(file);
                    result.add(content);
                } catch (IOException e) {
                    Log.warn("Failed to read from file: " + file.getAbsolutePath(), e);
                }
            }
        }

        return result;
    }
}
