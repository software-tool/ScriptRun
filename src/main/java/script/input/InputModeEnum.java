package script.input;

public enum InputModeEnum {

	Textfield, Textarea, Selection;

	private static final String KEYWORD_FIELD_TYPE_TEXT = "line";
	private static final String KEYWORD_FIELD_TYPE_TEXTAREA = "text";

	private static final String KEYWORD_FIELD_TYPE_SELECTION = "select";

	public boolean isTextfield() {
		return this == Textfield;
	}

	public boolean isTextarea() {
		return this == Textarea;
	}

	public boolean isSelection() {
		return this == Selection;
	}

	public static InputModeEnum fromConfig(String configStr) {
		if (KEYWORD_FIELD_TYPE_TEXT.equals(configStr)) {
			return Textfield;
		} else if (KEYWORD_FIELD_TYPE_TEXTAREA.equals(configStr)) {
			return Textarea;
		} else if (KEYWORD_FIELD_TYPE_SELECTION.equals(configStr)) {
			return Selection;
		}

		return Textfield;
	}
}
