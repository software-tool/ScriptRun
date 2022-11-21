package script.util;

import com.rwu.log.Log;
import script.app.Environment;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class SystemUtil {

    public static void browse(URI uri) {
        boolean done = browse(uri, false);
        if (done) {
            return;
        }

        File fileWithFullPath = null;

        // 2) Use only end part

        String path = uri.getPath();
        String userLocation = path.substring(path.lastIndexOf("/") + 1);

        URI uriSimple = null;
        try {
            // URISyntaxException possible
            uriSimple = new URI(userLocation);

            done = browse(uriSimple, false);
        } catch (URISyntaxException e) {
            // Ignore
        }

        if (done) {
            return;
        }
    }

    private static boolean browse(URI uri, boolean reportWarning) {
        if (Environment.isLinux()) {
            return browseInThread(uri, reportWarning);
        } else {
            return browseSync(uri, reportWarning);
        }
    }

    /**
     * Browsing (URL in Web Browser)
     *
     * Works for:
     * - Windows
     * - MacOS
     */
    public static boolean browseSync(URI uri, boolean reportWarning) {
        try {
            Desktop.getDesktop().browse(uri);

            return true;
        } catch (Exception e) {
            if (reportWarning) {
                //Log.error("Error opening URI", e);

                Log.warn("Error opening uri: " + uri, e);
            }

            return false;
        }
    }

    private static boolean browseInThread(final URI uri, final boolean reportWarning) {
        try {
            Thread openFileThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Desktop.getDesktop().browse(uri);
                    } catch (Exception e) {
                        if (reportWarning) {
                            Log.warn("Error opening URI: " + uri, e);
                        }
                    }
                }
            });
            openFileThread.start();

            return true;
        } catch (Exception e) {
            if (reportWarning) {
                Log.warn("Error opening URI in thread" + uri, e);
            }

            return false;
        }
    }

}
