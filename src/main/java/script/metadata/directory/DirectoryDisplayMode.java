package script.metadata.directory;

public enum DirectoryDisplayMode {

	Small, Medium, Large;

	private static final String MODE_SMALL = "small";
	private static final String MODE_MEDIUM = "medium";

	public boolean isSmall() {
		return this == Small;
	}

	public boolean isMedium() {
		return this == Medium;
	}

	public boolean isLarge() {
		return this == Large;
	}

	public static DirectoryDisplayMode fromConfig(String configStr) {
		if (configStr == null) {
			return Large;
		}

		if (configStr.equals(MODE_SMALL)) {
			return Small;
		}

		if (configStr.equals(MODE_MEDIUM)) {
			return Medium;
		}

		return Large;
	}

	public static String toConfig(DirectoryDisplayMode mode) {
		if (mode == null) {
			return null;
		}

		if (mode.isSmall()) {
			return MODE_SMALL;
		}

		if (mode.isMedium()) {
			return MODE_MEDIUM;
		}

		return null;
	}
}
