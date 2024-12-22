package com.example.simplecad.drawers;

import com.example.simplecad.modes.DrawingMode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;

import java.util.List;

public class LineDrawer extends FigureDrawer {
    private Point firstPoint;

    public LineDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(DrawingMode.BY_2_POINTS, DrawingMode.BY_ANGLE_LENGTH);
        modes.setValue(DrawingMode.BY_2_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case BY_2_POINTS:
                drawBy2Points();
                break;
            case BY_ANGLE_LENGTH:
                drawByAngleAndLength();
                break;
        }
    }

    private void drawBy2Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, inputBuilder, 2) {
            @Override
            public Figure buildFigure(Point[] points) {
                return new Line(points[0], points[1]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawByAngleAndLength() {
        inputBuilder.setPrompts("Укажите координаты первой точки", "X", "Y");

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                List<Double> inputs = inputBuilder.readInputValues();
                if (firstPoint != null) {
                    double angle = inputs.get(0);
                    double length = inputs.get(1) * drawingContext.getScale();
                    Line line = new Line(firstPoint, angle, length);
                    workSpace.getChildren().add(line);
                    firstPoint = null;
                    inputBuilder.setPrompts("Укажите координаты первой точки", "X", "Y");
                } else {
                    double x = coordsCenter.getX() + inputs.get(0) * drawingContext.getScale();
                    double y = coordsCenter.getY() - inputs.get(1) * drawingContext.getScale();
                    firstPoint = new Point(x, y);
                    workSpace.getChildren().add(firstPoint);
                    inputBuilder.setPrompts("Укажите угол и длину линии", "Угол", "Длина");
                }
            }
        });

        workSpace.setOnMouseClicked(null);
    }
}
