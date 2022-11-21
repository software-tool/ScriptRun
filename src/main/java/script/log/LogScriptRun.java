package script.log;

import com.rwu.log.ILogReceiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class LogScriptRun implements ILogReceiver {

    // If true, stacktrace on warnings
    private static final boolean WARNINGS_WITH_STACKTRACE = false;

    private static String newline = "\n";
    private static File logFile = null;

    private static int FILE_SIZE_5MB = (5 * 1024 * 1024);

    private static Object lockObj = new Object();

    public LogScriptRun(String newlineNew, File logFileNew) {
        synchronized (lockObj) {
            newline = newlineNew;
            logFile = logFileNew;
        }
    }

    /**
     * Only for development
     */
    public void tmp(String text) {
        StringBuilder sb = new StringBuilder();

        sb.append(newline);
        sb.append("TMP: " + text);

        writeText(sb.toString());
    }

    public void info(String text) {
        StringBuilder sb = new StringBuilder();

        sb.append(new Date().toString() + "  ");
        sb.append("INFO: " + text);

        writeText(sb.toString());
    }

    public void warn(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(newline);
        sb.append(new Date().toString() + "  ");

        sb.append("WARNING: " + text);

        sb.append(newline);

        writeText(sb.toString());
    }

    /**
     * Output warning, only message of exception
     */
    public void warn(String text, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(newline);
        sb.append(new Date().toString() + "  ");

        sb.append("WARNING: " + text);

        sb.append(" " + e.getMessage());

        sb.append(newline);

        if (WARNINGS_WITH_STACKTRACE) {
            // Stacktrace

            StackTraceElement[] stack = e.getStackTrace();
            for (StackTraceElement curr : stack) {
                sb.append(curr.toString() + newline);
            }
        }

        sb.append(newline);

        writeText(sb.toString());
    }

    public void error(Throwable e) {
        error(null, e);
    }

    public void error(String text, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(newline);
        sb.append(new Date().toString() + "  ");

        if (text != null) {
            sb.append(text + "  ");
        } else {
            sb.append("(no text)" + "  ");
        }

        sb.append(e);

        // Stacktrace
        sb.append(newline);

        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement curr : stack) {
            sb.append(curr.toString() + newline);
        }

        sb.append(newline);

        writeText(sb.toString());
    }

    private static void writeText(String text) {
        synchronized (lockObj) {
            if (logFile == null) {
                //System.out.println("No log file defined");
                return;
            }

            File parent = logFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try (BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true))) {
                long fileLength = logFile.length();

                if (fileLength > FILE_SIZE_5MB) {
                    // File bigger than 5 MB

                    int i = 2;
                    File other = new File("debug" + i + ".log");
                    while (other.exists()) {
                        i++;

                        other = new File("debug" + i + ".log");
                    }

                    logFile.renameTo(other);
                }

                // Write file
                out.write(text);
            } catch (Exception ex) {
                System.err.println("Failed to write log file: " + ex.getMessage());
            }
        }
    }
}
