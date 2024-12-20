package com.example.simplecad.editors;

import com.example.simplecad.LineType;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.util.InputBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Map;

public class FigureEditor {
    private final DrawingContext context;
    private final ToolBar toolBar;
    private final Figure figure;
    private final double scale;
    private final Point center;
    private final InputBuilder inputBuilder;
    private ComboBox<LineType> lineTypes;

    public FigureEditor(DrawingContext context, Figure figure) {
        this.context = context;
        this.figure = figure;
        this.toolBar = context.getInputTool();
        this.scale = context.getScale();
        this.center = context.getCoordsCenter();
        inputBuilder = new InputBuilder(toolBar);
    }

    public void toolBarInit() {
        Map<String, Double> prompts = figure.getValuesForOutput(center);
        inputBuilder.setPrompts(figure.getName(), prompts, scale);
        inputBuilder.addThicknessInput(figure.getThickness());

        lineTypes = inputBuilder.addLineTypeSelection();
        lineTypes.getItems().addAll(LineType.SOLID, LineType.DASHED, LineType.DASH_DOT, LineType.DASH_DOT_DOT);
        lineTypes.setValue(figure.getLineType());
        lineTypes.setOnAction(e -> figure.setLineType(lineTypes.getValue(), scale));

        Button applyBtn = inputBuilder.addApplyButton();

        applyBtn.setOnAction(e -> applyInputs());
        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                applyInputs();
        });
    }

    private void applyInputs() {
        List<Double> values = inputBuilder.readInputValues().stream()
                .map(value -> value * context.getScale())
                .toList();
        figure.setValuesFromInputs(values, center);
        figure.setThickness(inputBuilder.getThicknessValue());
    }
}
