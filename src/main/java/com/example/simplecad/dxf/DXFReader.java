package com.example.simplecad.dxf;

import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DXFReader {
    private final Pane workspace;
    private File file;
    private final FigureReader figureReader;

    public DXFReader(DrawingContext context) {
        workspace = context.getWorkspace();
        figureReader = new FigureReader(context);
    }

    public void open() {
        fileInit();
        if (file != null) {
            try {
                readDxfFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fileInit() {
        Stage stage = (Stage) workspace.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открытие файла");

        File defaultDirectory = new File("C:/Users/Илья/OneDrive/Рабочий стол/МАИ/Геометрическое моделирование");
        if (defaultDirectory.exists())
            fileChooser.setInitialDirectory(defaultDirectory);

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файл DXF", "*.dxf")
        );
        file = fileChooser.showOpenDialog(stage);
    }

    private void readDxfFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            figureReader.read(reader);
        }
    }
}