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
import javafx.scene.layout.Pane;

import java.util.Objects;

public class DrawingContext {
    private final Pane workSpace;
    private final ToolBar inputTool;
    private Label inputLabel;
    private TextField input1, input2, input3;
    private Label prompt1, prompt2, prompt3;
    private final Point coordsCenter;
    private final CustomCursor cursor;
    private ComboBox<Mode> modesComboBox;
    private final EventHandler<MouseEvent> defaultMouseMovedHandler;
    private final EventHandler<MouseEvent> defaultMouseClickedHandler;
    private double scale;

    public DrawingContext(Pane workSpace, ToolBar inputTool, EventHandler<MouseEvent> defaultMouseMovedHandler, EventHandler<MouseEvent> defaultMouseClickedHandler) {
        this.workSpace = workSpace;
        this.inputTool = inputTool;
        this.defaultMouseMovedHandler = defaultMouseMovedHandler;
        this.defaultMouseClickedHandler = defaultMouseClickedHandler;

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

    public EventHandler<MouseEvent> getDefaultMouseClickedHandler() {
        return defaultMouseClickedHandler;
    }

    public Label getPrompt3() {
        return prompt3;
    }

    public TextField getInput3() {
        return input3;
    }

    public void setInputLabel(Label inputLabel) {
        this.inputLabel = inputLabel;
    }

    public void setInput1(TextField input1) {
        this.input1 = input1;
    }

    public void setInput2(TextField input2) {
        this.input2 = input2;
    }

    public void setInput3(TextField input3) {
        this.input3 = input3;
    }

    public void setPrompt1(Label prompt1) {
        this.prompt1 = prompt1;
    }

    public void setPrompt2(Label prompt2) {
        this.prompt2 = prompt2;
    }

    public void setPrompt3(Label prompt3) {
        this.prompt3 = prompt3;
    }

    public void setModesComboBox(ComboBox<Mode> modesComboBox) {
        this.modesComboBox = modesComboBox;
    }
}
