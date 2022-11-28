package script.metadata.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.rwu.log.Log;

import script.input.InputConfig;
import script.metadata.file.reader.FileMetadataReader;

public class FileMetadataManager {

	public static FileMetadata getCombined(String path) {
		FileMetadata read = null;
		try {
			read = FileMetadataReader.read(new File(path));
		} catch (Exception e) {
			Log.warn("Failed to read metadata from file", e);
		}

		if (read == null) {
			read = new FileMetadata(Arrays.asList(path));
		}

		FileMetadata fromRegistry = FileMetadataFromRegistryManager.get(path);
		applyAdditional(read, fromRegistry);

		return read;
	}

	private static void applyAdditional(FileMetadata userDefined, FileMetadata additional) {
		if (additional == null) {
			return;
		}

		if (userDefined.getTitle() == null) {
			userDefined.setTitle(additional.getTitle());
		}
		if (userDefined.getDescription() == null) {
			userDefined.setDescription(additional.getDescription());
		}

		// Seconds
		//userDefined.setRepeatSeconds(additional.getRepeatSeconds());


	}

	private static boolean hasField(List<InputConfig> fields, InputConfig toFind) {
		return getField(fields, toFind) != null;
	}

	private static InputConfig getField(List<InputConfig> fields, InputConfig toFind) {
		for(InputConfig field : fields) {
			if (field.equalsOtherField(toFind)) {
				return field;
			}
		}

		return null;
	}

	@Deprecated
	public static void setIntervalSeconds(String path, int repeatSeconds) {
		//FileMetadata custom = FileMetadataFromRegistryManager.getOrCreate(path);
		//custom.setRepeatSeconds(repeatSeconds);

		// Save
		//FileMetadataFromRegistryManager.store();
	}

	@Deprecated
	public static void setTitle(String path, String title) {
		//FileMetadata custom = FileMetadataFromRegistryManager.getOrCreate(path);
		//custom.setTitle(title);

		// Save
		//FileMetadataFromRegistryManager.store();
	}

}
