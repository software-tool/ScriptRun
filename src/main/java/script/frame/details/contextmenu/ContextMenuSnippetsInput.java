package script.frame.details.contextmenu;

import com.rwu.application.lang.Ln;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import script.frame.details.PaneScriptDetails;
import script.input.InputConfig;
import script.lang.K;

import java.util.List;

public class ContextMenuSnippetsInput {

	private PaneScriptDetails parent;

	private ContextMenu contextMenu = new ContextMenu();

	public ContextMenuSnippetsInput(PaneScriptDetails parent) {
		this.parent = parent;

	}

	public void initEntries() {
		contextMenu.getItems().clear();

		initInputs();
	}

	private void initInputs() {
		List<InputConfig> inputsConfig = parent.getInputsConfig();

		int i=0;
		for (InputConfig inputConfig : inputsConfig) {
			final int index = i;

			String label = inputConfig.getLabel();
			if (label == null) {
				label = Ln.get(K.InputTitle);
			}

			label += " (" + i + ")";

			MenuItem item = new MenuItem(label);
			item.setOnAction(e -> {
				parent.insertTextAtCursor("String input" + (index) +" = INPUT.get(" + index + ");");
			});

			contextMenu.getItems().add(item);

			i++;
		}

		// Definitions

		StringBuilder sb = new StringBuilder();
		sb.append("// " + Ln.get(K.FileMetadataInfo));
		sb.append("\n");
		sb.append("// title: List Files in Directory");
		sb.append("\n");
		sb.append("// description:");
		sb.append("\n");
		sb.append("// input: type=line; label=Directory; size=3; mandatory");
		sb.append("\n");
		sb.append("// input: type=line; label=Ending(s), comma-separated; description=Working Examples: \".java, xml\", \"java, .css, .html\"");

		contextMenu.getItems().add(new SeparatorMenuItem());

		MenuItem itemFieldDefinitions1 = new MenuItem(Ln.get(K.SnippetFileMetadata));
		itemFieldDefinitions1.setOnAction(e -> {
			parent.insertTextAtCursor(sb.toString());
		});

		contextMenu.getItems().add(itemFieldDefinitions1);
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}
}
