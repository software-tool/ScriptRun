package script.config.entities;

import java.util.ArrayList;
import java.util.List;

import script.data.ScriptDirectory;
import script.manager.DirectoryConfigManager;

public class DirectoryConfig {

	private ScriptDirectory directory;
	private List<String> tags = new ArrayList();

	// State
	private boolean collapsed = false;

	public DirectoryConfig(ScriptDirectory directory) {
		this.directory = directory;
	}

	public ScriptDirectory getDirectory() {
		return directory;
	}

	public void setDirectory(ScriptDirectory directory) {
		this.directory = directory;
	}

	public List<String> getTags() {
		return tags;
	}

	public boolean showInTag(String tag) {
		if (tags.isEmpty() && DirectoryConfigManager.DEFAULT_GROUP_NAME.equals(tag)) {
			return true;
		}

		return tags.contains(tag);
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void removeTag(String tag) {
		this.tags.remove(tag);
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
}
