package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;

public class CircleDrawer extends FigureDrawer {

    public CircleDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.BY_RADIUS, Mode.BY_3_POINTS);
        modesComboBox.setValue(Mode.BY_RADIUS);
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case BY_RADIUS:
                drawByCenterAndRadius();
                break;
            case BY_3_POINTS:
                drawBy3Points();
                break;
        }
    }

    private void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, 3) {
            @Override
            protected Figure buildFigure(Point[] points) {
                return new Circle(points[0], points[1], points[2]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawByCenterAndRadius() {
        DrawerByRadius byRadius = new DrawerByRadius(drawingContext) {
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
                setPrompts("Укажите координаты центральной точки", "X", "Y", null);
            }

            @Override
            protected void setSecondActionPrompts() {
                setPrompts("Укажите радиус", "R", null, null);
            }
        };

        byRadius.setupDrawing();
    }
}
