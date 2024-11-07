package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Rectangle;
import javafx.scene.input.KeyCode;

public class RectDrawer extends Drawer {
    private Point rectCenter;

    public RectDrawer(DrawingContext context) {
        super(context);
    }

    public void drawBy2Points() {
        DrawerByPoints drawer = new DrawerByPoints(drawingContext, 2) {
            @Override
            public void drawFigure(Point[] points) {
                Rectangle rectangle = new Rectangle(points[0], points[1]);
                workSpace.getChildren().add(rectangle);
            }
        };

        drawer.setupDrawing();
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
    }
}
