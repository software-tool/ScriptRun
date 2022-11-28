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
 */
public class FileMetadataFromRegistryManager {

    private static int LIST_MAX_SIZE = 500;

    // Included
    private static String INCLUDES = "includes";

    private static String PATHS = "paths";

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
        List<String> list = AppConfigUtil.readList(ConfigConstants.FILE_METADATA, ConfigConstants.CUSTOM_FILE + "_", LIST_MAX_SIZE, true);
        for (String value : list) {
            // Read
            JSONObject parsed = new JSONObject(value);

            List<String> pathList = new ArrayList<>();
            List<String> includesList = new ArrayList<>();

            if (parsed.has(PATHS)) {
                JSONArray paths = parsed.getJSONArray(PATHS);
                for (Object path : paths) {
                    //System.out.println("path: " + path + ", " + path.getClass());

                    if (path instanceof String) {
                        pathList.add((String) path);
                    }
                }
            }

            if (parsed.has(INCLUDES)) {
                JSONArray includesArr = parsed.getJSONArray(INCLUDES);
                for (Object include : includesArr) {
                    //System.out.println("path: " + path + ", " + path.getClass());

                    if (include instanceof String) {
                        includesList.add((String) include);
                    }
                }
            }

            FileMetadata custom = new FileMetadata(pathList);
            custom.getIncludes().addAll(includesList);

            metadataEntries.add(custom);
        }
    }

    public static void store() {
        List<String> toStore = new ArrayList<>();
        for (FileMetadata custom : metadataEntries) {
            JSONObject obj = new JSONObject();

            // Paths

            JSONArray pathsArr = new JSONArray();
            JSONArray includesArr = new JSONArray();

            List<String> pathList = custom.getPaths();
            for (String path : pathList) {
                pathsArr.put(path);
            }
            obj.put(PATHS, pathsArr);

            // Includes

            List<String> includes = custom.getIncludes();
            for (String include : includes) {
                includesArr.put(include);
            }

            obj.put(INCLUDES, includesArr);

            String storeStr = obj.toString();
            toStore.add(storeStr);
        }

        AppConfigUtil.storeList(ConfigConstants.FILE_METADATA, ConfigConstants.CUSTOM_FILE + "_", toStore, LIST_MAX_SIZE, true);
    }
}
