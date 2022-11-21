package script.frame.details.contextmenu;

import javafx.scene.control.ContextMenu;
import script.frame.details.PaneScriptDetails;

public class ContextMenuMore {

	private PaneScriptDetails parent;

	private ContextMenu contextMenu = new ContextMenu();

	public ContextMenuMore(PaneScriptDetails parent) {
		this.parent = parent;

		init();
	}

	private void init() {
		/*CheckMenuItem itemEditInline = new CheckMenuItem(Ln.get(K.ScriptShowCode));
		itemEditInline.setOnAction(e -> {
			parent.setShowCode(itemEditInline.isSelected());
		});
		
		MenuItem itemAddInput = new MenuItem(Ln.dots(K.AddInput));
		itemAddInput.setOnAction(e -> {
			parent.addInput();
		});
		
		contextMenu.getItems().add(itemEditInline);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(itemAddInput);*/
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}
}
