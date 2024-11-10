package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Rectangle;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;

public class RectDrawer extends FigureDrawer {
    private Point rectCenter;

    public RectDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.BY_2_POINTS, Mode.BY_SIDES);
        modesComboBox.setValue(Mode.BY_2_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case BY_2_POINTS:
                drawBy2Points();
                break;
            case BY_SIDES:
                drawBy2Sides();
                break;
        }
    }

    public void drawBy2Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, 2) {
            @Override
            public void drawFigure(Point[] points) {
                Rectangle rectangle = new Rectangle(points[0], points[1]);
                workSpace.getChildren().add(rectangle);
            }
        };

        byPoints.setupDrawing();
    }

    public void drawBy2Sides() {
        setPrompts("Укажите координаты центральной точки", "X", "Y");

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double param1 = Double.parseDouble(input1.getText()) * drawingContext.getScale();
                double param2 = Double.parseDouble(input2.getText()) * drawingContext.getScale();
                if (rectCenter != null) {
                    Rectangle rectangle = new Rectangle(rectCenter, param1, param2);
                    workSpace.getChildren().add(rectangle);
                    workSpace.getChildren().remove(rectCenter);
                    rectCenter = null;
                    setPrompts("Укажите координаты центральной точки", "X", "Y");
                } else {
                    double x = coordsCenter.getX() + param1;
                    double y = coordsCenter.getY() - param2;
                    rectCenter = new Point(x, y);
                    workSpace.getChildren().add(rectCenter);
                    setPrompts("Укажите размеры сторон", "Длина", "Высота");
                }
            }
        });

        workSpace.setOnMouseClicked(null);
    }
}
