package script.execute;

import com.rwu.log.Log;
import com.rwu.misc.FileIOUtils;
import script.constant.ScriptConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptPreparation {

    private static final String NEWLINE = "\n";

    private StringBuilder sb = new StringBuilder();
    private String content = null;

    private List<String> filesIncluded = new ArrayList();

    public void applyContent(String content) {
        this.content = content;
    }

    public void setFilesIncluded(List<String> included) {
        this.filesIncluded.clear();
        this.filesIncluded.addAll(included);
    }

    public String getScript() {
        sb.setLength(0);

        sb.append(ScriptConstants.STATIC_INPUT);
        sb.append(ScriptConstants.STATIC_OUTPUT);
        sb.append(" ");

        sb.append(content);

        for (String fileIncluded : filesIncluded) {
            File file = new File(fileIncluded);
            if (file.exists() && file.isFile()) {
                String fileContent = null;
                try {
                    fileContent = FileIOUtils.readFile(file);
                } catch (IOException e) {
                    Log.warn("Cannot read include file: " + file.getAbsolutePath() + ", " + e.getMessage());
                }

                if (fileContent != null) {
                    sb.append(NEWLINE);
                    sb.append(NEWLINE);

                    sb.append(fileContent);
                }
            }
        }

        //Log.tmp(sb.toString());

        return sb.toString();
    }
}
