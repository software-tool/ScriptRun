package script.execute;

import groovy.lang.Binding;
import groovy.lang.Script;
import script.Execution;
import script.Input;
import script.Output;
import script.constant.ScriptConstants;
import script.frame.PaneMain;
import script.frame.state.ExecutionPanes;
import script.input.InputConfig;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instance of a script
 */
public class ScriptInst {

	private Map<String, Object> variables = new HashMap();

	private StringBuilder sb = new StringBuilder();
	private String content = null;

	private int instId;

	private Output output;

	private static final String NEWLINE = "\n";

	public ScriptInst() {
		this.instId = ExecutionPanes.getNextScriptId();
	}

	public void applyContent(String content) {
		this.content = content;
	}

	public Input applyInputs(List<InputConfig> inputs) {
		Input input = new Input();
		for (InputConfig config : inputs) {
			input.addInput(config.getInputText());
		}

		addVariable("input", input);

		return input;
	}

	public Output initOutput(Output output) {
		this.output = output;

		addVariable("output", output);

		return output;
	}

	public Execution setExecution(Input input, Output output) {
		Execution execution = new Execution();
		execution.input = input;
		execution.output = output;

		addVariable("execution", execution);

		return execution;
	}

	public void setOutput() {
		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) {
				writeToDestination(b);
			}

			private void writeToDestination(int b) {
				if (PaneMain.inst == null) {
					return;
				}

				String text = String.valueOf((char)b);
				PaneMain.inst.reportOutput(instId, text);
			}
		};

		DelegatingPrintStream stream = new DelegatingPrintStream(os);
		addVariable("out", stream);
	}

	public void bindVariables(Script script) {
		Binding binding = new Binding();

		for(Map.Entry<String, Object> variable : variables.entrySet()) {
			binding.setVariable(variable.getKey(), variable.getValue());
		}

		script.setBinding(binding);
	}

	public void applyStatic(Script script) {
		// Static
		script.evaluate("INPUT.in = input;");
		script.evaluate("OUTPUT.out = output;");
	}

	public void addVariable(String name, Object object) {
		this.variables.put(name, object);
	}

	public String getScript() {
		sb.setLength(0);

		sb.append(ScriptConstants.STATIC_INPUT);
		sb.append(ScriptConstants.STATIC_OUTPUT);
		sb.append(" ");

		sb.append(content);

		//Log.tmp(sb.toString());

		return sb.toString();
	}

	public int getInstId() {
		return instId;
	}

	public boolean isId(int id) {
		return instId == id;
	}

	public Output getOutput() {
		return output;
	}

	private static class DelegatingPrintStream extends PrintStream {
		public DelegatingPrintStream(OutputStream out) {
			super(out);
		}
	}
}
