package script.config;

import java.util.ArrayList;
import java.util.List;

public class BasicConfig {

	public static List<String> scriptFileEndings = new ArrayList<>();
	public static List<String> libraryFileEndings = new ArrayList<>();

	static {
		scriptFileEndings.add("java");
		scriptFileEndings.add("groovy");

		libraryFileEndings.add("jar");
	}
}
