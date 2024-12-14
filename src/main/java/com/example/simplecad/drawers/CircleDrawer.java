package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.drawers.helpers.DrawerByRadius;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;

public class CircleDrawer extends FigureDrawer {

    public CircleDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.BY_RADIUS, Mode.BY_3_POINTS);
        modes.setValue(Mode.BY_RADIUS);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case BY_RADIUS:
                drawByCenterAndRadius();
                break;
            case BY_3_POINTS:
                drawBy3Points();
                break;
        }
    }

    private void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, inputBuilder, 3) {
            @Override
            protected Figure buildFigure(Point[] points) {
                return new Circle(points[0], points[1], points[2]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawByCenterAndRadius() {
        DrawerByRadius byRadius = new DrawerByRadius(drawingContext, inputBuilder) {
            @Override
            protected Figure buildFigure(Point center, double radius) {
                return new Circle(center, radius);
            }

            @Override
            protected Figure buildFigure(Point center, Point vertex) {
                return new Circle(center, vertex);
            }

            @Override
            protected void setFirstActionPrompts() {
                inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y");
            }

            @Override
            protected void setSecondActionPrompts() {
                inputBuilder.setPrompts("Укажите радиус", "R");
            }
        };

        byRadius.setupDrawing();
    }
}
