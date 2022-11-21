package script.util;

public class Two<T extends Object, S extends Object> {

    T obj1;
    S obj2;

    public T getFirst() {
        return obj1;
    }

    public S getSecond() {
        return obj2;
    }

    public boolean hasFirst() {
        return obj1 != null;
    }

    public boolean hasSecond() {
        return obj2 != null;
    }

    public boolean hasContent() {
        return hasFirst() || hasSecond();
    }

    public static <T extends Object, S extends Object> Two from(T obj1, S obj2) {
        Two multiple = new Two();

        multiple.obj1 = obj1;
        multiple.obj2 = obj2;

        return multiple;
    }

}
