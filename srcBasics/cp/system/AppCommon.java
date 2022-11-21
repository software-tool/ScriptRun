package cp.system;

import com.rwu.misc.AppBasic;

/**
 * Infos on System
 */
public class AppCommon {

	public static boolean isMac = false;
	public static boolean isWindows = true;

	public static boolean isNotMac = true;

	/**
	 * Init OS, at program start
	 */
	public static void initOperatingSystem() {
		String os = System.getProperty("os.name");
		AppCommon.isMac = os != null && os.contains("Mac");

		// AppCommon.isMac = true;

		AppCommon.isNotMac = !AppCommon.isMac;
		AppCommon.isWindows = !AppCommon.isMac;

		if (AppCommon.isMac) {
			AppBasic.useMacButtonOrder = true;
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		// Apply to basics
		AppBasic.isNotMac = AppCommon.isNotMac;
	}

}
