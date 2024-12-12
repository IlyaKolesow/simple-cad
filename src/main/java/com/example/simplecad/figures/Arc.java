package com.example.simplecad.figures;

import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.simplecad.util.MathCalculation.getCenterAndRadius;
import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class Arc extends Figure {
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

    private void build(Point point1, Point point2, Point point3) {
        double angle1 = Math.toDegrees(Math.asin((center.getY() - point1.getY()) / radius));
        double angle2 = Math.toDegrees(Math.asin((center.getY() - point2.getY()) / radius));
        double angle3 = Math.toDegrees(Math.asin((center.getY() - point3.getY()) / radius));

        if (center.getX() > point1.getX())
            angle1 = 180 - angle1;
        if (center.getX() > point2.getX())
            angle2 = 180 - angle2;
        if (center.getX() > point3.getX())
            angle3 = 180 - angle3;

        double arcAngle = (angle3 - angle1 + 360) % 360;

        if (angle2 < angle1)
            arcAngle -= 360;

        arc = new javafx.scene.shape.Arc(center.getX(), center.getY(), radius, radius, angle1, arcAngle);
        arc.setStrokeWidth(thickness);
        arc.setStroke(color);
        arc.setFill(null);
        getChildren().addAll(arc, point1, point3);
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
    }

    private void setCenter(Point center) {
        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
    }

    private void setRadius(double radius) {
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
    }

    @Override
    public void setValuesFromInputs(List<Double> values, Point coordsCenter) {
        double deltaX = values.get(0) + coordsCenter.getX() - this.center.getX();
        double deltaY = coordsCenter.getY() - values.get(1) - this.center.getY();
        double radius = values.get(2);
        scale(radius / this.radius, this.center);
        move(deltaX, deltaY);
        this.radius = radius;
    }

    @Override
    public Map<String, Double> getValuesForOutput(Point center) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("Центр [X]", this.center.getX() - center.getX());
        map.put("Центр [Y]", center.getY() - this.center.getY());
        map.put("Радиус", radius);
        return map;
    }

    @Override
    public String getName() {
        return "ДУГА";
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
