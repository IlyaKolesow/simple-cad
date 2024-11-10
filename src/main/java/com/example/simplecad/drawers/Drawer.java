package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class Drawer {
    protected final Pane workSpace;
    protected final ToolBar toolBar;
    protected final Label inputLabel;
    protected final TextField input1, input2;
    protected final Label prompt1, prompt2;
    protected final Point coordsCenter;
    protected final ComboBox<Mode> modesComboBox;
    protected final EventHandler<MouseEvent> defaultMouseMovedHandler;
    protected final DrawingContext drawingContext;

    public Drawer(DrawingContext context) {
        this.drawingContext = context;
        workSpace = context.getWorkSpace();
        toolBar = context.getToolBar();
        coordsCenter = context.getCoordsCenter();
        inputLabel = context.getInputLabel();
        input1 = context.getInput1();
        input2 = context.getInput2();
        prompt1 = context.getPrompt1();
        prompt2 = context.getPrompt2();
        modesComboBox = context.getModesComboBox();
        defaultMouseMovedHandler = context.getDefaultMouseMovedHandler();
    }

    protected void setPrompts(String label, String prompt1, String prompt2) {
        if (Objects.equals(prompt2, null)) {
            input2.setVisible(false);
            this.prompt2.setVisible(false);
        } else {
            input2.setVisible(true);
            this.prompt2.setVisible(true);
        }

        inputLabel.setText(label + ":");
        input1.setText(null);
        input2.setText(null);
        this.prompt1.setText(prompt1 + ":");
        this.prompt2.setText(prompt2 + ":");
    }
}
