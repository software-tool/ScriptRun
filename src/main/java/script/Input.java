package script;

import java.util.ArrayList;
import java.util.List;

public class Input {

	public String one = "";
	public String two = "";

	private List<String> list = new ArrayList<>();

	public void addInput(String value) {
		this.list.add(value);

		fill();
	}

	private void fill() {
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				one = list.get(i);
			}
			if (i == 1) {
				two = list.get(i);
			}
		}
	}

	public String get(int index) {
		if ((list.size() - 1) < index) {
			return null;
		}

		return list.get(index);
	}

}
