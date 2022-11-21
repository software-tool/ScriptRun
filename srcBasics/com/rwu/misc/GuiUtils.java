package com.rwu.misc;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Utils for AWT
 */
public class GuiUtils {

	private static int ORIGINAL_ROW_HEIGHT_TABLE = 16;

	private static int BUTTON_TARGET_WIDTH_DEFAULT = 88;
	private static int BUTTON_TARGET_WIDTH_MAC = 92;

	public static void setButtonSelected(JButton button, int fontSize, boolean selected) {
		Font font = button.getFont();
		Map attributes = font.getAttributes();

		if (selected) {
			attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		} else {
			attributes.put(TextAttribute.WEIGHT, TextAttribute.WIDTH_REGULAR);
		}

		attributes.put(TextAttribute.SIZE, fontSize);

		button.setFont(font.deriveFont(attributes));
	}

	/**
	 * Minimum width of buttons
	 */
	public static void ensureButtonWidthDefault(double factor, JButton... buttons) {
		int targetWidth = BUTTON_TARGET_WIDTH_DEFAULT;
		if (AppBasic.useMacButtonOrder) {
			targetWidth = BUTTON_TARGET_WIDTH_MAC;
		}

		for (JButton button : buttons) {
			ensureButtonWidth(button, targetWidth, factor);
		}
	}

	public static void applyButtonOrder(JButton... buttons) {
		if (!AppBasic.useMacButtonOrder) {
			return;
		}

		Container parent = null;

		for (JButton button : buttons) {
			if (parent == null) {
				parent = button.getParent();
			}

			parent.remove(button);
		}

		for (JButton button : buttons) {
			parent.add(button);
		}
	}

	public static void applyButtonsTransparent(AbstractButton... buttons) {
		for (AbstractButton button : buttons) {
			button.setOpaque(false);
		}
	}

	/**
	 * Applies zoom to table
	 */
	public static void initLineHeights(JTable table, double factor) {
		if (factor == -1) {
			factor = 1;
		}

		int prevHeight = table.getRowHeight();
		if (factor == 1 && prevHeight == ORIGINAL_ROW_HEIGHT_TABLE) {
			// No change
			return;
		}

		// Line height
		int newHeight = (int) (ORIGINAL_ROW_HEIGHT_TABLE * factor);
		table.setRowHeight(newHeight);

		// Table header
		JTableHeader th = table.getTableHeader();
		th.setPreferredSize(new Dimension(0, newHeight + 4));
	}

	/**
	 * Set window size if bigger than current size
	 *
	 * @return True if applied
	 */
	public static void setBoundsIfBigger(Window window, Rectangle bounds) {
		if (bounds == null) {
			return;
		}

		Dimension size = window.getSize();

		if (bounds.getWidth() > size.getWidth() && bounds.getHeight() > size.getHeight()) {
			window.setLocation(bounds.getLocation());
			window.setSize(bounds.getSize());

			// Needs to be visible
			ScreenUtils.ensureIsVisible(window, bounds);
		}
	}

	/**
	 * Applies zoom for icons
	 */
	public static void applyIconSize(double factor, AbstractButton... buttons) {
		applyIconSize(factor, 16, 16, false, buttons);
	}

	/**
	 * Applies zoom for icons, main icons
	 */
	public static void applyIconSizeMain(double factor, AbstractButton... buttons) {
		applyIconSize(factor, 16, 16, true, buttons);
	}

	public static void applyIconSize(double factor, int baseWidth, int baseHeight, boolean applyBorder, AbstractButton... buttons) {
		if (factor == -1) {
			factor = 1;
		}

		if (applyBorder) {
			//System.out.println("factor2: " + factor);
		}

		// Factor
		// 11: 1.00
		// 12: 1.09
		// 13: 1.18
		// 14: 1.27
		// 17: 1.54
		// 18: 1.63
		// 20: 1.81

		int border = 6;
		if (factor > 1.80) {
			// 20

			border = 10;

			factor -= 0.25;
		} else if (factor > 1.50) {
			// 17

			border = 8;

			if (applyBorder) {
				factor = 1.3;
			} else {

			}
		} else if (factor > 1.30) {
			// 15

			border = 8;

			if (applyBorder) {
				factor = 1.15;
			} else {

			}
		} else if (factor > 1.20) {
			// 14

			border = 7;

			if (applyBorder) {
				factor = 1.1;
			} else {
				factor = 1.2;
			}
		} else if (factor > 1.15) {
			// 13

			border = 6;
			//factor = 1.0;
		} else if (factor > 1.0) {
			// 12

			factor = 1.0;
		}

		int newWidth = (int) (baseWidth * factor);
		int newHeight = (int) (baseHeight * factor);

		for (AbstractButton button : buttons) {
			Icon icon = button.getIcon();
			if (!(icon instanceof ImageIcon)) {
				continue;
			}

			ImageIcon imageIcon = (ImageIcon) icon;

			Image img = imageIcon.getImage();
			Image newimg = img.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
			ImageIcon iconNew = new ImageIcon(newimg);

			button.setIcon(iconNew);

			if (applyBorder) {
				button.setBorder(BorderFactory.createEmptyBorder(border, border + 2, border, border + 2));
			}
		}
	}

	/**
	 * Apply font size to tree
	 */
	public static void applyTreeRowHeight(int fontSize, JTree tree) {
		if (fontSize == -1) {
			fontSize = 11;
		}

		int treeSize = fontSize + 5;
		tree.setRowHeight(treeSize);
	}

	/**
	 * Applies line height from tree configuration
	 * 
	 * @deprecated
	 */
	@Deprecated
	public static void applyTreeRowHeightFromConfig(int height, JTree tree) {
		if (height == -1) {
			return;
		}

		tree.setRowHeight(height);
	}

	/**
	 * Applies font size to Textarea
	 */
	public static void applyGuiFont(int fontSize, JTextArea textarea) {
		if (fontSize == -1) {
			fontSize = 11;
		}

		Font font = textarea.getFont().deriveFont((float) fontSize);
		textarea.setFont(font);
	}

	public static void applyGuiFont(int fontSize, JList list) {
		if (fontSize == -1) {
			fontSize = 11;
		}

		Font font = list.getFont().deriveFont((float) fontSize);
		list.setFont(font);
	}

	/**
	 * Sets font size using reflection
	 *
	 * Infos: http://stackoverflow.com/questions/11309861/bug-or-feature-swing-default-gui-font-incorrect-for-win6
	 *
	 * https://bugs.launchpad.net/ubuntu/+source/netbeans/+bug/669159 http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6436509 https://bugs.openjdk.java.net/browse/JDK-6436509
	 * http://www.simosh.com/article/bbdajigb-bug-or-feature-swing-default-gui-font-incorrect-for-win6.html
	 *
	 * @param newSize If -1: From settings
	 */
	public static void setDefaultGUIFont(int newSize, int fallbackSize) {
		if (newSize == -1) {
			newSize = fallbackSize;
		}

		if (newSize == -1) {
			return;
		}

		setDesktopFontProperty("win.defaultGUI.font", newSize);
	}

	/**
	 * Set special font (using key)
	 *
	 * @param fontProperty System-Key
	 * @param newSize      Size
	 */
	public static void setFontProperty(String fontProperty, int newSize) {
		Font font = UIManager.getDefaults().getFont(fontProperty);
		if (font == null) {
			return;
		}

		font = font.deriveFont((float) newSize);
		UIManager.put(fontProperty, font);
	}

	public static void setPropertyNull(String property) {
		UIManager.put(property, null);
	}

	/**
	 * Lists all System-Keys/Font-Keys
	 *
	 * @deprecated As needed
	 */
	@Deprecated
	public static void listUIManagerKeys() {
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);

		for (Object key : keys) {
			// KEY + VALUE
			System.out.println(key + " = " + UIManager.get(key));

			// Only KEY
			// System.out.println(key);
			if (key != null && key.toString().toLowerCase().contains("font")) {
				// System.out.println(key);

				/*
				 * Font font = UIManager.getDefaults().getFont(key); if (font != null) { font = font.deriveFont((float) size); UIManager.put(key, font); }
				 */
			}
		}
	}

	public static Color getDefaultFocusColor() {
		// Color color = UIManager.getColor("Tree.selectionBackground");

		return new Color(192, 220, 243);
	}

	public static void selectAll(JTextField textField) {
		textField.setSelectionStart(0);
		textField.setSelectionEnd(textField.getText().length());
	}

	private static void setDesktopFontProperty(String fontProperty, int newSize) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		Font guiFont = (Font) toolkit.getDesktopProperty(fontProperty);
		if (guiFont == null) {
			return;
		}

		Font newFont = guiFont.deriveFont((float) newSize);
		invokeDeclaredMethod("setDesktopProperty", Toolkit.class, toolkit, fontProperty, newFont);
	}

	/**
	 * @deprecated For tests only
	 */
	@Deprecated
	public static void showAllDesktopProperties() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		String result = new String();

		result += "Toolkit: " + toolkit.toString() + "\n" + toolkit.getClass();

		Object value = getFieldValue(Toolkit.class, "desktopProperties", toolkit);
		Map<String, Object> valuesMap = (Map<String, Object>) value;

		if (valuesMap != null) {
			for (Entry<String, Object> entry : valuesMap.entrySet()) {
				result += entry.getKey() + ", " + entry.getValue() + "\n";
			}
		}

		JOptionPane.showMessageDialog(null, result);
	}

	private static void invokeDeclaredMethod(String methodName, Class<?> clazz, Object instance, String propertyName, Object propertyValue) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, String.class, Object.class);
			method.setAccessible(true);
			method.invoke(instance, propertyName, propertyValue);
		} catch (Exception e) {
			System.err.println("Failed to execute method using reflection: " + e.getMessage());
		}
	}

	private static Object getFieldValue(Class<?> clazz, String fieldname, Object obj) {
		try {
			Field field = clazz.getDeclaredField(fieldname);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sets minimum width for button
	 */
	private static void ensureButtonWidth(JButton button, int targetWidth, double factor) {
		if (factor == -1) {
			factor = 1;
		}

		double width = button.getSize().getWidth();
		int newWidth = (int) (targetWidth * factor);

		// GetWidth does not provide the real width
		double widthCorrected = width + 32.0d;

		if (widthCorrected < newWidth) {
			// System.out.println("Width: current=" + width + ", newWidth=" + newWidth + ", " + button.getText());

			int height = (int) button.getSize().getHeight();

			if (height == 0) {
				height = (int) button.getPreferredSize().getHeight();
			}

			button.setPreferredSize(new Dimension(newWidth, height));
		}
	}
}
