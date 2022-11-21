package com.rwu.misc;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardUtils {
	public static void setClipboard(String str) {
		StringSelection stringSelection = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	public static String getClipboard() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) t.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}
		return null;
	}
}
