package script.metadata.directory;

import script.metadata.file.ReadLibrariesMode;

public class DirectoryMetadata {

	private String path;

	private String title;
	private String description;

	private boolean isIncludeDirectory = false;

	// Display
	private DirectoryDisplayMode displayMode = null;

	// How libraries are searched
	private ReadLibrariesMode readLibrariesMode = ReadLibrariesMode.Directory;

	private boolean showAllFiles = false;

	public DirectoryMetadata(String path) {
		this.path = path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDisplayMode(DirectoryDisplayMode displayMode) {
		this.displayMode = displayMode;
	}

	public void setShowAllFiles(boolean showAllFiles) {
		this.showAllFiles = showAllFiles;
	}

	public String getPath() {
		return path;
	}

	public DirectoryDisplayMode getDisplayMode() {
		return displayMode;
	}

	public boolean getShowAllFiles() {
		return showAllFiles;
	}

	public ReadLibrariesMode getReadLibrariesMode() {
		return readLibrariesMode;
	}

	public void setReadLibrariesMode(ReadLibrariesMode readLibrariesMode) {
		this.readLibrariesMode = readLibrariesMode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIncludeDirectory() {
		return isIncludeDirectory;
	}

	public void setIncludeDirectory(boolean includeDirectory) {
		isIncludeDirectory = includeDirectory;
	}
}
