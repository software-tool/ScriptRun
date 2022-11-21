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

		if (!FileMetadataFromRegistryManager.REGISTRY_STORAGE_DEACTIVATED) {
			FileMetadata fromRegistry = FileMetadataFromRegistryManager.get(path);
			applyAdditional(read, fromRegistry);
		}

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

		// Seconds (not in file)
		userDefined.setRepeatSeconds(additional.getRepeatSeconds());

		List<InputConfig> userFields = userDefined.getInputs();
		List<InputConfig> additionalFields = additional.getInputs();

		// Add fields
		for(InputConfig additionalField : additionalFields) {
			if (!hasField(userFields, additionalField)) {
				userFields.add(additionalField);
			}
		}

		// Sizes
		for(InputConfig additionalField : additionalFields) {
			InputConfig field = getField(userFields, additionalField);
			if (field == null) {
				continue;
			}

			field.setSize(additionalField.getSize());
		}
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
		if (FileMetadataFromRegistryManager.REGISTRY_STORAGE_DEACTIVATED) return;

		FileMetadata custom = FileMetadataFromRegistryManager.getOrCreate(path);
		custom.setRepeatSeconds(repeatSeconds);

		// Save
		FileMetadataFromRegistryManager.store();
	}

	@Deprecated
	public static void setTitle(String path, String title) {
		if (FileMetadataFromRegistryManager.REGISTRY_STORAGE_DEACTIVATED) return;

		FileMetadata custom = FileMetadataFromRegistryManager.getOrCreate(path);
		custom.setTitle(title);

		// Save
		FileMetadataFromRegistryManager.store();
	}

	@Deprecated
	private static void setInputs(String path, List<InputConfig> inputs) {
		if (FileMetadataFromRegistryManager.REGISTRY_STORAGE_DEACTIVATED) return;

		FileMetadata custom = FileMetadataFromRegistryManager.getOrCreate(path);
		custom.setInputs(inputs);

		// Save
		FileMetadataFromRegistryManager.store();
	}

}
