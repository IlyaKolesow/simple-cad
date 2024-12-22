package com.example.simplecad.drawers;

import com.example.simplecad.modes.DrawingMode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Arc;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;

public class ArcDrawer extends FigureDrawer {
    private Point center;
    private Point startPoint;

    public ArcDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(DrawingMode.BY_3_POINTS, DrawingMode.CHORD);
        modes.setValue(DrawingMode.BY_3_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case BY_3_POINTS:
                drawBy3Points();
                break;
            case CHORD:
                drawByChord();
                break;
        }
    }

    private void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, inputBuilder, 3) {
            @Override
            public Figure buildFigure(Point[] points) {
                return new Arc(points[0], points[1], points[2]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawByChord() {
        inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y");
        setInputHandlers(this::drawNextPoint);
    }

    private void drawNextPoint(double x, double y) {
        if (startPoint != null) {
            Point endPoint = new Point(x, y);
            Arc arc = new Arc(center, new Line(startPoint, endPoint));
            workspace.getChildren().add(arc);
            workspace.getChildren().removeAll(startPoint, center);
            startPoint = null;
            center = null;
            inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y");
        } else if (center != null) {
            startPoint = new Point(x, y);
            workspace.getChildren().add(startPoint);
            inputBuilder.setPrompts("Укажите координаты второй точки хорды", "X", "Y");
        } else {
            center = new Point(x, y);
            workspace.getChildren().add(center);
            inputBuilder.setPrompts("Укажите координаты первой точки хорды", "X", "Y");
        }
    }
}
