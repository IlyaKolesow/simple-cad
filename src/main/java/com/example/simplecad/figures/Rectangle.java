package com.example.simplecad.figures;

import com.example.simplecad.LineType;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.simplecad.util.MathCalculation.getMiddlePoint;

public class Rectangle extends InputModifiableFigure {
    private final Line[] lines = new Line[4];
    private final Point[] points = new Point[4];
    private double width;
    private double height;
    private Point center;

    public Rectangle(Point point1, Point point3) {
        Point point2 = new Point(point1.getX(), point3.getY());
        Point point4 = new Point(point3.getX(), point1.getY());

        width = Math.abs(point1.getX() - point3.getX());
        height = Math.abs(point1.getY() - point3.getY());
        double x = point1.getX() + (point3.getX() - point1.getX()) / 2;
        double y = point3.getY() + (point1.getY() - point3.getY()) / 2;
        center = new Point(x, y);
        build(point1, point2, point3, point4);
    }

    public Rectangle(Point center, double width, double height) {
        Point point1 = new Point(center.getX() - width / 2, center.getY() - height / 2);
        Point point2 = new Point(center.getX() - width / 2, center.getY() + height / 2);
        Point point3 = new Point(center.getX() + width / 2, center.getY() + height / 2);
        Point point4 = new Point(center.getX() + width / 2, center.getY() - height / 2);

        this.width = width;
        this.height = height;
        this.center = center;
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
        center.move(deltaX, deltaY);
        updateLines();
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        for (Point point : points)
            point.scale(coef, cursorPosition);
        center.scale(coef, cursorPosition);
        width *= coef;
        height *= coef;
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

    private void setWidth(double width) {
        double scale = width / this.width;
        this.width = width;
        Point middlePoint1 = getMiddlePoint(points[0], points[3]);
        Point middlePoint2 = getMiddlePoint(points[1], points[2]);
        calculateCoords(scale, middlePoint1, middlePoint2, middlePoint2.getX(), middlePoint2.getY(), middlePoint1.getX(), middlePoint1.getY());
    }

    private void setHeight(double height) {
        double scale = height / this.height;
        this.height = height;
        Point middlePoint1 = getMiddlePoint(points[0], points[1]);
        Point middlePoint2 = getMiddlePoint(points[2], points[3]);
        calculateCoords(scale, middlePoint1, middlePoint2, middlePoint1.getX(), middlePoint1.getY(), middlePoint2.getX(), middlePoint2.getY());
    }

    private void calculateCoords(double scale, Point middlePoint1, Point middlePoint2, double x, double y, double x4, double y4) {
        double x0 = (points[0].getX() - middlePoint1.getX()) * scale + middlePoint1.getX();
        double y0 = (points[0].getY() - middlePoint1.getY()) * scale + middlePoint1.getY();
        double x1 = (points[1].getX() - x) * scale + x;
        double y1 = (points[1].getY() - y) * scale + y;
        double x2 = (points[2].getX() - middlePoint2.getX()) * scale + middlePoint2.getX();
        double y2 = (points[2].getY() - middlePoint2.getY()) * scale + middlePoint2.getY();
        double x3 = (points[3].getX() - x4) * scale + x4;
        double y3 = (points[3].getY() - y4) * scale + y4;

        points[0].setCoords(x0, y0);
        points[1].setCoords(x1, y1);
        points[2].setCoords(x2, y2);
        points[3].setCoords(x3, y3);
        updateLines();
    }

    @Override
    public void setValuesFromInputs(List<Double> values, Point coordsCenter) {
        setCenter(values.get(0) + coordsCenter.getX(), coordsCenter.getY() - values.get(1));
        setWidth(values.get(2));
        setHeight(values.get(3));
    }

    @Override
    public Map<String, Double> getValuesForOutput(Point center) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("Центр [X]", this.center.getX() - center.getX());
        map.put("Центр [Y]", center.getY() - this.center.getY());
        map.put("Ширина", width);
        map.put("Высота", height);
        return map;
    }

    @Override
    public String getName() {
        return "ПРЯМОУГОЛЬНИК";
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        for (Point point : points)
            point.rotate(centralPoint, angle);
        updateLines();
    }
}
