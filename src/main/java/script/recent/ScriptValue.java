package script.recent;

import java.util.Objects;

public class ScriptValue {

    private int index;
    private String stringValue;

    public ScriptValue(int index, String stringValue) {
        this.index = index;
        this.stringValue = stringValue;
    }

    public int getIndex() {
        return index;
    }

    public String getStringValue() {
        return stringValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptValue that = (ScriptValue) o;
        return index == that.index && Objects.equals(stringValue, that.stringValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, stringValue);
    }
}
