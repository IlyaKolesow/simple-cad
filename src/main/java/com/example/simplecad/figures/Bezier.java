package com.example.simplecad.figures;

import com.example.simplecad.modes.LineType;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bezier extends Spline {
    private final List<Point> controlPoints = new ArrayList<>();
    private final List<CubicCurve> splines = new ArrayList<>();

    @Override
    public void update() {
        splines.clear();
        lines.clear();
        getChildren().clear();

        if (points.size() == 1)
            controlPoints.add(new Point(points.getLast().getX() - 50, points.getLast().getY()));
        else if (points.size() * 2 > controlPoints.size()) {
            controlPoints.add(new Point(points.getLast().getX() + 50, points.getLast().getY()));
            controlPoints.add(new Point(points.getLast().getX() - 50, points.getLast().getY()));
        }

        for (int i = 1; i < points.size(); i++) {
            Point p0 = points.get(i - 1);
            Point p1 = controlPoints.get(2 * i - 2);
            Point p2 = controlPoints.get(2 * i - 1);
            Point p3 = points.get(i);
            Line line1 = new Line(p0, p1);
            Line line2 = new Line(p3, p2);
            line1.setColor(Color.AQUA);
            line2.setColor(Color.AQUA);
            lines.add(line1);
            lines.add(line2);

            CubicCurve spline = new CubicCurve();
            spline.setStartX(p0.getX());
            spline.setStartY(p0.getY());
            spline.setControlX1(p1.getX());
            spline.setControlY1(p1.getY());
            spline.setControlX2(p2.getX());
            spline.setControlY2(p2.getY());
            spline.setEndX(p3.getX());
            spline.setEndY(p3.getY());
            splines.add(spline);

            setColor(color);
            setThickness(thickness);
            setLinePattern(linePattern);
            getChildren().addAll(spline, line1, line2);
        }
    }

    @Override
    public Optional<Point> getSelectedPoint(double x, double y) {
        return controlPoints.stream().filter(p -> p.isHover(x, y)).findFirst();
    }

    @Override
    public void move(double deltaX, double deltaY) {
        points.forEach(p -> p.move(deltaX, deltaY));
        controlPoints.forEach(p -> p.move(deltaX, deltaY));
        update();
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        points.forEach(p -> p.scale(coef, cursorPosition));
        controlPoints.forEach(p -> p.scale(coef, cursorPosition));
        update();
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        points.forEach(p -> p.rotate(centralPoint, angle));
        controlPoints.forEach(p -> p.rotate(centralPoint, angle));
        update();
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        splines.forEach(e -> {
            e.setStroke(color);
            e.setFill(null);
        });
    }

    @Override
    public void setThickness(double thickness) {
        super.setThickness(thickness);
        splines.forEach(e -> e.setStrokeWidth(thickness));
    }

    @Override
    public void setLineType(LineType lineType, double scale) {
        super.setLineType(lineType, scale);
        setLinePattern(linePattern);
    }

    @Override
    protected void setLinePattern(List<Double> pattern) {
        super.setLinePattern(pattern);
        splines.forEach(e -> {
            e.getStrokeDashArray().clear();
            e.getStrokeDashArray().addAll(pattern);
        });
    }

    @Override
    public boolean isHover(double x, double y) {
        for (int i = 0; i <= points.size() - 2; i++)
            if (new Line(points.get(i), points.get(i + 1)).isHover(x, y))
                return true;
        return controlPoints.stream().anyMatch(p -> p.isHover(x, y));
    }

    @Override
    public String getName() {
        return "БЕЗЬЕ";
    }
}
