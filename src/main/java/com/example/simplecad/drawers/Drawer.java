package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.InputBuilder;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Drawer {
    protected final Pane workSpace;
    protected final ToolBar toolBar;
    protected final Point coordsCenter;
    protected final EventHandler<MouseEvent> defaultMouseMovedHandler;
    protected final DrawingContext drawingContext;
    protected InputBuilder inputBuilder;

    public Drawer(DrawingContext context) {
        this.drawingContext = context;
        workSpace = context.getWorkSpace();
        toolBar = context.getInputTool();
        coordsCenter = context.getCoordsCenter();
        inputBuilder = new InputBuilder(toolBar);
        defaultMouseMovedHandler = context.getDefaultMouseMovedHandler();
    }

    protected TextField input(int n) {
        return inputBuilder.getInputs().get(n);
    }
}
