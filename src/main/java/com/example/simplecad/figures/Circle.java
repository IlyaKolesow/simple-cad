package com.example.simplecad.figures;

import com.example.simplecad.LineType;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;
import static com.example.simplecad.util.MathCalculation.getCenterAndRadius;

public class Circle extends Figure {
    private Point center;
    private double radius;
    private javafx.scene.shape.Circle circle;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
        build();
    }

    public Circle(Point center, Point vertex) {
        this.center = center;
        this.radius = getPointsDistance(center, vertex);
        build();
    }

    public Circle(Point point1, Point point2, Point point3) {
        double[] values = getCenterAndRadius(point1, point2, point3);
        center = new Point(values[0], values[1]);
        radius = values[2];
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

        getChildren().add(circle);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        center.move(deltaX, deltaY);
        setCenter(center);
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        center.scale(coef, cursorPosition);
        setRadius(radius * coef);
        setCenter(center);
    }

    @Override
    public boolean isHover(double x, double y) {
        double eps = 5;
        double distance = getPointsDistance(center, new Point(x, y));
        return Math.abs(distance - radius) < eps;
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

    @Override
    public void setLineType(LineType lineType, double scale) {
        super.setLineType(lineType, scale);
        circle.getStrokeDashArray().clear();
        if (lineType != LineType.SOLID)
            circle.getStrokeDashArray().addAll(lineType.getPattern(scale));
    }

    @Override
    public void setValuesFromInputs(List<Double> values, Point center) {
        double x = values.get(0) + center.getX();
        double y = center.getY() - values.get(1);
        setCenter(new Point(x, y));
        setRadius(values.get(2));
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
        return "ОКРУЖНОСТЬ";
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        center.rotate(centralPoint, angle);
        setCenter(center);
    }
}
