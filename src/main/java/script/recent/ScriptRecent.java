package script.recent;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScriptRecent {

    private File file;
    private Date time;

    private List<ScriptValue> values = new ArrayList();

    public ScriptRecent(String filepath) {
        this.file = new File(filepath);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<ScriptValue> getValues() {
        return values;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ScriptValue getForIndex(int index) {
        for(ScriptValue value : values) {
            if (value.getIndex() == index) {
                return value;
            }
        }

        return null;
    }

    public String getValuesAsString() {
        StringBuilder sb = new StringBuilder();

        for(ScriptValue value : values) {
            sb.append("#");
            sb.append(value.getIndex());
            sb.append(" = \"");
            sb.append(value.getStringValue());
            sb.append("\"");
        }

        return sb.toString();
    }

    public boolean equalsContent(ScriptRecent other) {
        if (!other.getFile().equals(file)) {
            return false;
        }

        List<ScriptValue> values = getValues();
        List<ScriptValue> valuesOther = other.getValues();

        if (valuesOther.size() != values.size()) {
            return false;
        }

        return other.getValues().equals(getValues());
    }

    public int getCharacterCount() {
        int filePath = file.getAbsolutePath().length();

        int result = 0;
        result += filePath;

        for(ScriptValue value : values) {
            String stringValue = value.getStringValue();
            if (stringValue != null) {
                result += stringValue.length();
            }
        }

        return result;
    }
}
