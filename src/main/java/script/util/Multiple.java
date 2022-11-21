package script.util;

public class Multiple<T extends Object, S extends Object, O extends Object> {

    T obj1;
    S obj2;
    O obj3;

    public T getFirst() {
        return obj1;
    }

    public S getSecond() {
        return obj2;
    }

    public O getThird() {
        return obj3;
    }

    public static <T extends Object, S extends Object> Multiple from(T obj1, S obj2) {
        Multiple multiple = new Multiple();

        multiple.obj1 = obj1;
        multiple.obj2 = obj2;

        return multiple;
    }

    public static <T extends Object, S extends Object, O extends Object> Multiple from(T obj1, S obj2, O obj3) {
        Multiple multiple = new Multiple();

        multiple.obj1 = obj1;
        multiple.obj2 = obj2;
        multiple.obj3 = obj3;

        return multiple;
    }
}
