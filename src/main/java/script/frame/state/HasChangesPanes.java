package script.frame.state;

import script.data.Script;
import script.data.ScriptDirectory;
import script.frame.details.PaneScriptDetails;
import script.frame.directory.PaneDirectory;

import java.util.ArrayList;
import java.util.List;

public class HasChangesPanes {

    private static List<PaneScriptDetails> scriptsWithChanges = new ArrayList<>();
    private static List<PaneDirectory> directoriesWithChanges = new ArrayList<>();

    /**
     * Change:
     * - Code changed
     * - Has output
     */
    public static void setHasChanges(PaneScriptDetails paneScriptDetails) {
        if (!scriptsWithChanges.contains(paneScriptDetails)) {
            scriptsWithChanges.add(paneScriptDetails);
        }
    }

    public static void setHasChanges(PaneDirectory paneDirectory) {
        if (!directoriesWithChanges.contains(paneDirectory)) {
            directoriesWithChanges.add(paneDirectory);
        }
    }

    public synchronized static List<PaneScriptDetails> getHasChanges(Script script) {
        List<PaneScriptDetails> result = new ArrayList();

        for(PaneScriptDetails pane : scriptsWithChanges) {
            if (pane.equalsScript(script)) {
                result.add(pane);
            }
        }

        return result;
    }

    public synchronized static List<PaneDirectory> getHasChanges(ScriptDirectory scriptDirectory) {
        List<PaneDirectory> result = new ArrayList();

        for(PaneDirectory pane : directoriesWithChanges) {
            if (pane.equalsDirectory(scriptDirectory)) {
                result.add(pane);
            }
        }

        return result;
    }
}
