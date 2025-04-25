package com.example.simplecad.figures;

import com.example.simplecad.modes.LineType;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Figure extends Group {
    protected Color color;
    protected double thickness;
    protected LineType lineType;

    public Figure() {
        color = Color.WHITE;
        thickness = 1;
        lineType = LineType.SOLID;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public void setLineType(LineType lineType, double scale) {
        this.lineType = lineType;
    }

    public Color getColor() {
        return color;
    }

    public double getThickness() {
        return thickness;
    }

    public LineType getLineType() {
        return lineType;
    }

    public abstract void move(double deltaX, double deltaY);
    public abstract void scale(double coef, Point cursorPosition);
    public abstract boolean isHover(double x, double y);
    public abstract String getName();
    public abstract String getDXFName();
    public abstract void rotate(Point centralPoint, double angle);
}
