package com.example.simplecad.drawers;

import com.example.simplecad.modes.DrawingMode;
import com.example.simplecad.figures.Bezier;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.QuadSpline;
import com.example.simplecad.figures.Spline;
import com.example.simplecad.util.DrawingContext;

import java.util.ArrayList;
import java.util.List;

public class SplineDrawer extends FigureDrawer {
    private final List<Point> points = new ArrayList<>();
    private Spline spline;

    public SplineDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(DrawingMode.QUAD_SPLINE, DrawingMode.BEZIER);
        modes.setValue(DrawingMode.QUAD_SPLINE);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case QUAD_SPLINE:
                drawSpline(DrawingMode.QUAD_SPLINE);
                break;
            case BEZIER:
                drawSpline(DrawingMode.BEZIER);
                break;
        }
    }

    private void drawSpline(DrawingMode mode) {
        points.clear();
        if (mode == DrawingMode.QUAD_SPLINE)
            spline = new QuadSpline();
        else
            spline = new Bezier();

        workSpace.getChildren().add(spline);
        inputBuilder.setPrompts("Укажите координаты точки 1", "X", "Y");
        setInputHandlers(this::drawNextPoint);
    }

    private void drawNextPoint(double x, double y) {
        Point point = new Point(x, y);
        points.add(point);
        spline.addPoint(point);
        if (points.size() == 1)
            workSpace.getChildren().add(point);
        if (points.size() == 2)
            workSpace.getChildren().remove(points.getFirst());

        inputBuilder.setPrompts("Укажите координаты точки " + (points.size() + 1), "X", "Y");
    }
}
