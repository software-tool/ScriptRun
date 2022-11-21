package script.data;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.rwu.log.Log;
import com.rwu.misc.FileGetUtils;

import xml.LogHandler;

public class Script {

	private File directory = null;
	private String name = null;

	private int lines = -1;

	public Script(File directory, String name) {
		this.directory = directory;
		this.name = name;
	}

	public Script(File file) {
		this.directory = file.getParentFile();
		this.name = file.getName();
	}

	public File getFile() {
		return new File(directory, name);
	}

	public String getName() {
		return name;
	}

	public int getLinesCount() {
		if (lines != -1) {
			return lines;
		}

		File file = new File(directory, name);

		int count = -1;
		try {
			count = FileGetUtils.getFileLineCount(file);
		} catch (IOException e) {
			Log.error("Failed to read line count", e);
		}

		lines = count;

		return lines;
	}

	public long getFileSize() {
		File file = new File(directory, name);

		return file.length();
	}

	@Override
	public int hashCode() {
		return Objects.hash(directory, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Script other = (Script) obj;
		return Objects.equals(directory, other.directory) && Objects.equals(name, other.name);
	}

}
