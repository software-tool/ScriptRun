package script.metadata.file.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import script.input.InputConfig;
import script.input.InputModeEnum;
import script.input.InputModeSize;
import script.input.InputSelectionItemConfig;

public class InputPersistence {

	private static final String LABEL = "label";
	private static final String DESCRIPTION = "description";
	private static final String TYPE = "type";
	private static final String SIZE = "size";
	private static final String MANDATORY = "mandatory";
	private static final String SELECTION_ITEMS = "items";

	private static final String KEYWORD = "keyword";
	private static final String TEXT = "text";

	private static final String TYPE_TEXTFIELD = "field";
	private static final String TYPE_TEXTAREA = "textarea";
	private static final String TYPE_SELECTION = "select";

	private static final String SIZE1 = "1";
	private static final String SIZE1STR = "small1";

	private static final String SIZE2 = "2";
	private static final String SIZE2STR = "medium1";

	private static final String SIZE3 = "3";
	private static final String SIZE3STR = "medium2";

	private static final String SIZE4 = "4";
	private static final String SIZE4STR = "big1";

	private static final String SIZE5 = "5";
	private static final String SIZE5STR = "big2";

	public static JSONArray toJson(List<InputConfig> inputs) {
		JSONArray inputsArr = new JSONArray();

		for (InputConfig input : inputs) {
			String type = getTypeString(input.getInputMode());
			String size = getSizeString(input.getSize());

			boolean mandatory = input.isMandatory();

			JSONObject object = new JSONObject();
			object.put(LABEL, input.getLabel());
			object.put(DESCRIPTION, input.getDescription());
			object.put(TYPE, type);
			object.put(SIZE, size);
			object.put(MANDATORY, mandatory);

			JSONArray selectionItemsObj = new JSONArray();
			for(InputSelectionItemConfig item : input.getSelectionItems()) {
				JSONObject itemObj = new JSONObject();
				itemObj.put(KEYWORD, item.getKeyword());
				itemObj.put(TEXT, item.getText());

				selectionItemsObj.put(itemObj);
			}
			object.put(SELECTION_ITEMS, selectionItemsObj);

			inputsArr.put(object);
		}

		return inputsArr;
	}

	public static List<InputConfig> parse(JSONArray inputsArr) {
		List<InputConfig> inputs = new ArrayList<>();

		for (Object obj : inputsArr) {
			if (!(obj instanceof JSONObject)) {
				continue;
			}

			JSONObject inputObj = (JSONObject) obj;

			String label = null;
			String description = null;
			if (inputObj.has(LABEL)) {
				label = inputObj.getString(LABEL);
			}
			if (inputObj.has(DESCRIPTION)) {
				description = inputObj.getString(DESCRIPTION);
			}

			String modeStr = inputObj.getString(TYPE);
			String sizeStr = null;
			if (inputObj.has(SIZE)) {
				sizeStr = inputObj.getString(SIZE);
			}

			boolean mandatory = false;
			if (inputObj.has(MANDATORY)) {
				mandatory = inputObj.getBoolean(MANDATORY);
			}

			JSONArray itemsArr = null;
			if (inputObj.has(SELECTION_ITEMS)) {
				itemsArr = inputObj.getJSONArray(SELECTION_ITEMS);
			}

			InputConfig input = new InputConfig();
			input.setLabel(label);
			input.setDescription(description);
			input.setInputMode(getType(modeStr));
			input.setSize(getSize(sizeStr));
			input.setMandatory(mandatory);

			if (itemsArr != null) {
				for(int i=0; i<itemsArr.length(); i++) {
					JSONObject itemObj = (JSONObject) itemsArr.get(i);

					input.addSelectionItem(itemObj.getString(KEYWORD), itemObj.getString(TEXT));
				}
			}

			inputs.add(input);
		}

		return inputs;
	}

	private static String getTypeString(InputModeEnum mode) {
		if (mode.isTextfield()) {
			return TYPE_TEXTFIELD;
		} else if (mode.isTextarea()) {
			return TYPE_TEXTAREA;
		} else if (mode.isSelection()) {
			return TYPE_SELECTION;
		}

		return TYPE_TEXTFIELD;
	}

	private static InputModeEnum getType(String typeStr) {
		if (TYPE_TEXTFIELD.equals(typeStr)) {
			return InputModeEnum.Textfield;
		} else if (TYPE_TEXTAREA.equals(typeStr)) {
			return InputModeEnum.Textarea;
		} else if (TYPE_SELECTION.equals(typeStr)) {
			return InputModeEnum.Selection;
		}

		return InputModeEnum.Textfield;
	}

	private static String getSizeString(InputModeSize size) {
		if (size == InputModeSize.small1) {
			return SIZE1STR;
		}
		if (size == InputModeSize.medium1) {
			return SIZE2STR;
		}
		if (size == InputModeSize.medium2) {
			return SIZE3STR;
		}
		if (size == InputModeSize.big1) {
			return SIZE4STR;
		}
		if (size == InputModeSize.big2) {
			return SIZE5STR;
		}

		return SIZE2STR;
	}

	private static InputModeSize getSize(String typeStr) {
		if (SIZE1STR.equals(typeStr)) {
			return InputModeSize.small1;
		}
		if (SIZE2STR.equals(typeStr)) {
			return InputModeSize.medium1;
		}
		if (SIZE3STR.equals(typeStr)) {
			return InputModeSize.medium2;
		}
		if (SIZE4STR.equals(typeStr)) {
			return InputModeSize.big1;
		}
		if (SIZE5STR.equals(typeStr)) {
			return InputModeSize.big2;
		}

		return InputModeSize.small1;
	}

	public static InputModeSize parse(String value) {
		if (SIZE1.equals(value)) {
			return InputModeSize.small1;
		} else if (SIZE2.equals(value)) {
			return InputModeSize.medium1;
		} else if (SIZE3.equals(value)) {
			return InputModeSize.medium2;
		} else if (SIZE4.equals(value)) {
			return InputModeSize.big1;
		} else if (SIZE5.equals(value)) {
			return InputModeSize.big2;
		}

		return InputModeSize.medium1;
	}
}
