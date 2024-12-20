package com.example.simplecad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum LineType {
    SOLID("Сплошная"),
    DASHED("Штриховая", 8.0, 2.0),
    DASH_DOT("Штрих-пунктирная", 8.0, 2.5, 0.5, 2.5),
    DASH_DOT_DOT("Штрих-пунктир с 2 точками", 8.0, 2.0, 0.5, 2.0, 0.5, 2.0);

    LineType(String name, Double ...values) {
        this.name = name;
        pattern = List.of(values);
    }

    private final String name;
    private final List<Double> pattern;

    public List<Double> getPattern(double scale) {
        return pattern.stream()
                .map(value -> value * scale)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}
