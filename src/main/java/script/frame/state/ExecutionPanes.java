package script.frame.state;

import script.controller.ControllerPages;
import script.data.Script;
import script.frame.execute.IExecutePane;
import script.frame.pages.PageTypeEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExecutionPanes {

    private static int scriptInstIdCounter = 0;
    private static int currentInstId = -1;

    private static List<IExecutePane> panesExecuting = new ArrayList<>();

    public synchronized static void add(IExecutePane pane) {
        panesExecuting.add(pane);

        ControllerPages.updateExecutions();
    }

    public synchronized static void cleanupExecutions() {
        Iterator<IExecutePane> iterator = panesExecuting.iterator();

        while (iterator.hasNext()) {
            IExecutePane curr = iterator.next();

            if (!curr.hasRunningScript()) {
                iterator.remove();
            }
        }
    }

    public synchronized static List<IExecutePane> getExecutionPanes(PageTypeEnum pageTypeEnum, Script script) {
        List<IExecutePane> result = new ArrayList();

        for(IExecutePane pane : panesExecuting) {
            if (pane.equalsExecution(pageTypeEnum, script)) {
                result.add(pane);
            }
        }

        return result;
    }

    public synchronized static List<IExecutePane> getPanesExecuting() {
        return new ArrayList<>(panesExecuting);
    }

    public synchronized static boolean hasPanesExecuting() {
        return !panesExecuting.isEmpty();
    }

    public synchronized static boolean hasExecuting(PageTypeEnum pageTypeEnum, Script script) {
        List<IExecutePane> found = getExecutionPanes(pageTypeEnum, script);
        return !found.isEmpty();
    }

    public synchronized static int getNextScriptId() {
        cleanupExecutions();

        if (!panesExecuting.isEmpty()) {
            currentInstId = -1;
            return -1;
        }

        scriptInstIdCounter++;

        currentInstId = scriptInstIdCounter;

        return scriptInstIdCounter;
    }

    public synchronized static int getActiveScriptId() {
        return currentInstId;
    }
}
