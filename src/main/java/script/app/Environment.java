package script.app;

import org.apache.commons.lang3.SystemUtils;

public class Environment {

    public static boolean isLinux() {
        //return true;
        return SystemUtils.IS_OS_LINUX;
    }

}
