package script.app;

import java.io.File;

public class App {

    public static File getLogFile() {
        return new File(getConfigDir(), "messages.log");
    }

    public static File getConfigDir() {
        return new File(System.getProperty("user.home"), ".scriptrun");
    }

    public static File getScriptRecentDir() {
        return new File(getConfigDir(), "recent");
    }
}
