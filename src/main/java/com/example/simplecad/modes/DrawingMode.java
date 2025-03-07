package com.example.simplecad.modes;

public enum DrawingMode {
    BY_2_POINTS("2 точки"),
    BY_3_POINTS("3 точки"),
    BY_SIDES("Стороны"),
    BY_RADIUS("Радиус"),
    BY_ANGLE_LENGTH("Угол и длина"),
    INSCRIBED_IN_CIRCLE("Вписанный"),
    CIRCUMSCRIBED_AROUND_CIRCLE("Описанный"),
    CHORD("Хорда"),
    QUAD_SPLINE("Квадратичный"),
    BEZIER("Безье");

    DrawingMode(String name) {
        this.name = name;
    }
    
    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
