package script.groovy;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;

/**
 * Class Loader to restrict the loaded classes
 */
public class MyGroovyClassLoader extends GroovyClassLoader {

	private static final String PREFIX_BLOCKED = "com.somepackage";

	@Override
	public Class loadClass(String name, boolean arg1, boolean arg2, boolean arg3) throws ClassNotFoundException, CompilationFailedException {
		//System.out.println("name1: " + name);

		if (name.startsWith(PREFIX_BLOCKED)) {
			throw new ClassNotFoundException("Not allowed to access this class");
		}

		return super.loadClass(name, arg1, arg2, arg3);
	}

	@Override
	public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript) throws ClassNotFoundException, CompilationFailedException {
		//System.out.println("name2: " + name);

		if (name.startsWith(PREFIX_BLOCKED)) {
			throw new ClassNotFoundException("Not allowed to access this class");
		}

		return super.loadClass(name, lookupScriptFiles, preferClassOverScript);
	}

	@Override
	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		//System.out.println("name3: " + name);

		if (name.startsWith(PREFIX_BLOCKED)) {
			throw new ClassNotFoundException("Not allowed to access this class");
		}

		return super.loadClass(name, resolve);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		//System.out.println("name4: " + name);

		if (name.startsWith(PREFIX_BLOCKED)) {
			throw new ClassNotFoundException("Not allowed to access this class");
		}

		return super.loadClass(name);
	}

}
