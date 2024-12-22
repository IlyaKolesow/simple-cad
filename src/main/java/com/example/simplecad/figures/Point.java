package com.example.simplecad.figures;

import javafx.scene.paint.Color;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

import java.util.*;

public class Point extends InputModifiableFigure {
    private double x;
    private double y;
    private javafx.scene.shape.Circle circle;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        circle = new javafx.scene.shape.Circle(x, y, thickness + 1);
        circle.setFill(color);
        getChildren().add(circle);
    }

    public Point(Point point) {
        x = point.getX();
        y = point.getY();
        color = point.getColor();
        thickness = point.getThickness();
        circle = new javafx.scene.shape.Circle(x, y, thickness + 1);
        circle.setFill(color);
        getChildren().add(circle);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        setX(x + deltaX);
        setY(y + deltaY);
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        double actualX = x - cursorPosition.getX();
        double actualY = cursorPosition.getY() - y;

        double deltaX = actualX * coef - actualX;
        double deltaY = actualY * coef - actualY;

        move(deltaX, -deltaY);
    }

    @Override
    public boolean isHover(double x, double y) {
        return Math.abs(this.x - x) < 3 && Math.abs(this.y - y) < 3;
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        circle.setFill(color);
    }

    @Override
    public void setThickness(double thickness) {
        super.setThickness(thickness);
        circle.setRadius(thickness);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        circle.setCenterX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        circle.setCenterY(y);
    }

    public void setCoords(double x, double y) {
        this.x = x;
        this.y = y;
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    @Override
    public void setValuesFromInputs(List<Double> values, Point center) {
        setX(values.get(0) + center.getX());
        setY(center.getY() - values.get(1));
    }

    @Override
    public Map<String, Double> getValuesForOutput(Point center) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("X", x - center.getX());
        map.put("Y", center.getY() - y);
        return map;
    }

    @Override
    public String getName() {
        return "ТОЧКА";
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        double distance = getPointsDistance(this, centralPoint);
        if (distance == 0)
            return;
        double extraAngle = Math.toDegrees(Math.asin((centralPoint.getY() - y) / distance));
        if (x < centralPoint.getX())
            extraAngle = 180 - extraAngle;

        double x = distance * Math.cos(Math.toRadians(angle + extraAngle));
        double y = distance * Math.sin(Math.toRadians(angle + extraAngle));
        setX(centralPoint.getX() + x);
        setY(centralPoint.getY() - y);
    }
}
