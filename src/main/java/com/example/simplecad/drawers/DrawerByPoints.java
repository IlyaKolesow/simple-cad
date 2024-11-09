package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class DrawerByPoints extends Drawer {
    private final Point[] points;

    public DrawerByPoints(DrawingContext context, int numberOfPoints) {
        super(context);
        points = new Point[numberOfPoints];
    }

    public void setupDrawing() {
        setPrompts("Укажите координаты точки 1", "X", "Y");

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                drawNextPoint(e.getX(), e.getY());
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
                double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
                drawNextPoint(x, y);
            }
        });
    }

    public void drawNextPoint(double x, double y) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = new Point(x, y);

                if (i == points.length - 1) {
                    drawFigure(points);
                    setPrompts("Укажите координаты точки 1", "X", "Y");

                    for (int j = 0; j < points.length; j++) {
                        workSpace.getChildren().remove(points[j]);
                        points[j] = null;
                    }
                } else {
                    workSpace.getChildren().add(points[i]);
                    setPrompts("Укажите координаты точки " + (i + 2), "X", "Y");
                }
                break;
            }
        }
    }

    public abstract void drawFigure(Point[] points);
}
