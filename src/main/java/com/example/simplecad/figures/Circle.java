package com.example.simplecad.figures;

import javafx.scene.paint.Color;

public class Circle extends Figure {
    private Point center;
    private double radius;
    private javafx.scene.shape.Circle circle;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
        build();
    }

    public Circle(Point point1, Point point2, Point point3) {

        build();
    }

    private void build() {
        circle = new javafx.scene.shape.Circle();
        circle.setCenterX(center.getX());
        circle.setCenterY(center.getY());
        circle.setRadius(radius);
        circle.setStrokeWidth(thickness);
        circle.setStroke(color);
        circle.setFill(null);

        getChildren().addAll(circle, center);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        center.move(deltaX, deltaY);
        setCenter(center);
    }

    @Override
    public void scale(double coef, Point _center) {
        center.scale(coef, _center);
        setRadius(radius * coef);
        setCenter(center);
    }

    public void setCenter(Point center) {
        this.center = center;
        circle.setCenterX(center.getX());
        circle.setCenterY(center.getY());
    }

    public void setRadius(double radius) {
        this.radius = radius;
        circle.setRadius(radius);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        circle.setStroke(color);
    }

    @Override
    public void setThickness(double thickness) {
        super.setThickness(thickness);
        circle.setStrokeWidth(thickness);
    }
}
