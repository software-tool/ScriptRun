package com.rwu.log;

public class Log {

	public static ILogReceiver logReceiver;

	public static void initLog(ILogReceiver receiverNew) {
		logReceiver = receiverNew;
	}

	public static void tmp(String text) {
		logReceiver.tmp(text);
	}

	public static void info(String text) {
		logReceiver.info(text);
	}

	public static void warn(String text) {
		logReceiver.warn(text);
	}

	public static void warn(String text, Throwable e) {
		logReceiver.warn(text, e);
	}

	public static void error(Throwable e) {
		logReceiver.error(e);
	}

	public static void error(String text, Throwable e) {
		logReceiver.error(text, e);
	}
}
