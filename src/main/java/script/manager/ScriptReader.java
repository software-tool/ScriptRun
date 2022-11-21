package script.manager;

import java.io.File;

import com.rwu.log.Log;
import com.rwu.misc.Utils;

import script.execute.ScriptFile;
import xml.LogHandler;

public class ScriptReader {

	public static String readScript(ScriptFile scriptFile) {
		return readScript(scriptFile.getFile());
	}

	public static String readScript(File file) {
		try {
			String text = Utils.getFile(file, "UTF-8");
			return text;
		} catch (Exception e) {
			Log.error("Failed to read file", e);
		}

		return null;
	}

}
