package xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.rwu.log.ILogReceiver;

public class LogDefaultImpl implements ILogReceiver {

	// If true: Warnings have stacktrace
	private static final boolean WARNINGS_WITH_STACKTRACE = false;

	private static String newline = "\n";
	private static File logFile = null;

	private static int FILE_SIZE_5MB = (5 * 1024 * 1024);

	private static Object lockObj = new Object();

	public LogDefaultImpl(String newlineNew, File logFileNew) {
		synchronized (lockObj) {
			newline = newlineNew;
			logFile = logFileNew;

			logFile.getParentFile().mkdirs();
		}
	}

	/**
	 * For testing only
	 */
	@Override
	@Deprecated
	public void tmp(String text) {
		StringBuilder sb = new StringBuilder();

		sb.append(newline);
		sb.append("TMP: " + text);
		//sb.append(newline);

		System.out.println(sb.toString());

		writeText(sb.toString());
	}

	@Override
	public void info(String text) {
		StringBuilder sb = new StringBuilder();

		sb.append(new Date().toString() + "  ");
		sb.append("INFO: " + text);

		System.out.println(sb.toString());
	}

	@Override
	public void warn(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append(newline);
		sb.append(new Date().toString() + "  ");

		sb.append("WARNING: " + text);

		System.err.println(sb.toString());

		sb.append(newline);

		writeText(sb.toString());
	}

	/**
	 * Warning, only message
	 */
	@Override
	public void warn(String text, Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(newline);
		sb.append(new Date().toString() + "  ");

		sb.append("WARNING: " + text);

		sb.append(" " + e.getMessage());

		sb.append(newline);

		if (WARNINGS_WITH_STACKTRACE) {
			// Stacktrace ausgeben

			StackTraceElement[] stack = e.getStackTrace();
			for (StackTraceElement curr : stack) {
				sb.append(curr.toString() + newline);
			}
		}

		System.err.println(sb.toString());

		sb.append(newline);

		writeText(sb.toString());
	}

	@Override
	public void error(Throwable e) {
		error(null, e);
	}

	@Override
	public void error(String text, Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(newline);
		sb.append(new Date().toString() + "  ");

		if (text != null) {
			sb.append(text + "  ");
		} else {
			sb.append("(no text)" + "  ");
		}

		// sb.append(e.getClass() + " " + e.getMessage());
		sb.append(e);

		System.err.println("ERROR: " + sb.toString());

		// Stacktrace
		sb.append(newline);

		StackTraceElement[] stack = e.getStackTrace();
		for (StackTraceElement curr : stack) {
			sb.append(curr.toString() + newline);
		}

		sb.append(newline);

		writeText(sb.toString());

		e.printStackTrace();

		boolean skip = false;
		if (e instanceof NullPointerException && stack[0].getMethodName().equals("isNavigationKey") && stack[0].getFileName().equals("BasicTreeUI.java")) {
			skip = true;
		}

		// For development
		// if (skip == false)
		// AppSpecific.showErrorMessageOnly(e, text);
		System.out.println("");
	}

	private static void writeText(String text) {
		synchronized (lockObj) {
			if (logFile == null) {
				System.out.println("No log file defined for logging");
				return;
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
