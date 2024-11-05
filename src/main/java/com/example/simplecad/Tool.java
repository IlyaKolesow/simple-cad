package com.example.simplecad;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class Tool {
    private final Pane workSpace;
    private double scale;

    public Tool(Pane workSpace) {
        this.workSpace = workSpace;
        scale = 1;
        pan(MouseButton.MIDDLE);
        zoomByScroll();
    }

    public void pan(MouseButton mouseButton) {
        final double[] start = new double[2];

        CustomCursor cursor = (CustomCursor) workSpace.getChildren()
                .stream()
                .filter(elem -> Objects.equals(elem.getId(), "cursor"))
                .findFirst().get();

        workSpace.setOnMousePressed(e -> {
            if (e.getButton() == mouseButton) {
                start[0] = e.getX();
                start[1] = e.getY();
            }
        });

        workSpace.setOnMouseDragged(e -> {
            cursor.update(e);

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
        Point center = (Point) workSpace.getChildren()
                .stream()
                .filter(elem -> Objects.equals(elem.getId(), "center"))
                .findFirst().get();

        workSpace.getChildren().forEach(elem -> {
            if (elem instanceof Figure)
                ((Figure) elem).scale(zoomCoef, center);
        });

        scale *= zoomCoef;
    }

    public void updateCoords(MouseEvent e, Label mouseX, Label mouseY) {
        Point center = (Point) workSpace.getChildren()
                .stream()
                .filter(elem -> Objects.equals(elem.getId(), "center"))
                .findFirst().get();

        mouseX.setText("X: " + String.format("%.1f", (e.getX() - center.getX()) / scale));
        mouseY.setText("Y: " + String.format("%.1f", (center.getY() - e.getY()) / scale));
    }
}
