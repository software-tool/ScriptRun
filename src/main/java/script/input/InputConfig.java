package script.input;

import com.rwu.misc.EqualsUtil;

import java.util.ArrayList;
import java.util.List;

public class InputConfig {

	private InputModeEnum inputMode = InputModeEnum.Textfield;
	private InputModeSize size = InputModeSize.small1;

	private String label = null;
	private String description = null;

	private String inputText = null;
	private boolean mandatory = false;

	private List<InputSelectionItemConfig> selectionItems = new ArrayList();

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public String getInputText() {
		return inputText;
	}

	public void setInputMode(InputModeEnum inputMode) {
		this.inputMode = inputMode;
	}

	public InputModeEnum getInputMode() {
		return inputMode;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public InputModeSize getSize() {
		return size;
	}

	public void setSize(InputModeSize size) {
		this.size = size;
	}

	public void addSelectionItem(String keyword, String text) {
		this.selectionItems.add(new InputSelectionItemConfig(keyword, text));
	}

	public List<InputSelectionItemConfig> getSelectionItems() {
		return selectionItems;
	}

	public boolean equalsOtherField(InputConfig other) {
		if (other.inputMode != inputMode) {
			return false;
		}

		if (EqualsUtil.isDifferent(other.label, label)) {
			return false;
		}
		if (EqualsUtil.isDifferent(other.description, description)) {
			return false;
		}

		return true;
	}
}
