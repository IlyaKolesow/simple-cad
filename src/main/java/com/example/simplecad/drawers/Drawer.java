package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Point;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

public class Drawer {
    protected final Pane workSpace;
    protected final ToolBar toolBar;
    protected final Label inputLabel;
    protected final TextField input1, input2;
    private final Label prompt1, prompt2;
    protected final Point coordsCenter;
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
    }

    protected void setPrompts(String label, String prompt1, String prompt2) {
        inputLabel.setText(label + ":");
        input1.setText(null);
        input2.setText(null);
        this.prompt1.setText(prompt1 + ":");
        this.prompt2.setText(prompt2 + ":");
    }
}
