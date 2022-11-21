package script.frame.details.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import script.frame.details.PaneScriptDetails;

public class ContextMenuSnippetsOutput {

	private PaneScriptDetails parent;

	private ContextMenu contextMenu = new ContextMenu();

	public ContextMenuSnippetsOutput(PaneScriptDetails parent) {
		this.parent = parent;
	}

	public void initEntries() {
		contextMenu.getItems().clear();

		initOutputs();
	}

	private void initOutputs() {
		StringBuilder setHtmlText = new StringBuilder();
		StringBuilder htmlExampleText = new StringBuilder();

		setHtmlText.append("StringBuilder sb = new StringBuilder();");
		setHtmlText.append("\n");
		setHtmlText.append("OUTPUT.setHtml(sb.toString());");

		htmlExampleText.append("OUTPUT.setHead(\"<style>* { font-family: Arial; } </style>\");");
		htmlExampleText.append("\n");
		htmlExampleText.append("OUTPUT.addHtml(\"<h1>Our Meeting</h1>\");");
		htmlExampleText.append("\n");
		htmlExampleText.append("OUTPUT.addHtml(\"<p>Content: Find new solutions\");");

		// HTML

		MenuItem setHead = new MenuItem("OUTPUT.setHead()");
		MenuItem addHtml = new MenuItem("OUTPUT.addHtml()");
		MenuItem setHtml = new MenuItem("OUTPUT.setHtml()");

		MenuItem htmlExample = new MenuItem("HTML Example");

		setHead.setOnAction(e -> {
			parent.insertTextAtCursor("OUTPUT.setHead(\"<style>* { font-family: Arial; } </style>\");");
		});
		addHtml.setOnAction(e -> {
			parent.insertTextAtCursor("OUTPUT.addHtml(\"<h1>Our Meeting</h1>\");");
		});
		setHtml.setOnAction(e -> {
			parent.insertTextAtCursor(setHtmlText.toString());
		});
		htmlExample.setOnAction(e -> {
			parent.insertTextAtCursor(htmlExampleText.toString());
		});

		contextMenu.getItems().add(setHead);
		contextMenu.getItems().add(addHtml);
		contextMenu.getItems().add(setHtml);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(htmlExample);
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}
}
