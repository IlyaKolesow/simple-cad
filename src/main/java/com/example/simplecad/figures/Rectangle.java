package com.example.simplecad.figures;

import javafx.scene.paint.Color;

public class Rectangle extends Figure {
    private final Line[] lines = new Line[4];
    private final Point[] points = new Point[4];

    public Rectangle(Point point1, Point point3) {
        Point point2 = new Point(point1.getX(), point3.getY());
        Point point4 = new Point(point3.getX(), point1.getY());

        build(point1, point2, point3, point4);
    }

    public Rectangle(Point center, double width, double height) {
        Point point1 = new Point(center.getX() - width / 2, center.getY() - height / 2);
        Point point2 = new Point(center.getX() - width / 2, center.getY() + height / 2);
        Point point3 = new Point(center.getX() + width / 2, center.getY() + height / 2);
        Point point4 = new Point(center.getX() + width / 2, center.getY() - height / 2);

        build(point1, point2, point3, point4);
    }

    private void build(Point point1, Point point2, Point point3, Point point4) {
        lines[0] = new Line(point1, point2);
        lines[1] = new Line(point2, point3);
        lines[2] = new Line(point3, point4);
        lines[3] = new Line(point4, point1);

        points[0] = point1;
        points[1] = point2;
        points[2] = point3;
        points[3] = point4;

        getChildren().addAll(lines);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        for (Point point : points)
            point.move(deltaX, deltaY);
        updateLines();
    }

    @Override
    public void scale(double coef, Point center) {
        for (Point point : points)
            point.scale(coef, center);
        updateLines();
    }

    private void updateLines() {
        for (int i = 0; i < 3; i++)
            lines[i].setPoints(points[i], points[i + 1]);
        lines[3].setPoints(points[3], points[0]);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        for (Line line : lines)
            line.setColor(color);
    }

    @Override
    public void setThickness(double thickness) {
        super.setThickness(thickness);
        for (Line line : lines)
            line.setThickness(thickness);
    }
}
