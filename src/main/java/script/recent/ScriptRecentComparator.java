package script.recent;

import java.util.Comparator;

public class ScriptRecentComparator implements Comparator<ScriptRecent> {

    @Override
    public int compare(ScriptRecent o1, ScriptRecent o2) {
        return o1.getTime().compareTo(o2.getTime());
    }

}
