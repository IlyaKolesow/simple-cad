package com.example.simplecad;

import com.example.simplecad.figures.Point;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class DrawingContext {
    private final Pane workSpace;
    private final ToolBar toolBar;
    private final Label inputLabel;
    private final TextField input1, input2;
    private final Label prompt1, prompt2;
    private final Point coordsCenter;
    private double scale;

    public DrawingContext(Pane workSpace, ToolBar toolBar) {
        this.workSpace = workSpace;
        this.toolBar = toolBar;

        coordsCenter = (Point) workSpace.getChildren().stream()
                .filter(e -> Objects.equals(e.getId(), "center"))
                .findFirst().get();

        inputLabel = (Label) toolBar.getItems().get(0);
        HBox hBox1 = (HBox) toolBar.getItems().get(1);
        HBox hBox2 = (HBox) toolBar.getItems().get(2);
        prompt1 = (Label) hBox1.getChildren().get(0);
        prompt2 = (Label) hBox2.getChildren().get(0);
        input1 = (TextField) hBox1.getChildren().get(1);
        input2 = (TextField) hBox2.getChildren().get(1);
        scale = 1;
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
}
