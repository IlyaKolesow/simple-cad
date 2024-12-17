package com.example.simplecad.drawers.helpers;

import com.example.simplecad.drawers.Drawer;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.InputBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class DrawerByPoints extends Drawer {
    private final Point[] points;

    public DrawerByPoints(DrawingContext context, InputBuilder inputBuilder, int numberOfPoints) {
        super(context);
        points = new Point[numberOfPoints];
        this.inputBuilder = inputBuilder;
    }

    public void setupDrawing() {
        inputBuilder.setPrompts("Укажите координаты точки 1", "X", "Y");

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                drawNextPoint(e.getX(), e.getY());
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double x = (coordsCenter.getX() + Double.parseDouble(input(0).getText()) * drawingContext.getScale());
                double y = (coordsCenter.getY() - Double.parseDouble(input(1).getText()) * drawingContext.getScale());
                drawNextPoint(x, y);
            }
        });
    }

    private void drawNextPoint(double x, double y) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = new Point(x, y);

                if (i == points.length - 1) {
                    workSpace.getChildren().add(buildFigure(points));
                    inputBuilder.setPrompts("Укажите координаты точки 1", "X", "Y");

                    for (int j = 0; j < points.length; j++) {
                        workSpace.getChildren().remove(points[j]);
                        points[j] = null;
                    }
                } else {
                    workSpace.getChildren().add(points[i]);
                    inputBuilder.setPrompts("Укажите координаты точки " + (i + 2), "X", "Y");
                }
                break;
            }
        }
    }

    protected abstract Figure buildFigure(Point[] points);
}
