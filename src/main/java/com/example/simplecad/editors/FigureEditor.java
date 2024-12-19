package com.example.simplecad.editors;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FigureEditor {
    protected final DrawingContext context;
    protected final ToolBar toolBar;
    protected final Figure figure;
    protected final double scale;
    protected final Point center;

    public FigureEditor(DrawingContext context, Figure figure) {
        this.context = context;
        this.figure = figure;
        this.toolBar = context.getInputTool();
        this.scale = context.getScale();
        this.center = context.getCoordsCenter();
    }

    public void toolBarInit() {
        List<TextField> inputs = new ArrayList<>();
        Label figureName = new Label(figure.getName());

        toolBar.getItems().clear();
        toolBar.getItems().add(figureName);

        for (Map.Entry<String, Double> entry : figure.getValuesForOutput(center).entrySet()) {
            Label label = new Label(entry.getKey() + ":");
            TextField input = new TextField(String.format("%.1f", entry.getValue() / scale));
            inputs.add(input);
            HBox hBox = new HBox(label, input);

            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            input.setMaxWidth(50);

            toolBar.getItems().add(hBox);
        }

        Button button = new Button("Применить");
        toolBar.getItems().add(button);

        button.setOnAction(e -> applyInputs(inputs));
        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                applyInputs(inputs);
        });
    }

    private void applyInputs(List<TextField> inputs) {
        List<Double> values = new ArrayList<>();
        for (TextField input : inputs) {
            try {
                // Double parse вместо NumberFormat
                double value = NumberFormat.getInstance().parse(input.getText()).doubleValue() * scale;
                values.add(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        figure.setValuesFromInputs(values, center);
    }
}
