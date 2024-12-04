package com.example.simplecad.figures;

import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public void scale(double coef, Point cursorPosition) {
        for (Point point : points)
            point.scale(coef, cursorPosition);
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

    @Override
    public void setValuesFromInputs(List<Double> values, Point center) {
        points[0].setX(values.get(0) + center.getX());
        points[0].setY(center.getY() - values.get(1));
        points[2].setX(values.get(2) + center.getX());
        points[2].setY(center.getY() - values.get(3));
        points[1].setX(points[0].getX());
        points[1].setY(points[2].getY());
        points[3].setX(points[2].getX());
        points[3].setY(points[0].getY());
        updateLines();
    }

    @Override
    public Map<String, Double> getValuesForOutput(Point center) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("Диагональ [X1]", points[0].getX() - center.getX());
        map.put("Диагональ [Y1]", center.getY() - points[0].getY());
        map.put("Диагональ [X2]", points[2].getX() - center.getX());
        map.put("Диагональ [Y2]", center.getY() - points[2].getY());
        return map;
    }

    @Override
    public String getName() {
        return "ПРЯМОУГОЛЬНИК";
    }
}
