package script.frame.code;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import script.data.Script;
import script.lang.K;

public class PaneCodeToolbar {

	private ToolBar toolbar = new ToolBar();

	private Label labelOutput = new Label();

	private String outputText = "";

	private Script script;
	private IToolbarListener listener;

	private Button buttonRun;
	private Button buttonStop;

	private Button buttonSave;

	public PaneCodeToolbar(Script script, IToolbarListener listener) {
		this.script = script;
		this.listener = listener;

		labelOutput.setVisible(false);

		labelOutput.setText("[" + Ln.get(K.Output) + "]");

		fill();
	}

	private void fill() {
		buttonSave = new Button();

		buttonRun = new Button(Ln.get(K.RunScript));
		buttonStop = new Button(Ln.get(K.StopScript));

		buttonSave.setDisable(true);

		FxIconUtils.setIconNoStyleWithSize(buttonSave, "save.png");

		buttonSave.setOnAction(e -> {
			listener.performSave();
		});

		buttonRun.setOnAction(e -> {
			outputText = "";
			labelOutput.setVisible(false);

			buttonRun.setDisable(true);
			buttonStop.setDisable(false);

			listener.performRun();
		});

		buttonStop.setOnAction(e -> {
			listener.performStop();
		});

		ObservableList<Node> items = toolbar.getItems();

		items.add(buttonRun);
		items.add(buttonSave);
		//items.add(buttonStop);
		items.add(new Label("     "));
		items.add(labelOutput);
	}

	public void addOutput(String text) {
		outputText += text;

		FxTooltip.setTooltip(labelOutput, outputText);

		labelOutput.setVisible(true);
	}

	public void onExecutionEnded() {
		buttonStop.setDisable(true);
		buttonRun.setDisable(false);
	}

	public void onCodeEdited() {
		buttonSave.setDisable(false);
	}

	public Node getContent() {
		return toolbar;
	}

	public interface IToolbarListener {
		public void performSave();

		public void performRun();

		public void performStop();
	}
}
