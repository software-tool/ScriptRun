package script.frame.drop;

import java.io.File;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import script.config.entities.DirectoryConfig;
import script.controller.ControllerPages;
import script.controller.ControllerPath;
import script.controller.ControllerScripts;
import script.data.Script;
import script.manager.DirectoryConfigManager;

public class FrameDrop {

	/**
	 * File-Drop in Window
	 */
	public static void initFileDrop(Scene scene) {
		scene.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getDragboard().hasFiles()) {
					List<File> files = event.getDragboard().getFiles();

					handleDrop(files);
				}

				event.consume();
			}
		});

		scene.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getDragboard().hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				}

				event.consume();
			}
		});
	}

	private static void handleDrop(List<File> files) {
		for (File file : files) {
			if (file.isDirectory()) {
				DirectoryConfig config = DirectoryConfigManager.addScriptPath(file);

				ControllerPath.setCurrentDirectory(config.getDirectory(), false);

				ControllerPages.updateScriptDirectories();
			} else if (file.isFile()) {
				Script script = new Script(file.getParentFile(), file.getName());

				ControllerScripts.openDetails(script, false, false);
			}
		}
	}
}
