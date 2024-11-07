package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class CircleDrawer extends Drawer {
    private Point center;

    public CircleDrawer(DrawingContext context) {
        super(context);
    }

    public void drawByCenterAndRadius() {
        setPrompts("Укажите координаты центральной точки", "X", "Y");

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (center != null) {
                    double radius = calculateDistance(center, new Point(e.getX(), e.getY()));
                    Circle circle = new Circle(center, radius);
                    workSpace.getChildren().add(circle);
                    workSpace.getChildren().remove(center);
                    center = null;
                    input2.setVisible(true);
                    prompt2.setVisible(true);
                    setPrompts("Укажите координаты центральной точки", "X", "Y");
                } else {
                    center = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center);
                    input2.setVisible(false);
                    prompt2.setVisible(false);
                    setPrompts("Укажите радиус", "R", null);
                }
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (center != null) {
                    double radius = Double.parseDouble(input1.getText()) * drawingContext.getScale();
                    Circle circle = new Circle(center, radius);
                    workSpace.getChildren().add(circle);
                    workSpace.getChildren().remove(center);
                    center = null;
                    input2.setVisible(true);
                    prompt2.setVisible(true);
                    setPrompts("Укажите координаты центральной точки", "X", "Y");
                } else {
                    double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
                    double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
                    center = new Point(x, y);
                    workSpace.getChildren().add(center);
                    input2.setVisible(false);
                    prompt2.setVisible(false);
                    setPrompts("Укажите радиус", "R", null);
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
