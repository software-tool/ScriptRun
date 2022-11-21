package script.data;

import java.util.ArrayList;
import java.util.List;

import script.config.entities.DirectoryConfig;

public class DirectoryGroup {

	private String tag;

	private List<DirectoryConfig> configs = new ArrayList<>();

	public DirectoryGroup(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public List<DirectoryConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<DirectoryConfig> configs) {
		this.configs = configs;
	}

}
