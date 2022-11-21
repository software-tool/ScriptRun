package script.execute.intf;

public interface IScriptResultListener {

	boolean hasInstId(int id);

	void addOutput(String text);
}
