package script.metadata.file.reader;

import com.rwu.log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import script.input.InputConfig;
import script.input.InputModeEnum;
import script.input.InputModeSize;
import script.log.LogInTests;
import script.metadata.file.FileMetadata;

import java.util.ArrayList;
import java.util.List;

public class FileMetadataReaderTest {

    @BeforeAll
    public static void prepare() {
        Log.initLog(new LogInTests());
    }

    @Test
    public void testReadBasics() {
        List<String> lines1 = new ArrayList<>();
        lines1.add("  //  title: Title1");
        lines1.add("//description: Useful script");

        FileMetadata result1 = FileMetadataReader.read(lines1, "demo");

        Assertions.assertEquals("Title1", result1.getTitle());
        Assertions.assertEquals("Useful script", result1.getDescription());
    }

    @Test
    public void testReadFields1() {
        List<String> lines1 = new ArrayList<>();
        lines1.add("  //  input: type=line; label=Directory; size=3; mandatory");
        lines1.add("//input: type=line; label=Ending(s), comma-separated; description=Working Examples: \".java, xml\", \"java, .css, .html\"");
        lines1.add("//input: type=text; label=Lines; description=Add lines here; size=4");
        lines1.add("//input: type=select; label=Mode; description=Add lines here; item=list_only,Only List; item=write_details,Write Details");

        FileMetadata result1 = FileMetadataReader.read(lines1, "demo");
        List<InputConfig> inputs = result1.getInputs();

        InputConfig input1 = inputs.get(0);
        InputConfig input2 = inputs.get(1);
        InputConfig input3 = inputs.get(2);
        InputConfig input4 = inputs.get(3);

        // 1
        Assertions.assertEquals("Directory", input1.getLabel());
        Assertions.assertEquals(InputModeSize.medium2, input1.getSize());
        Assertions.assertEquals(InputModeEnum.Textfield, input1.getInputMode());
        Assertions.assertEquals(true, input1.isMandatory());

        // 2
        Assertions.assertEquals("Ending(s), comma-separated", input2.getLabel());
        Assertions.assertEquals("Working Examples: \".java, xml\", \"java, .css, .html\"", input2.getDescription());

        // 3
        Assertions.assertEquals("Lines", input3.getLabel());
        Assertions.assertEquals(InputModeSize.big1, input3.getSize());
        Assertions.assertEquals(InputModeEnum.Textarea, input3.getInputMode());

        // 4
        Assertions.assertEquals(InputModeEnum.Selection, input4.getInputMode());
        Assertions.assertEquals("list_only", input4.getSelectionItems().get(0).getKeyword());
        Assertions.assertEquals("Only List", input4.getSelectionItems().get(0).getText());
        Assertions.assertEquals("write_details", input4.getSelectionItems().get(1).getKeyword());
        Assertions.assertEquals("Write Details", input4.getSelectionItems().get(1).getText());
    }
}
