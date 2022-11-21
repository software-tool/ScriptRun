package com.rwu.log;

public interface ILogReceiver {

    @Deprecated
    public void tmp(String text);
    public void info(String text);
    public void warn(String text);

    public void warn(String text, Throwable e);
    public void error(Throwable e);
    public void error(String text, Throwable e);

}
