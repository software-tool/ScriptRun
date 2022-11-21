package script.execute.result;

import java.util.List;

public class ScriptError {

	private ScriptErrorType type = null;

	private String message = null;
	private String details = null;

	private String lineNumbersStr;

	public ScriptError(ScriptErrorType type, String message, String details, String lineNumbersStr) {
		this.type = type;

		this.message = message;
		this.details = details;

		this.lineNumbersStr = lineNumbersStr;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public ScriptErrorType getType() {
		return type;
	}

	public String getLineNumbersStr() {
		return lineNumbersStr;
	}

	public String getDisplayInformation() {
		return message + "\nline(s): " + lineNumbersStr;
	}

	public static String getDisplayInformation(List<ScriptError> errors) {
		if (errors.isEmpty())
			return null;

		StringBuilder sb = new StringBuilder();

		int i=0;
		for(ScriptError error : errors) {
			if (i > 0) {
				sb.append("\n");
			}

			sb.append(error.getDisplayInformation());
			i++;
		}

		return sb.toString();
	}
}
