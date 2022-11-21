package xml;

import com.rwu.log.Log;

public class LogHandler {

	/**
	 * For testing only
	 */
	@Deprecated
	public static void tmp(String text) {
		Log.logReceiver.tmp(text);
	}

	public static void info(String text) {
		Log.logReceiver.info(text);
	}

	public static void warn(String text) {
		Log.logReceiver.warn(text);
	}

	/**
	 * Warning, only message
	 */
	public static void warn(String text, Throwable e) {
		Log.logReceiver.warn(text, e);
	}

	public static void error(Throwable e) {
		Log.logReceiver.error(e);
	}

	public static void error(String text, Throwable e) {
		Log.logReceiver.error(text, e);
	}

}
