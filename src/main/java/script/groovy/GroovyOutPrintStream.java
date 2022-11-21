package script.groovy;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

public class GroovyOutPrintStream extends PrintStream {

    private StringWriter sw = new StringWriter();

    public GroovyOutPrintStream(OutputStream out) {
        super(out);
    }

    @Override
    public void println(String x) {
        // Not to System.out
        //super.println(x);

        //applyOutputLine(x);
    }

    /*private void applyOutput(String text) {
        ControllerRunning.reportOutput(GroovyScript.this.scriptInst.getInstId(), text);
    }

    private void applyOutputLine(String text) {
        ControllerRunning.reportOutput(GroovyScript.this.scriptInst.getInstId(), text + "\n");
    }*/
}