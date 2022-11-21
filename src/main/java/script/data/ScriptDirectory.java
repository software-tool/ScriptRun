package script.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rwu.misc.FileNameUtils;

import script.config.BasicConfig;

public class ScriptDirectory {

	private File directory;

	public ScriptDirectory(File directory) {
		this.directory = directory;
	}

	public ScriptDirectory(String directory) {
		this.directory = new File(directory);
	}

	public String getPath() {
		return directory.getAbsolutePath();
	}

	public String getName() {
		return directory.getName();
	}

	public File getDirectory() {
		return directory;
	}

	public List<Script> readScripts() {
		List<Script> scripts = new ArrayList<>();

		File[] files = directory.listFiles();
		if (files == null) {
			return scripts;
		}

		for (File file : files) {
			if (!file.isFile()) {
				continue;
			}

			String nameLower = file.getName();
			boolean isScript = FileNameUtils.hasEnding(nameLower, BasicConfig.scriptFileEndings);
			if (!isScript) {
				continue;
			}

			Script script = new Script(directory, file.getName());
			scripts.add(script);
		}

		return scripts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScriptDirectory that = (ScriptDirectory) o;
		return Objects.equals(directory, that.directory);
	}

	@Override
	public int hashCode() {
		return Objects.hash(directory);
	}
}
