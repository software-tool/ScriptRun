package com.rwu.application.lang.generic;

public interface ILangProvider {

	public String get(String key);

	public String get(String key, String parameter1);

	public String get(String key, String parameter1, String parameter2);
}
