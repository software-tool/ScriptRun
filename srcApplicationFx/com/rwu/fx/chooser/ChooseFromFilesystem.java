package com.rwu.fx.chooser;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChooseFromFilesystem {

    /**
     * Open dialog to select directory
     */
    public static File browseForDirectory(Stage stage, String title, File dirPreselect) {
        DirectoryChooser fileChooser = initDirectoryChooser(title, dirPreselect);

        File selectedFile = fileChooser.showDialog(stage);
        return selectedFile;
    }

    /**
     * Init of chooser
     */
    private static DirectoryChooser initDirectoryChooser(String title, File pathPreset) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);

        if (pathPreset != null) {
            pathPreset = pathPreset.getAbsoluteFile();

            if (!pathPreset.isDirectory()) {
                pathPreset = pathPreset.getParentFile();
            }

            if (pathPreset != null && pathPreset.isDirectory()) {
                chooser.setInitialDirectory(pathPreset);
            }
        }

        return chooser;
    }

}
