package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;

public abstract class FigureDrawer extends Drawer {
    public FigureDrawer(DrawingContext context) {
        super(context);
        modesComboBox.setOnAction(null);
        modesComboBox.getItems().clear();
        input1.setText(null);
        input2.setText(null);
        input3.setText(null);
    }

    public abstract void startDrawing();
}
