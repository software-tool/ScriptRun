package script.frame.state;

import script.frame.details.PaneScriptDetails;

import java.util.ArrayList;
import java.util.List;

public class CodeUnsavedPanes {

    private static List<PaneScriptDetails> scriptsInEditing = new ArrayList<>();

    public synchronized static void add(PaneScriptDetails pane) {
        if (!scriptsInEditing.contains(pane)) {
            scriptsInEditing.add(pane);
        }
    }

    public synchronized static void remove(PaneScriptDetails pane) {
        scriptsInEditing.remove(pane);
    }

    public static List<PaneScriptDetails> getScriptsInEditing() {
        return scriptsInEditing;
    }

}
