package com.example.simplecad.figures;

import java.util.ArrayList;
import java.util.List;

public abstract class Spline extends Figure {
    protected final List<Point> points = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
        update();
    }

    protected abstract void update();

    public List<Point> getPoints() {
        return points;
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
