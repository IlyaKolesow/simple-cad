package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Bezier;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.QuadSpline;
import com.example.simplecad.figures.Spline;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class SplineDrawer extends FigureDrawer {
    private final List<Point> points = new ArrayList<>();
    private Spline spline;

    public SplineDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.QUAD_SPLINE, Mode.BEZIER);
        modes.setValue(Mode.QUAD_SPLINE);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case QUAD_SPLINE:
                drawSpline(Mode.QUAD_SPLINE);
                break;
            case BEZIER:
                drawSpline(Mode.BEZIER);
                break;
        }
    }

    private void drawSpline(Mode mode) {
        points.clear();
        if (mode == Mode.QUAD_SPLINE)
            spline = new QuadSpline();
        else
            spline = new Bezier();

        workSpace.getChildren().add(spline);
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
