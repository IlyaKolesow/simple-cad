package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Rectangle;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;

import java.util.List;

public class RectDrawer extends FigureDrawer {
    private Point rectCenter;

    public RectDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.BY_2_POINTS, Mode.BY_SIDES);
        modes.setValue(Mode.BY_2_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case BY_2_POINTS:
                drawBy2Points();
                break;
            case BY_SIDES:
                drawBy2Sides();
                break;
        }
    }

    private void drawBy2Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, inputBuilder, 2) {
            @Override
            public Figure buildFigure(Point[] points) {
                return new Rectangle(points[0], points[1]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawBy2Sides() {
        inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y");

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                List<Double> inputs = inputBuilder.readInputValues();
                double param1 = inputs.get(0) * drawingContext.getScale();
                double param2 = inputs.get(1) * drawingContext.getScale();
                if (rectCenter != null) {
                    Rectangle rectangle = new Rectangle(rectCenter, param1, param2);
                    workSpace.getChildren().add(rectangle);
                    workSpace.getChildren().remove(rectCenter);
                    rectCenter = null;
                    inputBuilder.setPrompts("Укажите координаты центральной точки", "Y", "X");
                } else {
                    double x = coordsCenter.getX() + param1;
                    double y = coordsCenter.getY() - param2;
                    rectCenter = new Point(x, y);
                    workSpace.getChildren().add(rectCenter);
                    inputBuilder.setPrompts("Укажите размеры сторон", "Ширина", "Высота");
                }
            }
        });

        workSpace.setOnMouseClicked(null);
    }
}
