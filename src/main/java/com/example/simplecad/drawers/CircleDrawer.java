package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import javafx.scene.input.MouseButton;

public class CircleDrawer extends Drawer {
    public CircleDrawer(DrawingContext context) {
        super(context);
    }

    public void drawByCenterAndRadius() {
        final Point[] center = {null};
        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (center[0] != null) {
                    Circle circle = new Circle(center[0], calculateDistance(center[0], new Point(e.getX(), e.getY())));
                    workSpace.getChildren().add(circle);
                    center[0] = null;
                } else {
                    center[0] = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center[0]);
                }
            }
        });
    }

    public void drawBy3Points() {
        DrawerByPoints drawer = new DrawerByPoints(drawingContext, 3) {
            @Override
            public void drawFigure(Point[] points) {
                Circle circle = new Circle(points[0], points[1], points[2]);
                workSpace.getChildren().add(circle);
            }
        };

        drawer.setupDrawing();
    }

    private double calculateDistance(Point point1, Point point2) {
        double a = Math.abs(point1.getX() - point2.getX());
        double b = Math.abs(point1.getY() - point2.getY());
        return Math.sqrt(a * a + b * b);
    }
}
