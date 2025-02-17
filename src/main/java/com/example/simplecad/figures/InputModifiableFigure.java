package com.example.simplecad.figures;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class InputModifiableFigure extends Figure {
    public abstract void setValuesFromInputs(List<Double> values, Point center);
    public abstract Map<String, Double> getValuesForOutput(Point center);

    public Map<String, Double> getCenterRadiusForOutput(Point coordsCenter, Point figureCenter, double radius) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("Центр [X]", figureCenter.getX() - coordsCenter.getX());
        map.put("Центр [Y]", coordsCenter.getY() - figureCenter.getY());
        map.put("Радиус", radius);
        return map;
    }
}
