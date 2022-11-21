package script.frame.state;

import script.data.Script;
import script.frame.details.PaneScriptDetails;
import script.frame.execute.IExecutePane;
import script.frame.pages.PageTypeEnum;
import script.util.Two;

import java.util.List;

public class OpenRequestScript {

    private Script script;

    private PageTypeEnum pageType;

    private List<IExecutePane> executingPanes;
    private List<PaneScriptDetails> hasChangesPanes;

    public OpenRequestScript(Script script, PageTypeEnum pageType) {
        this.script = script;
        this.pageType = pageType;

        analyseForScript();
    }

    private void analyseForScript() {
        // Execution
        executingPanes = ExecutionPanes.getExecutionPanes(pageType, script);

        // Changes
        hasChangesPanes = HasChangesPanes.getHasChanges(script);
    }

    public Two<PaneScriptDetails, IExecutePane> getMostRecentScript() {
        PaneScriptDetails newestWithChanges = null;
        for(PaneScriptDetails details : hasChangesPanes) {
            if (newestWithChanges == null) {
                newestWithChanges = details;
                continue;
            }

            if (newestWithChanges.getLastEditedTime() < details.getLastEditedTime()) {
                newestWithChanges = details;
            }
        }

        IExecutePane mostRecentExecution = null;
        for(IExecutePane executePane : executingPanes) {
            if (executePane.getPageType() != pageType) {
                continue;
            }

            if (mostRecentExecution == null) {
                mostRecentExecution = executePane;
                continue;
            }

            if (mostRecentExecution.getStartTime() < executePane.getStartTime()) {
                mostRecentExecution = executePane;
            }
        }

        // Nothing known
        if (newestWithChanges == null && mostRecentExecution == null) {
            return Two.from(null, null);
        }

        // Only execution
        if (newestWithChanges == null && mostRecentExecution != null) {
            return Two.from(null, mostRecentExecution);
        }

        // Only editing
        if (newestWithChanges != null && mostRecentExecution == null) {
            return Two.from(newestWithChanges, null);
        }

        // Edited newer
        if (newestWithChanges.getLastEditedTime() > mostRecentExecution.getStartTime()) {
            return Two.from(newestWithChanges, null);
        }

        // Execution newer
        return Two.from(null, mostRecentExecution);
    }
}
