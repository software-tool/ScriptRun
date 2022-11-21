package script;

import javax.swing.JOptionPane;

import com.rwu.menu.factory.MenuFactory;

import cp.system.AppCommon;

public class Scripting {
	
	private static final String SOFTWARE_NAME = "Groovy Execute";

	public static String[] startupArgs = null;

	public static void main(String[] args) {
		startupArgs = args;

		try {
			// System
			AppCommon.initOperatingSystem();

			// Menu Factory
			MenuFactory.USE_SWING_MENU = useSwingMenu();

			if (useSwingMenu()) {
				// Workaround: On open with file (Drop in Dock)
				// Works the Drop Handler (Java Swing) with parameters given OR
				// the JavaFX menu bar
				// https://bugs.openjdk.java.net/browse/JDK-8239590

				try {
					System.setProperty("com.apple.mrj.application.apple.menu.about.name", SOFTWARE_NAME);
					System.setProperty("apple.awt.application.name", SOFTWARE_NAME);
				} catch (Throwable t) {
					JOptionPane.showMessageDialog(null, "Failed to set Mac parameters: " + t.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}

				//AppleMacFileDropHandler.initMacDialogHandlers();

				// Does not work (maybe because of Java >9 or MacOS Security Features)
				// https://github.com/bitgamma/fx-platform-utils/tree/master/src/main/java/com/briksoftware/javafx/platform/osx
				// https://stackoverflow.com/questions/29101472/pass-parameters-to-javafx-application-by-double-click-on-file
			}

			ScriptingRun.main(args);
		} catch (Throwable t) {
			String info = t.toString();

			String title = "Failed to start " + SOFTWARE_NAME;
			String message = "Failed to start " + SOFTWARE_NAME + ".";

			String stacktrace = "";

			StackTraceElement[] stackTrace = t.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				stacktrace += element;
				stacktrace += "\n";
			}

			message += ("\n\nError: " + info);
			message += ("\n\nStacktrace: " + stacktrace);

			JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
		}
	}

	public static boolean useSwingMenu() {
		return AppCommon.isMac && startupArgs.length > 0;
	}
}
