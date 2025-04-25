package com.example.simplecad.dxf;

import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DXFWriter {
    private final Pane workspace;
    private File file;
    private final FigureWriter figureWriter;
    private final LineTypeWriter lineTypeWriter;
    private BufferedWriter bufferedWriter;

    public DXFWriter(DrawingContext context) {
        this.workspace = context.getWorkspace();
        figureWriter = new FigureWriter(context);
        lineTypeWriter = new LineTypeWriter();
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

        File defaultDirectory = new File("C:/Users/Илья/OneDrive/Рабочий стол/МАИ/Геометрическое моделирование");
        if (defaultDirectory.exists())
            fileChooser.setInitialDirectory(defaultDirectory);
        
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файл DXF", "*.dxf")
        );
        file = fileChooser.showSaveDialog(stage);
    }

    private void generate() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter = writer;
            writeHeader();
            writeTables();
            figureWriter.write(bufferedWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader() throws IOException {
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("SECTION\r\n");
        bufferedWriter.write("  2\r\n");
        bufferedWriter.write("HEADER\r\n");
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("ENDSEC\r\n");
    }

    private void writeTables() throws IOException {
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("SECTION\r\n");
        bufferedWriter.write("  2\r\n");
        bufferedWriter.write("TABLES\r\n");

        // Определение типов линий
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("TABLE\r\n");
        bufferedWriter.write("  2\r\n");
        bufferedWriter.write("LTYPE\r\n");

        lineTypeWriter.write(bufferedWriter);

        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("ENDTAB\r\n");

        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("ENDSEC\r\n");
    }
}
