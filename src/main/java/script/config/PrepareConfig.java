package script.config;

import com.rwu.application.config.AppConfig;
import script.groups.GroupsConfig;
import script.metadata.directory.DirectoryMetadataInRegistry;
import script.metadata.file.FileMetadataInRegistry;
import script.recent.PrepareRecent;

public class PrepareConfig {

	public static void prepare() {
		AppConfig.initNode(ConfigConstants.BASIC, PrepareConfig.class);
		AppConfig.initNode(ConfigConstants.RECENT, PrepareRecent.class);
		AppConfig.initNode(ConfigConstants.FILE_METADATA, FileMetadataInRegistry.class);
		AppConfig.initNode(ConfigConstants.DIRECTORY_METADATA, DirectoryMetadataInRegistry.class);
		AppConfig.initNode(ConfigConstants.GROUPS, GroupsConfig.class);
	}

}
