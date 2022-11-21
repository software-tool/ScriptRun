package script.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {

    public static <I extends Object, K extends Object> Map<K, List<I>> toMap(Function<Object, K> getKey, List<I> list) {
        Map<K, List<I>> result = new HashMap<>();

        for(I entry : list) {
            K key = getKey.apply(entry);

            List<I> sublist = result.get(key);
            if (sublist == null) {
                sublist = new ArrayList<>();

                result.put(key, sublist);
            }

            sublist.add(entry);
        }

        return result;
    }

}
