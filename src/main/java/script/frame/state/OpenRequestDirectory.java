package script.frame.state;

import script.data.ScriptDirectory;
import script.frame.directory.PaneDirectory;

import java.util.List;

public class OpenRequestDirectory {

    private ScriptDirectory scriptDirectory;

    private List<PaneDirectory> panesWithChanges;

    public OpenRequestDirectory(ScriptDirectory scriptDirectory) {
        this.scriptDirectory = scriptDirectory;

        analyseForDirectory();
    }

    private void analyseForDirectory() {
        // Edit
        panesWithChanges = HasChangesPanes.getHasChanges(scriptDirectory);
    }

    public PaneDirectory getMostRecent() {
        PaneDirectory newestEditing = null;
        for(PaneDirectory details : panesWithChanges) {
            if (newestEditing == null) {
                newestEditing = details;
                continue;
            }

            if (newestEditing.getLastChangesTime() < details.getLastChangesTime()) {
                newestEditing = details;
            }
        }

        // Execution newer
        return newestEditing;
    }
}
