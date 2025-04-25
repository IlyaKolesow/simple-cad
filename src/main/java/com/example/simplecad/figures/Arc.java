package com.example.simplecad.figures;

import com.example.simplecad.modes.LineType;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.simplecad.util.MathCalculation.*;

public class Arc extends InputModifiableFigure {
    private Point center;
    private double radius;
    private Point startPoint;
    private Point endPoint;
    private javafx.scene.shape.Arc arc;

    public Arc(Point point1, Point point2, Point point3) {
        startPoint = point1;
        endPoint = point3;
        double[] values = getCenterAndRadius(point1, point2, point3);
        center = new Point(values[0], values[1]);
        radius = values[2];
        build(point1, point2, point3);
    }

    public Arc(Point center, Line chord) {
        this.center = center;
        startPoint = chord.getPoint1();
        radius = getPointsDistance(center, startPoint);
        build(chord);
    }

    private void build(Point point1, Point point2, Point point3) {
        double cx = center.getX();
        double cy = center.getY();

        double angle1 = Math.toDegrees(Math.acos((point1.getX() - cx) / radius));
        double angle2 = Math.toDegrees(Math.acos((point2.getX() - cx) / radius));
        double angle3 = Math.toDegrees(Math.acos((point3.getX() - cx) / radius));

        if (cy < point1.getY())
            angle1 = 360 - angle1;
        if (cy < point2.getY())
            angle2 = 360 - angle2;
        if (cy < point3.getY())
            angle3 = 360 - angle3;

        double arcAngle = (angle3 - angle1 + 360) % 360;

        boolean isClockwiseNormalCase = angle1 < angle3 && !(angle1 < angle2 && angle2 < angle3);
        boolean isClockwiseCrossZero = angle1 > angle3 && !(angle1 < angle2 || angle2 < angle3);

        if (isClockwiseNormalCase || isClockwiseCrossZero)
            arcAngle -= 360;

        arc = new javafx.scene.shape.Arc(cx, cy, radius, radius, angle1, arcAngle);
        arc.setStrokeWidth(thickness);
        setColor(color);
        getChildren().addAll(arc, point1, point3);
    }

    private void build(Line chord) {
        double cx = center.getX();
        double cy = center.getY();
        List<Point> intersections = findLineCircleIntersections(center, radius, chord.getPoint1(), chord.getPoint2());
        if (intersections.size() == 2) {
            if (Math.abs(startPoint.getX() - intersections.get(0).getX()) < 0.0001 && Math.abs(startPoint.getY() - intersections.get(0).getY()) < 0.0001)
                endPoint = intersections.get(1);
            else
                endPoint = intersections.get(0);
        }
        if (intersections.size() == 1)
            endPoint = intersections.get(0);

        double startAngle = Math.toDegrees(Math.acos((startPoint.getX() - cx) / radius));
        if (cy < startPoint.getY())
            startAngle = 360 - startAngle;

        double scalar = (startPoint.getX() - cx) * (endPoint.getX() - cx) + (startPoint.getY() - cy) * (endPoint.getY() - cy);
        double arcAngle = Math.toDegrees(Math.acos(scalar / (radius * radius)));

        double cross = (startPoint.getX() - cx) * (cy - endPoint.getY()) - (cy - startPoint.getY()) * (endPoint.getX() - cx);
        if (cross < 0)
            arcAngle *= -1;

        arc = new javafx.scene.shape.Arc(cx, cy, radius, radius, startAngle, arcAngle);
        arc.setStrokeWidth(thickness);
        setColor(color);
        getChildren().addAll(arc, startPoint, endPoint);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        startPoint.move(deltaX, deltaY);
        endPoint.move(deltaX, deltaY);
        center.move(deltaX, deltaY);
        setCenter(center);
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        startPoint.scale(coef, cursorPosition);
        endPoint.scale(coef, cursorPosition);
        center.scale(coef, cursorPosition);
        radius *= coef;
        setCenter(center);
        setRadius(radius);
    }

    @Override
    public boolean isHover(double x, double y) {
        double eps = 5;
        double distance = getPointsDistance(center, new Point(x, y));
        return Math.abs(distance - radius) < eps;
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        arc.setStroke(color);
        startPoint.setColor(color);
        endPoint.setColor(color);
        arc.setFill(null);
    }

    @Override
    public void setLineType(LineType lineType, double scale) {
        super.setLineType(lineType, scale);
        arc.getStrokeDashArray().clear();
        arc.getStrokeDashArray().addAll(lineType.getPattern(scale));
    }

    private void setCenter(Point center) {
        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
    }

    private void setRadius(double radius) {
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public double getStartAngle() {
        return arc.getStartAngle();
    }

    public double getLength() {
        return arc.getLength();
    }

    @Override
    public void setCoords(List<Double> values, Point coordsCenter) {
        double deltaX = values.get(0) + coordsCenter.getX() - this.center.getX();
        double deltaY = coordsCenter.getY() - values.get(1) - this.center.getY();
        double radius = values.get(2);
        scale(radius / this.radius, this.center);
        move(deltaX, deltaY);
        this.radius = radius;
    }

    @Override
    public Map<String, Double> getCoords(Point center) {
        return getCenterRadiusCoords(center, this.center, radius);
    }

    @Override
    public String getName() {
        return "ДУГА";
    }

    @Override
    public String getDXFName() {
        return "ARC";
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        startPoint.rotate(centralPoint, angle);
        endPoint.rotate(centralPoint, angle);
        center.rotate(centralPoint, angle);
        setCenter(center);
        arc.setStartAngle(arc.getStartAngle() + angle);
    }
}
