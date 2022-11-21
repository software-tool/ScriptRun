package script.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rwu.misc.FileNameUtils;

import script.config.BasicConfig;

public class ScriptDirectoryReader {

	public static List<File> getFilesOfDirectory(File directory) {
		List<File> result = new ArrayList<>();

		File[] found = directory.listFiles();

		if (found != null) {
			for (File current : found) {
				result.add(current);
			}
		}

		return result;
	}

	public static List<File> getScriptFilesOfDirectory(File directory, List<File> result) {
		if (result == null) {
			result = new ArrayList<>();
		}

		File[] found = directory.listFiles();

		if (found != null) {
			for (File file : found) {
				if (file.isDirectory()) {
					getScriptFilesOfDirectory(file, result);
					continue;
				}

				String nameLower = file.getName();
				boolean isScript = FileNameUtils.hasEnding(nameLower, BasicConfig.scriptFileEndings);
				if (!isScript) {
					continue;
				}

				result.add(file);
			}
		}

		return result;
	}

	public static List<File> getLibraryFilesOfDirectory(File directory) {
		List<File> result = new ArrayList<>();
		File[] found = directory.listFiles();

		if (found != null) {
			for (File file : found) {
				String nameLower = file.getName();
				boolean isScript = FileNameUtils.hasEnding(nameLower, BasicConfig.libraryFileEndings);
				if (!isScript) {
					continue;
				}

				result.add(file);
			}
		}

		return result;
	}
}
