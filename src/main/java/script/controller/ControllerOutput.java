package script.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

import script.frame.PaneMain;
import script.frame.state.ExecutionPanes;
import script.state.RunningState;

public class ControllerOutput {

	/**
	 * Redirect Output
	 * 
	 * Redirect:
	 * https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
	 */
	public static void init() {
		// Globale Ausgabe

		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				writeToDestination(b);
			}

			private void writeToDestination(int b) {
				if (PaneMain.inst == null) {
					return;
				}

				String text = String.valueOf((char)b);
				PaneMain.inst.reportOutput(ExecutionPanes.getActiveScriptId(), text);
			}
		};

		System.setOut(new DelegatingPrintStream(false, os));
		System.setErr(new DelegatingPrintStream(true, os));
	}

	private static class DelegatingPrintStream extends PrintStream {

		private boolean error;

		public DelegatingPrintStream(boolean error, OutputStream out) {
			super(out);

			this.error = error;
		}
	}
}
