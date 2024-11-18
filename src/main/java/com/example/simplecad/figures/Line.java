package com.example.simplecad.figures;

import javafx.scene.paint.Color;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class Line extends Figure {
    private Point point1;
    private Point point2;
    private javafx.scene.shape.Line line;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        build(point1, point2);
    }

    public Line(double x1, double y1, double x2, double y2) {
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
        build(point1, point2);
    }

    public Line(Point point1, double angle, double length) {
        double x2 = point1.getX() + Math.cos(-angle * Math.PI / 180) * length;
        double y2 = point1.getY() + Math.sin(-angle * Math.PI / 180) * length;
        Point point2 = new Point(x2, y2);

        this.point1 = point1;
        this.point2 = point2;

        build(point1, point2);
    }

    private void build(Point point1, Point point2) {
        line = new javafx.scene.shape.Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        line.setStrokeWidth(thickness);
        line.setStroke(color);
        getChildren().addAll(line, point1, point2);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        point1.move(deltaX, deltaY);
        point2.move(deltaX, deltaY);
        setPoints(point1, point2);
    }

    @Override
    public void scale(double coef, Point center) {
        point1.scale(coef, center);
        point2.scale(coef, center);
        setPoints(point1, point2);
    }

    @Override
    public boolean isHover(double x, double y) {
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double eps = 3;
        double distance = 10;

        if (x >= Math.min(x1, x2) - eps && x <= Math.max(x1, x2) + eps && y >= Math.min(y1, y2) - eps && y <= Math.max(y1, y2) + eps) {
            double a = getPointsDistance(point1, point2);
            double b = getPointsDistance(point1, new Point(x, y));
            double scalar = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
            double cos = scalar / (a * b);
            distance = b * Math.sqrt(1 - cos * cos);
        }

        return distance < eps;
    }

    public void setPoints(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        line.setStartX(point1.getX());
        line.setStartY(point1.getY());
        line.setEndX(point2.getX());
        line.setEndY(point2.getY());
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        line.setStroke(color);
        point1.setColor(color);
        point2.setColor(color);
    }

    @Override
    public void setThickness(double thickness) {
        super.setThickness(thickness);
        line.setStrokeWidth(thickness);
        point1.setThickness(thickness + 1);
        point2.setThickness(thickness + 1);
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }
}
