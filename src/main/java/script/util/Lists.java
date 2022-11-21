package script.util;

import java.util.List;

public class Lists {

    public static <T extends Object> List<T> last(List<T> list, int count) {
        int from = list.size() - count;
        int to = list.size();

        if (from < 0) {
            from = 0;
        }

        return list.subList(from, to);
    }
}
