package script.metadata.file.reader;

import com.rwu.log.Log;
import com.rwu.misc.FileIOUtils;
import script.input.InputConfig;
import script.input.InputModeEnum;
import script.metadata.file.FileMetadata;
import script.metadata.file.input.InputPersistence;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileMetadataReader {

    private static final String KEYWORD_TITLE = "title";
    private static final String KEYWORD_DESCRIPTION = "description";

    private static final String KEYWORD_FIELD = "input";

    private static final String KEYWORD_SELECTION_ITEM = "item";

    private List<String> lines;
    private String filepath;

    private FileMetadata fileMetadata;

    public static FileMetadata read(File file) {
        List<String> lines = FileIOUtils.getFileAsArray(file, "UTF-8");
        return new FileMetadataReader(lines, file.getAbsolutePath()).read();
    }

    public static FileMetadata read(List<String> lines, String filepath) {
        return new FileMetadataReader(lines, filepath).read();
    }

    public FileMetadataReader(List<String> lines, String filepath) {
        this.lines = lines;
        this.filepath = filepath;
    }

    public FileMetadata read() {
        fileMetadata = new FileMetadata(Arrays.asList(filepath));

        for(String line : lines) {
            line = line.trim();

            boolean stop = visitLine(line);
            if (stop) {
                break;
            }
        }

        if (fileMetadata.hasContent()) {
            return fileMetadata;
        }

        return null;
    }

    private boolean visitLine(String line) {
        if (!line.startsWith("//") && !line.isEmpty()) {
            return true;
        }
        if (line.length() < 3) {
            return false;
        }

        String payload = line.substring(2).trim();

        String[] parts = payload.split(";");
        if (parts.length == 0) {
            return false;
        }

        String main = parts[0];
        readMain(main, parts);

        return false;
    }

    private void readMain(String main, String[] parts) {
        int pos = main.indexOf(":");
        if (pos == -1) {
            return;
        }

        String keyword = main.substring(0, pos).trim();
        String value = main.substring(pos + 1).trim();

        //Log.tmp("Found: '" + keyword + "', '" + value + "'");

        if (KEYWORD_TITLE.equals(keyword)) {
            fileMetadata.setTitle(value);
        } else if (KEYWORD_DESCRIPTION.equals(keyword)) {
            fileMetadata.setDescription(value);
        } else if (KEYWORD_FIELD.equals(keyword)) {
            applyField(parts);
        }
    }

    private void applyField(String[] parts) {
        String main = parts[0];
        if (main.startsWith("input:")) {
            main = main.substring("input:".length());
        }

        InputConfig inputConfig = new InputConfig();
        fileMetadata.getInputs().add(inputConfig);

        applyFieldSetting(main, inputConfig);

        for(int i=1; i<parts.length; i++) {
            String part = parts[i];

            applyFieldSetting(part, inputConfig);
        }
    }

    private static void applyFieldSetting(String part, InputConfig inputConfig) {
        String[] fieldParts = part.split("=");

        String keyword = fieldParts[0].trim();
        String value = null;

        if (fieldParts.length > 1) {
            value = fieldParts[1].trim();
        }

        //Log.tmp(" Field setting: '" + keyword + "', '" + value + "'");

        if ("mandatory".equals(keyword)) {
            inputConfig.setMandatory(true);
        }

        if (value == null) {
            // Abort
            return;
        }

        if ("type".equals(keyword)) {
            InputModeEnum mode = InputModeEnum.fromConfig(value);
            inputConfig.setInputMode(mode);
        } else if ("label".equals(keyword)) {
            inputConfig.setLabel(value);
        } else if ("size".equals(keyword)) {
            inputConfig.setSize(InputPersistence.parse(value));
        } else if ("description".equals(keyword)) {
            inputConfig.setDescription(value);
        } else if (KEYWORD_SELECTION_ITEM.equals(keyword)) {
            String[] valueParts = value.split(",");

            String itemKeyword = valueParts[0];
            String itemText;

            if (valueParts.length > 1) {
                itemText = valueParts[1];
            } else {
                itemText = itemKeyword;
            }

            inputConfig.addSelectionItem(itemKeyword, itemText);
        }
    }
}
