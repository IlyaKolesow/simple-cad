package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.InputBuilder;
import javafx.event.EventHandler;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Drawer {
    protected final Pane workSpace;
    protected final ToolBar toolBar;
//    protected Label inputLabel;
//    protected TextField input1, input2, input3;
//    protected Label prompt1, prompt2, prompt3;
    protected final Point coordsCenter;
//    protected ComboBox<Mode> modesComboBox;
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

//        inputLabel = context.getInputLabel();
//        input1 = context.getInput1();
//        input2 = context.getInput2();
//        input3 = context.getInput3();
//        prompt1 = context.getPrompt1();
//        prompt2 = context.getPrompt2();
//        prompt3 = context.getPrompt3();
//        modesComboBox = context.getModesComboBox();


//    protected void setPrompts(String label, String prompt1, String prompt2, String prompt3) {
//        if (Objects.equals(prompt3, null)) {
//            input3.setVisible(false);
//            this.prompt3.setVisible(false);
//        } else {
//            input3.setVisible(true);
//            this.prompt3.setVisible(true);
//        }
//
//        if (Objects.equals(prompt2, null)) {
//            input2.setVisible(false);
//            this.prompt2.setVisible(false);
//        } else {
//            input2.setVisible(true);
//            this.prompt2.setVisible(true);
//        }
//
//        inputLabel.setText(label + ":");
//        input1.setText(null);
//        input2.setText(null);
//        input3.setText(null);
//        this.prompt1.setText(prompt1 + ":");
//        this.prompt2.setText(prompt2 + ":");
//        this.prompt3.setText(prompt3 + ":");
//    }
}
