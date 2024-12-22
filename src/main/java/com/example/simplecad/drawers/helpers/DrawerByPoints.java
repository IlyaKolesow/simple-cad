package com.example.simplecad.drawers.helpers;

import com.example.simplecad.drawers.Drawer;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.InputBuilder;

public abstract class DrawerByPoints extends Drawer {
    private final Point[] points;

    public DrawerByPoints(DrawingContext context, InputBuilder inputBuilder, int numberOfPoints) {
        super(context);
        points = new Point[numberOfPoints];
        this.inputBuilder = inputBuilder;
    }

    public void setupDrawing() {
        inputBuilder.setPrompts("Укажите координаты точки 1", "X", "Y");
        setInputHandlers(this::drawNextPoint);
    }

    private void drawNextPoint(double x, double y) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = new Point(x, y);

                if (i == points.length - 1) {
                    workspace.getChildren().add(buildFigure(points));
                    inputBuilder.setPrompts("Укажите координаты точки 1", "X", "Y");

                    for (int j = 0; j < points.length; j++) {
                        workspace.getChildren().remove(points[j]);
                        points[j] = null;
                    }
                } else {
                    workspace.getChildren().add(points[i]);
                    inputBuilder.setPrompts("Укажите координаты точки " + (i + 2), "X", "Y");
                }
                break;
            }
        }
    }

    protected abstract Figure buildFigure(Point[] points);
}
