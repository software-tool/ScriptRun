package script.frame.execute;

import script.data.Script;
import script.execute.intf.IScriptResultListener;
import script.frame.intf.IContent;
import script.frame.pages.PageTypeEnum;

public interface IExecutePane extends IContent, IScriptResultListener {

    // Actions

    void triggerRunScript();

    void triggerStopScript();

    void applyScriptFinished(int id);

    // Infos

    boolean hasRunningScript();

    boolean equalsExecution(PageTypeEnum pageType, Script scriptOther);

    PageTypeEnum getPageType();

    Script getScript();

    String getContentTitle();

    long getStartTime();

}
