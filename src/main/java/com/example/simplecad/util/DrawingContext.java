package com.example.simplecad.util;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class DrawingContext {
    private final Pane workSpace;
    private final ToolBar toolBar;
    private final Label inputLabel;
    private final TextField input1, input2, input3;
    private final Label prompt1, prompt2, prompt3;
    private final Point coordsCenter;
    private final CustomCursor cursor;
    private final ComboBox<Mode> modesComboBox;
    private final EventHandler<MouseEvent> defaultMouseMovedHandler;
    private double scale;

    public DrawingContext(Pane workSpace, ToolBar toolBar, EventHandler<MouseEvent> defaultMouseMovedHandler) {
        this.workSpace = workSpace;
        this.toolBar = toolBar;
        this.defaultMouseMovedHandler = defaultMouseMovedHandler;

        coordsCenter = (Point) findById(workSpace, "center");
        cursor = (CustomCursor) findById(workSpace, "cursor");

        modesComboBox = (ComboBox<Mode>) toolBar.getItems().get(0);
        inputLabel = (Label) toolBar.getItems().get(1);
        HBox hBox1 = (HBox) toolBar.getItems().get(2);
        HBox hBox2 = (HBox) toolBar.getItems().get(3);
        HBox hBox3 = (HBox) toolBar.getItems().get(4);
        prompt1 = (Label) hBox1.getChildren().get(0);
        prompt2 = (Label) hBox2.getChildren().get(0);
        prompt3 = (Label) hBox3.getChildren().get(0);
        input1 = (TextField) hBox1.getChildren().get(1);
        input2 = (TextField) hBox2.getChildren().get(1);
        input3 = (TextField) hBox3.getChildren().get(1);
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

    public ToolBar getToolBar() {
        return toolBar;
    }

    public Label getInputLabel() {
        return inputLabel;
    }

    public TextField getInput1() {
        return input1;
    }

    public TextField getInput2() {
        return input2;
    }

    public Point getCoordsCenter() {
        return coordsCenter;
    }

    public Label getPrompt1() {
        return prompt1;
    }

    public Label getPrompt2() {
        return prompt2;
    }

    public CustomCursor getCursor() {
        return cursor;
    }

    public ComboBox<Mode> getModesComboBox() {
        return modesComboBox;
    }

    public EventHandler<MouseEvent> getDefaultMouseMovedHandler() {
        return defaultMouseMovedHandler;
    }

    public Label getPrompt3() {
        return prompt3;
    }

    public TextField getInput3() {
        return input3;
    }
}
