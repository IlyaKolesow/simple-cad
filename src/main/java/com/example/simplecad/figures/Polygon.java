package com.example.simplecad.figures;

import com.example.simplecad.modes.LineType;
import com.example.simplecad.modes.DrawingMode;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class Polygon extends InputModifiableFigure {
    private int n;
    private Line[] lines;
    private Point[] points;
    private Point center;
    private double R;
    private double r;

    public Polygon(Point center, double radius, int n, DrawingMode mode) {
        if (mode == DrawingMode.INSCRIBED_IN_CIRCLE)
            this.R = radius;
        else if (mode == DrawingMode.CIRCUMSCRIBED_AROUND_CIRCLE)
            this.r = radius;

        init(center, n);
        build(mode, null);
    }

    public Polygon(Point center, Point vertex, int n, DrawingMode mode) {
        init(center, n);
        build(mode, vertex);
    }

    private void init(Point center, int n) {
        this.center = center;
        this.n = n;
        lines = new Line[n];
        points = new Point[n];
    }

    private void build(DrawingMode mode, Point vertex) {
        double extraAngle = 0;

        switch (mode) {
            case INSCRIBED_IN_CIRCLE:
                if (R == 0) {
                    R = getPointsDistance(center, vertex);
                    points[0] = vertex;
                    extraAngle = Math.toDegrees(Math.acos((center.getY() - vertex.getY()) / R));
                } else if (vertex == null)
                    points[0] = new Point(center.getX(), center.getY() - R);
                r = R * Math.cos(Math.PI / n);
                break;

            case CIRCUMSCRIBED_AROUND_CIRCLE:
                if (r == 0) {
                    r = getPointsDistance(center, vertex);
                    R = r / Math.cos(Math.PI / n);
                    extraAngle = Math.toDegrees(Math.acos((center.getY() - vertex.getY()) / r));
                } else if (vertex == null) {
                    R = r / Math.cos(Math.PI / n);
                    points[0] = new Point(center.getX(), center.getY() - R);
                }
        }

        double sideLength = 2 * R * Math.sin(Math.PI / n);
        double sidesAngle = 180.0 * (n - 2) / n;

        if (vertex != null && vertex.getX() < center.getX())
            extraAngle = 360 - extraAngle;

        if (mode == DrawingMode.CIRCUMSCRIBED_AROUND_CIRCLE && vertex != null) {
            double x = vertex.getX() + sideLength / 2 * Math.cos(Math.toRadians(extraAngle));
            double y = vertex.getY() + sideLength / 2 * Math.sin(Math.toRadians(extraAngle));
            points[0] = new Point(x, y);

            extraAngle += 180.0 / n;
        }

        for (int i = 0; i < n - 1; i++) {
            lines[i] = new Line(points[i], (sidesAngle / 2 - 90) - i * (180 - sidesAngle) - extraAngle, sideLength);
            points[i + 1] = lines[i].getPoint2();
        }
        lines[n - 1] = new Line(points[n - 1], points[0]);

        getChildren().addAll(lines);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        for (Point point : points)
            point.move(deltaX, deltaY);
        center.move(deltaX, deltaY);
        updateLines();
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        for (Point point : points)
            point.scale(coef, cursorPosition);
        center.scale(coef, cursorPosition);
        updateLines();
        R *= coef;
        r = R * Math.cos(Math.PI / n);
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

    @Override
    public void setLineType(LineType lineType, double scale) {
        super.setLineType(lineType, scale);
        for (Line line : lines)
            line.setLineType(lineType, scale);
    }

    private void setCenter(double x, double y) {
        double deltaX = x - this.center.getX();
        double deltaY = y - this.center.getY();
        move(deltaX, deltaY);
    }

    private void setRadius(double R) {
        double scaleCoef = R / this.R;
        scale(scaleCoef, this.center);
        this.R = R;
        r = R * Math.cos(Math.PI / n);
    }

    @Override
    public void setValuesFromInputs(List<Double> values, Point coordsCenter) {
        setCenter(values.get(0) + coordsCenter.getX(), coordsCenter.getY() - values.get(1));
        double R = values.get(2);
        setRadius(R);
    }

    @Override
    public Map<String, Double> getValuesForOutput(Point center) {
        return centerRadius(center, this.center, R);
    }

    @Override
    public String getName() {
        return "МНОГОУГОЛЬНИК";
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        for (Point point : points)
            point.rotate(centralPoint, angle);
        updateLines();
    }
}
