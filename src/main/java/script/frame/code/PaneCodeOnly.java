package script.frame.code;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rwu.log.Log;
import com.rwu.misc.StringUtils;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import script.frame.code.GroovyHighlight.VisibleParagraphStyler;
import script.frame.code.editor.EditorCss;
import script.frame.intf.IContent;
import script.frame.listener.ICodeEditListener;

public class PaneCodeOnly implements IContent {

	private static int MAX_LINE_COUNT = 25;

	private CodeArea codeArea;
	private StackPane stackPage;
	private VirtualizedScrollPane<CodeArea> virtualScrollPane;

	private ICodeEditListener listener;

	private boolean reportEdit = true;
	private boolean lineCountCorrected = false;

	public PaneCodeOnly(ICodeEditListener listener) {
		this.listener = listener;

		initEditor();
	}

	public void setText(String scriptText) {
		reportEdit = false;

		StringBuilder sb = new StringBuilder();

		int lineCount = StringUtils.getLineCount(scriptText, MAX_LINE_COUNT);
		if (lineCount < MAX_LINE_COUNT) {
			lineCountCorrected = true;

			for(int i=lineCount + 1; i<MAX_LINE_COUNT; i++) {
				sb.append("\n");
			}
		}

		if (scriptText != null) {
			sb.insert(0, scriptText);
		}

		codeArea.replaceText(0, codeArea.getLength(), sb.toString());

		reportEdit = true;
	}

	private void initEditor() {
		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.getStyleClass().add("code-area");

		codeArea.textProperty().addListener((obs, oldText, newText) -> {
			// codeArea.setStyleSpans(0, computeHighlighting(newText));

			// boolean equals = codeArea.getText().equals(textOfFileTree);
			// parent.setDirty(!equals);

			if (listener != null && reportEdit) {
				listener.textChanged();
			}
		});

		// Selection, spacing
		String filenameEditorMisc = "pane-editor-misc.css";
		URL resource = EditorCss.class.getResource(filenameEditorMisc);
		if (resource == null) {
			Log.warn("Cannot find resource: " + filenameEditorMisc);
		}

		codeArea.getStylesheets().add(EditorCss.class.getResource(filenameEditorMisc).toExternalForm());

		codeArea.getVisibleParagraphs().addModificationObserver(new VisibleParagraphStyler<>(codeArea));

		// auto-indent: insert previous line's indents on enter
		final Pattern whiteSpace = Pattern.compile("^\\s+");
		codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
			if (KE.getCode() == KeyCode.ENTER) {
				int caretPosition = codeArea.getCaretPosition();
				int currentParagraph = codeArea.getCurrentParagraph();
				Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
				if (m0.find()) {
					Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
				}
			}
		});

		// Workaround: Bug for display when clicked in
		// Moving the window solves the problem
		//		codeArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
		//			@Override
		//			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean newValue) {
		//				DockNode dockNode = parent.getDockNode();
		//				if (dockNode == null) {
		//					return;
		//				}
		//
		//				StageFloatable floatableStage = dockNode.getFloatableStage();
		//				if (floatableStage == null) {
		//					return;
		//				}
		//
		//				if (newValue) {
		//					floatableStage.setWidth(floatableStage.getWidth() - 1);
		//				} else {
		//					floatableStage.setWidth(floatableStage.getWidth() + 1);
		//				}
		//			}
		//		});

		virtualScrollPane = new VirtualizedScrollPane<>(codeArea);

		// FxControlUtils.applyFontResize(codeArea);

		stackPage = new StackPane(virtualScrollPane);
	}

	public void insertAtCursor(String text) {
		int caretPosition = codeArea.getCaretPosition();

		codeArea.insertText(caretPosition, text);
	}

	@Override
	public Node getContent() {
		return stackPage;
	}

	public String getText() {
		String text = codeArea.getText();

		if (lineCountCorrected) {
			return text.trim();
		}

		return text;
	}
}
