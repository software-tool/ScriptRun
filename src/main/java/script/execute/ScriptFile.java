package script.execute;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import script.data.Script;
import script.data.ScriptType;
import script.Output;
import script.input.InputConfig;
import script.recent.ScriptRecent;
import script.recent.ScriptValue;

public class ScriptFile {

	private ScriptType scriptType = ScriptType.GROOVY;

	private File file;

	private List<InputConfig> inputs = new ArrayList<>();
	private Output output = new Output();

	public ScriptFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setInputs(List<InputConfig> inputs) {
		this.inputs = inputs;
	}

	public List<InputConfig> getInputs() {
		return inputs;
	}

	public Output getOutput() {
		return output;
	}

	public ScriptRecent asRecent() {
		ScriptRecent recent = new ScriptRecent(file.getAbsolutePath());
		recent.setTime(new Date());

		int i=0;
		for(InputConfig config : inputs) {
			ScriptValue scriptValue = new ScriptValue(i, config.getInputText());

			recent.getValues().add(scriptValue);

			i++;
		}

		return recent;
	}

	public Script asScript() {
		return new Script(file.getParentFile(), file.getName());
	}
}
