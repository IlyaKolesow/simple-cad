package com.example.simplecad.util;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CustomCursor extends Group {
    private final Line line1;
    private final Line line2;

    public CustomCursor() {
        line1 = new Line();
        line2 = new Line();
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        getChildren().addAll(line1, line2);
    }

    public void update(MouseEvent e) {
        line1.setStartX(e.getX() - 30);
        line1.setStartY(e.getY());
        line1.setEndX(e.getX() + 30);
        line1.setEndY(e.getY());

        line2.setStartX(e.getX());
        line2.setStartY(e.getY() - 30);
        line2.setEndX(e.getX());
        line2.setEndY(e.getY() + 30);
    }
}
