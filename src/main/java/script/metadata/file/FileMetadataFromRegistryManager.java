package script.metadata.file;

import com.rwu.application.config.AppConfigUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import script.config.ConfigConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Store some metadata in the registry
 *
 * (currently deactivated)
 */
@Deprecated
public class FileMetadataFromRegistryManager {

    private static int LIST_MAX_SIZE = 500;

    public static boolean REGISTRY_STORAGE_DEACTIVATED = true;

    // Values
    private static String TITLE = "title";
    private static String DESCRIPTION = "description";

    @Deprecated
    private static String INPUTS = "inputs";

    private static String PATHS = "paths";

    private static String REPEAT_SECONDS = "repeat_seconds";

    private static List<FileMetadata> metadataEntries = new ArrayList<>();

    public static FileMetadata get(String path) {
        for (FileMetadata custom : metadataEntries) {
            if (custom.hasPath(path)) {
                return custom;
            }
        }

        return null;
    }

    /**
     * Init Metadata to write values
     */
    public static FileMetadata getOrCreate(String path) {
        FileMetadata custom = get(path);
        if (custom == null) {
            custom = new FileMetadata(Arrays.asList(path));
            metadataEntries.add(custom);
        }

        return custom;
    }

    public static void read() {
        if (REGISTRY_STORAGE_DEACTIVATED) return;

        List<String> list = AppConfigUtil.readList(ConfigConstants.METADATA, ConfigConstants.CUSTOM_FILE + "_", LIST_MAX_SIZE, true);
        for (String value : list) {
            // Read
            JSONObject parsed = new JSONObject(value);

            List<String> pathList = new ArrayList<>();

            if (parsed.has(PATHS)) {
                JSONArray paths = parsed.getJSONArray(PATHS);
                for (Object path : paths) {
                    //System.out.println("path: " + path + ", " + path.getClass());

                    if (path instanceof String) {
                        pathList.add((String) path);
                    }
                }
            }

            // Repeat

            int repeatSeconds = 10;
            if (parsed.has(REPEAT_SECONDS)) {
                repeatSeconds = parsed.getInt(REPEAT_SECONDS);
            }

            FileMetadata custom = new FileMetadata(pathList);
            custom.setRepeatSeconds(repeatSeconds);

            // Title
            if (parsed.has(TITLE)) {
                String title = parsed.get(TITLE) + "";
                custom.setTitle(title);
            }

            // Inputs
            // Do not read inputs from registry now
            /*if (parsed.has(INPUTS)) {
                JSONArray arr = parsed.getJSONArray(INPUTS);

                custom.setInputs(InputPersistence.parse(arr));
            }*/

            metadataEntries.add(custom);
        }
    }

    public static void store() {
        if (REGISTRY_STORAGE_DEACTIVATED) return;

        List<String> toStore = new ArrayList<>();
        for (FileMetadata custom : metadataEntries) {
            JSONObject obj = new JSONObject();

            // Paths

            JSONArray paths = new JSONArray();

            List<String> pathList = custom.getPaths();
            for (String path : pathList) {
                paths.put(path);
            }
            obj.put(PATHS, paths);

            // Repeat

            int repeatSeconds = custom.getRepeatSeconds();
            if (repeatSeconds != 10) {
                obj.put(REPEAT_SECONDS, repeatSeconds);
            }

            // Title

            String title = custom.getTitle();
            obj.put(TITLE, title);

            // Inputs

            //JSONArray inputsArr = InputPersistence.toJson(custom.getInputs());
            //obj.put(INPUTS, inputsArr);

            String storeStr = obj.toString();
            toStore.add(storeStr);
        }

        AppConfigUtil.storeList(ConfigConstants.METADATA, ConfigConstants.CUSTOM_FILE + "_", toStore, LIST_MAX_SIZE, true);
    }
}
