package script.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListsTest {

    @Test
    void testLast() {
        List<String> small = new ArrayList<>();
        List<String> large = new ArrayList<>();
        List<String> empty = new ArrayList<>();

        small.addAll(Arrays.asList("aa", "bb"));
        large.addAll(Arrays.asList("aa", "bb", "cc", "dd", "ee"));

        Assertions.assertEquals(0, Lists.last(empty, 1).size());

        Assertions.assertEquals(1, Lists.last(small, 1).size());
        Assertions.assertEquals(2, Lists.last(small, 2).size());
        Assertions.assertEquals(2, Lists.last(small, 3).size());

        Assertions.assertEquals(4, Lists.last(large, 4).size());
        Assertions.assertEquals(5, Lists.last(large, 5).size());
    }
}
