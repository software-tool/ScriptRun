package script.frame.code;

/**
 * Editor-Tab
 *
 * (aktuell deaktiviert)
 */
@Deprecated
public class PaneCode {} /*implements IScriptResultListener, ICloseRequest, ICodeEditListener, IExecutePane {
	private BorderPane pane = new BorderPane();

	private PaneCodeToolbar toolbar;

	private Script script;

	// Execution
	private ScriptInst inst = new ScriptInst();
	private ScriptRunning scriptRunning = null;

	private PaneCodeOnly paneCodeOnly;

	private TextArea areaResult = new TextArea();

	public PaneCode(Script script) {
		this.script = script;

		initEditor();
	}

	private void initEditor() {
		String scriptText = ScriptReader.readScript(script.getFile());

		paneCodeOnly = new PaneCodeOnly(this);
		paneCodeOnly.setText(scriptText);
	}

	public void performSave() {
		File file = script.getFile();
		String text = paneCodeOnly.getText();

		try {
			FileUtils.writeToFile(file, text);
		} catch (Exception e) {
			Log.error("Failed to write file", e);
		}
	}

	@Override
	public void triggerRunScript() {
		File filepath = script.getFile();
		ScriptFile file = new ScriptFile(filepath);

		scriptRunning = ScriptRun.runScriptInThread(null, inst, file).getScriptRunning();

		// Error
		List<ScriptError> error = scriptRunning.getAndResetError();

		String errorText = ScriptError.getDisplayInformation(error);
		if (errorText != null) {
			// Only compile errors (Execution in Thread)

			areaResult.setText(errorText);
			areaResult.setVisible(true);
		} else {
			areaResult.setText(null);
		}

		RecentManager.addEntry(filepath);
	}

	public void performStop() {
		scriptRunning.interruptThread();
	}

	private void onExecutionEnded() {
		toolbar.onExecutionEnded();
	}

	public Node getContent() {
		IToolbarListener listener = new IToolbarListener() {
			@Override
			public void performSave() {
				PaneCode.this.performSave();
			}

			@Override
			public void performRun() {
				PaneCode.this.triggerRunScript();
			}

			@Override
			public void performStop() {
				PaneCode.this.performStop();
			}
		};

		areaResult.setVisible(false);

		toolbar = new PaneCodeToolbar(script, listener);

		pane.setPadding(new Insets(0, 2, 2, 0));

		pane.setTop(toolbar.getContent());
		pane.setCenter(paneCodeOnly.getStackPage());
		pane.setBottom(areaResult);

		return pane;
	}

	@Override
	public int getInstId() {
		return inst.getInstId();
	}

	@Override
	public void addOutput(String text) {
		Platform.runLater(() -> {
			toolbar.addOutput(text);
		});
	}

	@Override
	public void applyScriptFinished() {
		Platform.runLater(() -> {
			// Error
			List<ScriptError> error = scriptRunning.getAndResetError();

			String errorText = ScriptError.getDisplayInformation(error);
			if (errorText != null) {
				areaResult.appendText(errorText);
				areaResult.setVisible(true);
			}

			onExecutionEnded();
		});
	}

	@Override
	public boolean hasRunningScript() {
		return false;
	}

	public Script getScript() {
		return script;
	}

	@Override
	public boolean requestClose() {
		// TODO: Not when script is executed
		return true;
	}

	@Override
	public void textChanged() {
		toolbar.onCodeEdited();
	}
}
*/