package script.recent;

import com.rwu.log.Log;
import script.config.persist.ScriptRecentPersist;
import script.config.persist.ScriptRecentSaveToFiles;
import script.config.persist.ScriptRecentSaveToRegistry;
import script.util.MapUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ScriptRecentManager {

    private static List<ScriptRecent> recents = new ArrayList();

    private static ScriptRecentComparator scriptRecentComparator = new ScriptRecentComparator();

    private static int CHARACTER_COUNT_LIMIT_FOR_REGISTRY = 1000;

    public static int MAX_COUNT = 100;

    // Ensure that everything is written
    public static int MAX_COUNT_READ_AND_WRITE = MAX_COUNT + 5;

    public static List<ScriptRecent> getRecents(File file) {
        return recents.stream().filter(recent -> recent.getFile().equals(file)).collect(Collectors.toList());
    }

    public static List<ScriptRecent> getRecents(List<File> file) {
        return recents.stream().filter(recent -> file.contains(recent.getFile())).collect(Collectors.toList());
    }

    public static List<ScriptRecent> getRecentsAll() {
        return recents;
    }

    public static void addRecent(ScriptRecent scriptRecent) {
        removeAllWithEqualContent(scriptRecent);

        while (recents.size() > MAX_COUNT) {
            recents.remove(0);
        }

        recents.add(scriptRecent);

        store();
    }

    public static void removeRecent(ScriptRecent scriptRecent) {
        recents.remove(scriptRecent);

        store();
    }

    public static void read() {
        // Reset
        recents.clear();

        List<String> all = new ArrayList<>();

        List<String> listFromRegistry = ScriptRecentSaveToRegistry.readList();
        List<String> listFromFiles = ScriptRecentSaveToFiles.readList();

        all.addAll(listFromRegistry);
        all.addAll(listFromFiles);

        for (String json : all) {
            try {
                ScriptRecent config = ScriptRecentPersist.read(json);
                recents.add(config);
            } catch (Exception e) {
                Log.warn("Failed to parse ScriptRecent: " + json);
            }
        }

        Collections.sort(recents, scriptRecentComparator);
    }

    public static void store() {
        List<String> registryStrings = new ArrayList<>();
        List<String> fileStrings = new ArrayList<>();

        for (ScriptRecent config : recents) {
            int characterCount = config.getCharacterCount();

            if (characterCount > CHARACTER_COUNT_LIMIT_FOR_REGISTRY) {
                String objStr = ScriptRecentPersist.persist(config, true);
                fileStrings.add(objStr);
            } else {
                String objStr = ScriptRecentPersist.persist(config, false);
                registryStrings.add(objStr);
            }
        }

        ScriptRecentSaveToRegistry.saveList(registryStrings);
        ScriptRecentSaveToFiles.saveList(fileStrings);
    }

    public static List<ScriptRecent> getRecentsLimited(int maximumForFile) {
        List<ScriptRecent> result = new ArrayList();

        List<ScriptRecent> recentsCopy = new ArrayList<>(recents);
        Collections.reverse(recentsCopy);

        Map<File, Integer> counts = new HashMap();

        for(ScriptRecent recent : recentsCopy) {
            Integer count = counts.get(recent.getFile());
            if (count == null) {
                count = 0;
            }

            if (count > maximumForFile) {
                continue;
            }

            boolean exists = hasRecent(result, recent);
            if (!exists) {
                result.add(recent);

                count++;
            }

            counts.put(recent.getFile(), count);
        }

        Collections.reverse(result);

        return result;
    }

    public static Map<File, List<ScriptRecent>> getGroupedByFile(List<ScriptRecent> list) {
        return MapUtils.toMap(entry -> ((ScriptRecent)entry).getFile(), list);
    }

    private static void removeAllWithEqualContent(ScriptRecent scriptRecent) {
        Iterator<ScriptRecent> iterator = recents.iterator();

        while (iterator.hasNext()) {
            ScriptRecent next = iterator.next();
            if (next.equalsContent(scriptRecent)) {
                iterator.remove();
            }
        }
    }

    public static void removeAllOfFile(File file) {
        Iterator<ScriptRecent> iterator = recents.iterator();

        while (iterator.hasNext()) {
            ScriptRecent next = iterator.next();

            if (file.equals(next.getFile())) {
                iterator.remove();
            }
        }
    }

    private static boolean hasRecent(List<ScriptRecent> existing, ScriptRecent toCheck) {
        for(ScriptRecent current : existing) {
            if (current.equalsContent(toCheck)) {
                return true;
            }
        }

        return false;
    }

}
