package script.log;

import com.rwu.log.ILogReceiver;

public class LogInTests implements ILogReceiver {
    @Override
    public void tmp(String text) {
        System.out.println("TMP: " + text);
    }

    @Override
    public void info(String text) {

    }

    @Override
    public void warn(String text) {

    }

    @Override
    public void warn(String text, Throwable e) {

    }

    @Override
    public void error(Throwable e) {

    }

    @Override
    public void error(String text, Throwable e) {

    }
}
