package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public abstract class FigureDrawer extends Drawer {
    public FigureDrawer(DrawingContext context) {
        super(context);

        modesComboBox = new ComboBox<>();
        inputLabel = new Label();
        input1 = new TextField();
        input2 = new TextField();
        input3 = new TextField();
        prompt1 = new Label();
        prompt2 = new Label();
        prompt3 = new Label();
        HBox hBox1 = new HBox(prompt1, input1);
        HBox hBox2 = new HBox(prompt2, input2);
        HBox hBox3 = new HBox(prompt3, input3);

        modesComboBox.setOnAction(e -> startDrawing());

        toolBar.getItems().clear();
        toolBar.getItems().addAll(modesComboBox, inputLabel, hBox1, hBox2, hBox3);

        updateContext();
        setStyles(hBox1, hBox2, hBox3);
    }

    private void updateContext() {
        drawingContext.setInput1(input1);
        drawingContext.setInput2(input2);
        drawingContext.setInput3(input3);
        drawingContext.setInputLabel(inputLabel);
        drawingContext.setModesComboBox(modesComboBox);
        drawingContext.setPrompt1(prompt1);
        drawingContext.setPrompt2(prompt2);
        drawingContext.setPrompt3(prompt3);
    }

    private void setStyles(HBox hBox1, HBox hBox2, HBox hBox3) {
        hBox1.setSpacing(10);
        hBox2.setSpacing(10);
        hBox3.setSpacing(10);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox3.setAlignment(Pos.CENTER_LEFT);
        input1.setMaxWidth(50);
        input2.setMaxWidth(50);
        input3.setMaxWidth(50);
    }

    public abstract void startDrawing();
}
