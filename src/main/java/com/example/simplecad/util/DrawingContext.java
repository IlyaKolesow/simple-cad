package com.example.simplecad.util;

import com.example.simplecad.figures.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class DrawingContext {
    private final Pane workSpace;
    private final ToolBar inputTool;
    private final Point coordsCenter;
    private final CustomCursor cursor;
    private final EventHandler<MouseEvent> defaultMouseMovedHandler, defaultMouseClickedHandler, defaultMouseDraggedHandler, defaultMousePressedHandler;
    private double scale;

    public DrawingContext(Pane workSpace, ToolBar inputTool, EventHandler<MouseEvent> defaultMouseMovedHandler, EventHandler<MouseEvent> defaultMouseClickedHandler, EventHandler<MouseEvent> defaultMouseDraggedHandler, EventHandler<MouseEvent> defaultMousePressedHandler) {
        this.workSpace = workSpace;
        this.inputTool = inputTool;
        this.defaultMouseMovedHandler = defaultMouseMovedHandler;
        this.defaultMouseClickedHandler = defaultMouseClickedHandler;
        this.defaultMouseDraggedHandler = defaultMouseDraggedHandler;
        this.defaultMousePressedHandler = defaultMousePressedHandler;

        coordsCenter = (Point) findById(workSpace, "center");
        cursor = (CustomCursor) findById(workSpace, "cursor");

        scale = 1;
    }

    private Node findById(Pane workSpace, String id) {
        return workSpace.getChildren().stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public Pane getWorkSpace() {
        return workSpace;
    }

    public ToolBar getInputTool() {
        return inputTool;
    }

    public Point getCoordsCenter() {
        return coordsCenter;
    }

    public CustomCursor getCursor() {
        return cursor;
    }

    public EventHandler<MouseEvent> getDefaultMouseMovedHandler() {
        return defaultMouseMovedHandler;
    }

    public EventHandler<MouseEvent> getDefaultMouseClickedHandler() {
        return defaultMouseClickedHandler;
    }

    public EventHandler<MouseEvent> getDefaultMouseDraggedHandler() {
        return defaultMouseDraggedHandler;
    }

    public EventHandler<MouseEvent> getDefaultMousePressedHandler() {
        return defaultMousePressedHandler;
    }
}
