package script.execute.intf;

public interface IScriptImpl {

	void prepare();

	void run();

	boolean isValid();

	//public String getOutput();
}
