package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Arc;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.List;

public class ArcDrawer extends FigureDrawer {
    private Point center;
    private Point startPoint;

    public ArcDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.BY_3_POINTS, Mode.CHORD);
        modes.setValue(Mode.BY_3_POINTS);
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

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                drawNextPoint(e.getX(), e.getY());
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                List<Double> inputs = inputBuilder.readInputValues();
                double x = (coordsCenter.getX() + inputs.get(0) * drawingContext.getScale());
                double y = (coordsCenter.getY() - inputs.get(1) * drawingContext.getScale());
                drawNextPoint(x, y);
            }
        });
    }

    private void drawNextPoint(double x, double y) {
        if (startPoint != null) {
            Point endPoint = new Point(x, y);
            Arc arc = new Arc(center, new Line(startPoint, endPoint));
            workSpace.getChildren().add(arc);
            workSpace.getChildren().removeAll(startPoint, center);
            startPoint = null;
            center = null;
            inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y");
        } else if (center != null) {
            startPoint = new Point(x, y);
            workSpace.getChildren().add(startPoint);
            inputBuilder.setPrompts("Укажите координаты второй точки хорды", "X", "Y");
        } else {
            center = new Point(x, y);
            workSpace.getChildren().add(center);
            inputBuilder.setPrompts("Укажите координаты первой точки хорды", "X", "Y");
        }
    }
}
