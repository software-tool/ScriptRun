package com.rwu.fx.util;

import cp.system.AppCommon;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class FxKeyUtils {

	private static final String KEY_CTRL = "ctrl+";
	private static final String KEY_CTRL_SHIFT = "ctrl+shift+";

	private static final String KEY_META = "meta+";
	private static final String KEY_META_SHIFT = "meta+shift+";

	public static KeyCombination meta(String key) {
		if (AppCommon.isMac) {
			return KeyCombination.keyCombination(KEY_META + key);
		} else {
			return KeyCombination.keyCombination(KEY_CTRL + key);
		}
	}

	public static KeyCombination metaShift(String key) {
		if (AppCommon.isMac) {
			return KeyCombination.keyCombination(KEY_META_SHIFT + key);
		} else {
			return KeyCombination.keyCombination(KEY_CTRL_SHIFT + key);
		}
	}

	public static KeyCombination metaByCode(KeyCode keyCode) {
		if (AppCommon.isMac) {
			return new KeyCodeCombination(keyCode, KeyCodeCombination.META_DOWN);
		} else {
			return new KeyCodeCombination(keyCode, KeyCodeCombination.CONTROL_DOWN);
		}
	}

	public static KeyCombination metaShiftByCode(KeyCode keyCode) {
		if (AppCommon.isMac) {
			return new KeyCodeCombination(keyCode, KeyCodeCombination.META_DOWN, KeyCodeCombination.SHIFT_DOWN);
		} else {
			return new KeyCodeCombination(keyCode, KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN);
		}
	}

	public static KeyCombination key(String key) {
		return KeyCombination.keyCombination(key);
	}

	public static KeyCombination key(KeyCode keyCode) {
		return new KeyCodeCombination(keyCode);
	}

	public static boolean isCtrlDown(KeyEvent keyEvent) {
		if (AppCommon.isMac) {
			return keyEvent.isMetaDown();
		}

		return keyEvent.isControlDown();
	}

}
