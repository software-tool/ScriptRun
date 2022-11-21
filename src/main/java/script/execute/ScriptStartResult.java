package script.execute;

import script.execute.result.ScriptRunning;
import script.recent.ScriptRecent;

public class ScriptStartResult {

    private ScriptRunning scriptRunning;

    private ScriptRecent scriptRecent;

    public ScriptStartResult(ScriptRunning scriptRunning, ScriptRecent scriptRecent) {
        this.scriptRunning = scriptRunning;
        this.scriptRecent = scriptRecent;
    }

    public ScriptRunning getScriptRunning() {
        return scriptRunning;
    }

    public ScriptRecent getScriptRecent() {
        return scriptRecent;
    }
}
