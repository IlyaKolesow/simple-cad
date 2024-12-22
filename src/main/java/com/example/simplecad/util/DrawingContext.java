package com.example.simplecad.util;

import com.example.simplecad.figures.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

public class DrawingContext {
    private final Pane workspace;
    private final ToolBar inputTool;
    private final Point coordsCenter;
    private final CustomCursor cursor;
    private final EventHandler<MouseEvent> defaultMouseMovedHandler, defaultMouseClickedHandler, defaultMouseDraggedHandler, defaultMousePressedHandler;
    private double scale;

    public DrawingContext(Pane workspace, ToolBar inputTool, List<EventHandler<MouseEvent>> mouseHandlers) {
        this.workspace = workspace;
        this.inputTool = inputTool;
        this.defaultMouseMovedHandler = mouseHandlers.get(0);
        this.defaultMouseClickedHandler = mouseHandlers.get(1);;
        this.defaultMouseDraggedHandler = mouseHandlers.get(2);;
        this.defaultMousePressedHandler = mouseHandlers.get(3);;

        coordsCenter = (Point) findById(workspace, "center");
        cursor = (CustomCursor) findById(workspace, "cursor");

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

    public Pane getWorkspace() {
        return workspace;
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