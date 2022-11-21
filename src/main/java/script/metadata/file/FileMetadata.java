package script.metadata.file;

import java.util.ArrayList;
import java.util.List;

import script.input.InputConfig;

public class FileMetadata {

	// Path to file
	private List<String> paths = new ArrayList<>();

	// Description
	private String title = null;
	private String description = null;

	private int repeatSeconds = 10;

	// --- Libraries ---

	// Additional paths
	private List<String> extraClasspathUrls = new ArrayList<>();

	// --- Display ---

	private List<InputConfig> inputs = new ArrayList<>();

	private OutputTextDisplayMode textDisplayMode = OutputTextDisplayMode.HalfWidthMedium;

	public FileMetadata(List<String> paths) {
		this.paths.addAll(paths);
	}

	public boolean hasPath(String other) {
		return paths.contains(other);
	}

	public List<String> getPaths() {
		return paths;
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

	public List<String> getExtraClasspathUrls() {
		return extraClasspathUrls;
	}

	public void setExtraClasspathUrls(List<String> extraClasspathUrls) {
		this.extraClasspathUrls = extraClasspathUrls;
	}

	public OutputTextDisplayMode getTextDisplayMode() {
		return textDisplayMode;
	}

	public void setTextDisplayMode(OutputTextDisplayMode textDisplayMode) {
		this.textDisplayMode = textDisplayMode;
	}

	public int getRepeatSeconds() {
		return repeatSeconds;
	}

	public void setRepeatSeconds(int repeatSeconds) {
		this.repeatSeconds = repeatSeconds;
	}

	public List<InputConfig> getInputs() {
		return inputs;
	}

	public void setInputs(List<InputConfig> inputs) {
		this.inputs.clear();
		this.inputs.addAll(inputs);
	}

	public boolean hasContent() {
		if (title != null) {
			return true;
		}
		if (description != null) {
			return true;
		}

		if (!inputs.isEmpty()) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("FileCustom [path=%s, title=%s, description=%s, repeatSeconds=%s, extraClasspathUrls=%s, textDisplayMode=%s]", paths, title,
				description, repeatSeconds, extraClasspathUrls, textDisplayMode);
	}

}
