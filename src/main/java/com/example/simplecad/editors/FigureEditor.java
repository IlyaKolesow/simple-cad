package com.example.simplecad.editors;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.InputModifiableFigure;
import com.example.simplecad.figures.Spline;
import com.example.simplecad.modes.LineType;
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

    public void inputBarInit() {
        if (figure instanceof InputModifiableFigure)
            showCoords();

        if (figure instanceof Spline)
            new SplineEditor(context, figure).pointMovement();

        showLineType();

        Button applyBtn = inputBuilder.addApplyButton();

        applyBtn.setOnAction(e -> applyInputs());
        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                applyInputs();
        });
    }

    private void showCoords() {
        Map<String, Double> prompts = ((InputModifiableFigure) figure).getCoords(center);
        inputBuilder.setPrompts(figure.getName(), prompts, scale);
    }

    private void showLineType() {
        inputBuilder.setLabel(figure.getName());
        lineTypes = inputBuilder.setLineType(figure.getThickness(), figure.getLineType().getDashSpace());
        lineTypes.getItems().addAll(LineType.SOLID, LineType.DASHED, LineType.DASH_DOT, LineType.DASH_DOT_DOT);
        lineTypes.setValue(figure.getLineType());
        lineTypes.setOnAction(e -> {
            LineType lineType = lineTypes.getValue();
            inputBuilder.setLineType(figure.getThickness(), lineType.getDashSpace());
            figure.setLineType(lineType, scale);
        });
    }

    private void applyInputs() {
        if (figure instanceof InputModifiableFigure f) {
            List<Double> values = inputBuilder.readInputValues().stream()
                    .map(value -> value * context.getScale())
                    .toList();
            f.setCoords(values, center);
        }

        figure.setThickness(inputBuilder.getThicknessValue());
        LineType lineType = figure.getLineType();
        lineType.setDashSpace(inputBuilder.getDashSpace());
        figure.setLineType(lineType, context.getScale());
    }
}
