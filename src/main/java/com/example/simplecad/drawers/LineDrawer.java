package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;

public class LineDrawer extends FigureDrawer {
    private Point firstPoint;

    public LineDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.BY_2_POINTS, Mode.BY_ANGLE_LENGTH);
        modes.setValue(Mode.BY_2_POINTS);
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
                if (firstPoint != null) {
                    double angle = Double.parseDouble(inputBuilder.getInputs().get(0).getText());
                    double length = Double.parseDouble(inputBuilder.getInputs().get(1).getText()) * drawingContext.getScale();
                    Line line = new Line(firstPoint, angle, length);
                    workSpace.getChildren().add(line);
                    firstPoint = null;
                    inputBuilder.setPrompts("Укажите координаты первой точки", "X", "Y");
                } else {
                    double x = coordsCenter.getX() + Double.parseDouble(inputBuilder.getInputs().get(0).getText()) * drawingContext.getScale();
                    double y = coordsCenter.getY() - Double.parseDouble(inputBuilder.getInputs().get(1).getText()) * drawingContext.getScale();
                    firstPoint = new Point(x, y);
                    workSpace.getChildren().add(firstPoint);
                    inputBuilder.setPrompts("Укажите угол и длину линии", "Угол", "Длина");
                }
            }
        });

        workSpace.setOnMouseClicked(null);
    }
}
