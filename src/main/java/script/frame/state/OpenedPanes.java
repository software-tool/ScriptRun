package script.frame.state;

import script.frame.details.PaneScriptDetails;

import java.util.ArrayList;
import java.util.List;

public class OpenedPanes {

    private static List<PaneScriptDetails> scriptsOpened = new ArrayList<>();

    public synchronized static void add(PaneScriptDetails pane) {
        if (!scriptsOpened.contains(pane)) {
            scriptsOpened.add(pane);
        }
    }

    public synchronized static void remove(PaneScriptDetails pane) {
        scriptsOpened.remove(pane);
    }

    public static List<PaneScriptDetails> getScriptsOpened() {
        return scriptsOpened;
    }

    public static void tickScriptFilesSavedOutside() {
        for(PaneScriptDetails scriptOpened : scriptsOpened) {
            scriptOpened.checkSavedOutside();
        }
    }
}
