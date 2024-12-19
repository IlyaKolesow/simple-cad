package com.example.simplecad.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Spline extends Figure {
    protected final List<Point> points = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
        update();
    }

    public abstract void update();

    public List<Point> getPoints() {
        return points;
    }

    public Optional<Point> getSelectedPoint(double x, double y) {
        return points.stream().filter(p -> p.isHover(x, y)).findFirst();
    }

    @Override
    public void move(double deltaX, double deltaY) {

    }

    @Override
    public void scale(double coef, Point cursorPosition) {

    }

    @Override
    public void rotate(Point centralPoint, double angle) {

    }
}
