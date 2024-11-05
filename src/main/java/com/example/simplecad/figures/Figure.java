package com.example.simplecad.figures;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Figure extends Group {
    protected Color color;
    protected double thickness;

    public Figure() {
        color = Color.WHITE;
        thickness = 2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public Color getColor() {
        return color;
    }

    public double getThickness() {
        return thickness;
    }

    public abstract void move(double deltaX, double deltaY);

    public abstract void scale(double coef, Point center);
}
