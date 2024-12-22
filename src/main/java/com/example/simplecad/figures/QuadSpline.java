package com.example.simplecad.figures;

import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.simplecad.util.MathCalculation.getMiddlePoint;

public class QuadSpline extends Spline {
    private final List<QuadCurve> splines = new ArrayList<>();

    @Override
    public void update() {
        splines.clear();
        lines.clear();
        getChildren().clear();

        if (points.size() > 1) {
            Line line = new Line(points.getFirst(), points.get(1));
            line.setColor(Color.AQUA);
            lines.add(line);
            getChildren().add(line);
        }

        for (int i = 1; i < points.size() - 1; i++) {
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);

            Point start = getMiddlePoint(p0, p1);
            Point end = getMiddlePoint(p1, p2);

            if (i == 1)
                start = new Point(p0.getX(), p0.getY());
            if (i == points.size() - 2)
                end = new Point(p2.getX(), p2.getY());
            if (i > 1) {
                splines.getLast().setEndX(start.getX());
                splines.getLast().setEndY(start.getY());
            }

            Line line = new Line(p1, p2);
            line.setColor(Color.AQUA);
            lines.add(line);
            getChildren().add(line);

            QuadCurve spline = new QuadCurve();
            spline.setStartX(start.getX());
            spline.setStartY(start.getY());
            spline.setControlX(p1.getX());
            spline.setControlY(p1.getY());
            spline.setEndX(end.getX());
            spline.setEndY(end.getY());
            splines.add(spline);

            setColor(color);
            setThickness(thickness);
            getChildren().add(spline);
        }
    }

    @Override
    public Optional<Point> getSelectedPoint(double x, double y) {
        return points.stream().filter(p -> p.isHover(x, y)).findFirst();
    }

    @Override
    public void move(double deltaX, double deltaY) {
        points.forEach(p -> p.move(deltaX, deltaY));
        update();
    }

    @Override
    public void scale(double coef, Point cursorPosition) {
        points.forEach(p -> p.scale(coef, cursorPosition));
        update();
    }

    @Override
    public void rotate(Point centralPoint, double angle) {
        points.forEach(p -> p.rotate(centralPoint, angle));
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
    public boolean isHover(double x, double y) {
        Optional<Line> optional = lines.stream().filter(line -> line.isHover(x, y)).findFirst();
        return optional.isPresent();
    }

    @Override
    public String getName() {
        return "КВАДРАТИЧНЫЙ СПЛАЙН";
    }
}
