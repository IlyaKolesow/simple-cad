package com.example.simplecad.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Spline extends Figure {
    protected final List<Point> points = new ArrayList<>();
    protected final List<Line> lines = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
        update();
    }

    public abstract void update();

    public List<Point> getPoints() {
        return points;
    }

    public abstract Optional<Point> getSelectedPoint(double x, double y);
}
