package com.example.simplecad.figures;

import com.example.simplecad.Mode;
import javafx.scene.paint.Color;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class Polygon extends Figure {
    private int n;
    private Line[] lines;
    private Point[] points;
    private Point center;
    private double radius;

    public Polygon(Point center, double radius, int n, Mode mode) {
        this.radius = radius;
        init(center, n);
        build(mode);
    }

    public Polygon(Point center, Point vertex, int n, Mode mode) {
        init(center, n);
        points[0] = vertex;
        build(mode);
    }

    private void init(Point center, int n) {
        this.center = center;
        this.n = n;
        lines = new Line[n];
        points = new Point[n];
    }

    private void build(Mode mode) {
        if (mode == Mode.INSCRIBED_IN_CIRCLE) {
            if (radius == 0)
                radius = getPointsDistance(center, points[0]);
            else if (points[0] == null)
                points[0] = new Point(center.getX(), center.getY() - radius);

            double sideLength = 2 * radius * Math.sin(Math.PI / n);
            double sidesAngle = 180.0 * (n - 2) / n;
            double extraAngle = Math.toDegrees(Math.acos((center.getY() - points[0].getY()) / radius));

            if (points[0].getX() < center.getX())
                extraAngle = 360 - extraAngle;

            for (int i = 0; i < n - 1; i++) {
                lines[i] = new Line(points[i], (sidesAngle / 2 - 90) - i * (180 - sidesAngle) - extraAngle, sideLength);
                points[i + 1] = lines[i].getPoint2();
            }

            lines[n - 1] = new Line(points[n - 1], points[0]);
        }

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

    @Override
    public boolean isHover(double x, double y) {
        for (Line line : lines)
            if (line.isHover(x, y))
                return true;
        return false;
    }

    private void updateLines() {
        for (int i = 0; i < n - 1; i++)
            lines[i].setPoints(points[i], points[i + 1]);
        lines[n - 1].setPoints(points[n - 1], points[0]);
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
