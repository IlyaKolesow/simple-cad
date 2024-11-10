package com.example.simplecad;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DrawingTool {
    private final DrawingContext context;
    private final Pane workSpace;
    private final Point center;
    private double scale;

    public DrawingTool(DrawingContext context) {
        this.context = context;
        workSpace = context.getWorkSpace();
        scale = context.getScale();
        center = context.getCoordsCenter();
        pan(MouseButton.MIDDLE);
        zoomByScroll();
    }

    public void pan(MouseButton mouseButton) {
        final double[] start = new double[2];

        workSpace.setOnMousePressed(e -> {
            if (e.getButton() == mouseButton) {
                start[0] = e.getX();
                start[1] = e.getY();
            }
        });

        workSpace.setOnMouseDragged(e -> {
            context.getCursor().update(e);

            if (e.getButton() == mouseButton) {
                double deltaX = e.getX() - start[0];
                double deltaY = e.getY() - start[1];

                workSpace.getChildren().forEach(elem -> {
                    if (elem instanceof Figure)
                        ((Figure) elem).move(deltaX, deltaY);
                });

                start[0] = e.getX();
                start[1] = e.getY();
            }
        });
    }

    public void zoomByScroll() {
        workSpace.setOnScroll(e -> {
            double wheelDirection = e.getDeltaY();
            double zoomCoef;

            zoomCoef = 1.1;
            if (wheelDirection < 0)
                zoomCoef = 0.9;

            zoom(zoomCoef);
        });
    }

    public void zoom(double zoomCoef) {
        workSpace.getChildren().forEach(elem -> {
            if (elem instanceof Figure)
                ((Figure) elem).scale(zoomCoef, center);
        });

        scale *= zoomCoef;
        context.setScale(scale);
    }

    public void updateCoords(MouseEvent e, Label mouseX, Label mouseY) {
        mouseX.setText("X: " + String.format("%.1f", (e.getX() - center.getX()) / scale));
        mouseY.setText("Y: " + String.format("%.1f", (center.getY() - e.getY()) / scale));
    }
}
