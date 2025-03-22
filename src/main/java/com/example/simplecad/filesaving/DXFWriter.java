package com.example.simplecad.filesaving;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DXFWriter {
    private final Pane workspace;
    private File file;

    public DXFWriter(Pane workspace) {
        this.workspace = workspace;
    }

    public void save() {
        fileInit();
        if (file != null)
            generate();
    }

    private void fileInit() {
        Stage stage = (Stage) workspace.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение файла");
        fileChooser.setInitialFileName("чертеж");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файл DXF", "*.dxf")
        );
        file = fileChooser.showSaveDialog(stage);
    }

    private void generate() {

    }
}
