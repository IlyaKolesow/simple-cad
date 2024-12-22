package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.InputBuilder;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.List;

public class Drawer {
    protected final Pane workspace;
    protected final ToolBar toolBar;
    protected final Point coordsCenter;
    protected final DrawingContext drawingContext;
    protected InputBuilder inputBuilder;

    public Drawer(DrawingContext context) {
        this.drawingContext = context;
        workspace = context.getWorkspace();
        toolBar = context.getInputTool();
        coordsCenter = context.getCoordsCenter();
        inputBuilder = new InputBuilder(toolBar);
    }

    protected interface PointHandler {
        void handle(double x, double y);
    }

    protected void setInputHandlers(PointHandler nextAction) {
        workspace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                nextAction.handle(e.getX(), e.getY());
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                List<Double> inputs = inputBuilder.readInputValues();
                double x = (coordsCenter.getX() + inputs.get(0) * drawingContext.getScale());
                double y = (coordsCenter.getY() - inputs.get(1) * drawingContext.getScale());
                nextAction.handle(x, y);
            }
        });
    }
}
