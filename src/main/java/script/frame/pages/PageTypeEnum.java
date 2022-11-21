package script.frame.pages;

public enum PageTypeEnum {

	Main, ScriptRecents,

	ScriptDetails, ScriptEdit,

	ScriptDirectory,

	GlobalOutput,

	Welcome, Options;

	public boolean isScriptPage() {
		return this == PageTypeEnum.ScriptEdit || this == PageTypeEnum.ScriptDetails;
	}

	public boolean isScriptEdit() {
		return this == PageTypeEnum.ScriptEdit;
	}

	public boolean isScriptResult() {
		return this == PageTypeEnum.ScriptDetails;
	}
}
