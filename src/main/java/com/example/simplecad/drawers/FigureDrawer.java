package com.example.simplecad.drawers;

import com.example.simplecad.modes.DrawingMode;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.control.ComboBox;

public abstract class FigureDrawer extends Drawer {
    protected final ComboBox<DrawingMode> modes;

    public FigureDrawer(DrawingContext context) {
        super(context);
        modes = inputBuilder.addModeSelection();
        modes.setOnAction(e -> startDrawing());
    }

    public abstract void startDrawing();
}


