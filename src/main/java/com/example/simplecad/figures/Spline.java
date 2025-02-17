package com.example.simplecad.figures;

import com.example.simplecad.modes.LineType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Spline extends Figure {
    protected final List<Point> points = new ArrayList<>();
    protected final List<Line> lines = new ArrayList<>();
    protected List<Double> linePattern = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
        update();
    }

    @Override
    public void setLineType(LineType lineType, double scale) {
        super.setLineType(lineType, scale);
        linePattern = lineType.getPattern(scale);
    }

    protected void setLinePattern(List<Double> pattern) {
        linePattern = pattern;
    }

    public List<Point> getPoints() {
        return points;
    }

    public abstract void update();
    public abstract Optional<Point> getSelectedPoint(double x, double y);
}
