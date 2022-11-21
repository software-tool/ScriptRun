package script.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import script.controller.ControllerRunning;
import script.execute.ScriptInst;
import script.execute.intf.IScriptImpl;
import script.manager.ScriptDirectoryReader;
import script.state.RunningState;
import script.util.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class GroovyScript implements IScriptImpl {

	private File file;
	private ScriptInst scriptInst;

	private GroovyShell shell;
	private Script script;

	//private GroovyOutPrintStream output = new GroovyOutPrintStream(System.out);

	public GroovyScript(File file, ScriptInst scriptInst) {
		this.file = file;
		this.scriptInst = scriptInst;
	}

	@Override
	public void prepare() {
		shell = configureShell();

		script = shell.parse(scriptInst.getScript());

		scriptInst.bindVariables(script);
		scriptInst.applyStatic(script);
	}

	@Override
	public void run() {
		RunningState.mostRecentInstId = scriptInst.getInstId();

		script.run();

		// Executed
		ControllerRunning.reportScriptFinished(scriptInst.getInstId());
	}

	/**
	 * Prepare shell
	 * 
	 * Bind-Info:
	 * https://stackoverflow.com/questions/17785911/how-to-catch-groovyshell-output
	 */
	private GroovyShell configureShell() {
		File parentFile = file.getParentFile();
		URL url = FileUtils.getUrl(parentFile);

		// Own Classloader
		MyGroovyClassLoader myClassLoader = new MyGroovyClassLoader();
		myClassLoader.addURL(url);

		// Libraries
		addLibraries(myClassLoader);

		Binding binding = new Binding();
		//binding.setProperty("out", output);

		//		binding.setProperty("java.lang.System$out", new MyPrintStream(System.out));
		//		binding.setProperty("System#out", new MyPrintStream(System.out));
		//		binding.setProperty("System.out", new MyPrintStream(System.out));
		//		binding.setProperty("java.lang.System.out", new MyPrintStream(System.out));

		// Shell
		GroovyShell shell = new GroovyShell(myClassLoader, binding);
		return shell;
	}

	/**
	 * Adds all libraries in the directory to the classpath (as URL)
	 */
	private void addLibraries(MyGroovyClassLoader myClassLoader) {
		File parentFile = file.getParentFile();

		List<File> libraries = ScriptDirectoryReader.getLibraryFilesOfDirectory(parentFile);
		for (File library : libraries) {
			URL url = FileUtils.getUrl(library);

			myClassLoader.addURL(url);
		}

		// Not necessary/has effect
		//myClassLoader.addClasspath(parentFile.getAbsolutePath());

	}

	@Override
	public boolean isValid() {
		return script != null;
	}

	/*private static class MyBufferedWriter extends BufferedWriter {
		public MyBufferedWriter(Writer out) {
			super(out);
		}
	}*/

	/*@Override
	public String getOutput() {
		return output.sw.toString();
	}*/

	// Using script manager, not so flexible
	/*StringWriter result = new StringWriter();
	
	ScriptEngineManager manager = new ScriptEngineManager();
	ScriptEngine engine = manager.getEngineByName("groovy");
	
	PrintWriter writer = new PrintWriter(result);
	engine.getContext().setWriter(writer);
	engine.getContext().setErrorWriter(writer);
	
	try {
		engine.eval(scriptInst.getScript());
	} catch (ScriptException e) {
		LogHandler.error("Failed to execute script", e);
	}
	
	System.out.println("result: " + result.toString());*/
}
