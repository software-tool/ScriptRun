package script.frame.details.input;

import script.input.InputConfig;

public interface IInputParent {

	public void remove(InputConfig config);

	public int getIndex(InputConfig config);

	public void storeInputs();
}
