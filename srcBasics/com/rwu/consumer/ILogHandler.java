package com.rwu.consumer;

public interface ILogHandler {

	public static ILogHandler inst = null;

	public void warn(String text);
}
