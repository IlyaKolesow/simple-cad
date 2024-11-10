package com.example.simplecad.drawers;

import com.example.simplecad.util.DrawingContext;

public abstract class FigureDrawer extends Drawer {
    public FigureDrawer(DrawingContext context) {
        super(context);
        modesComboBox.setOnAction(null);
        modesComboBox.getItems().clear();
    }

    public abstract void startDrawing();
}
