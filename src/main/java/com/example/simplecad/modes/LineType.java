package com.example.simplecad.modes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineType {
    public static final LineType SOLID = new LineType("Сплошная");
    public static final LineType DASHED = new LineType("Штриховая", 8.0, 2.0);
    public static final LineType DASH_DOT = new LineType("Штрих-пунктирная", 8.0, 5.0);
    public static final LineType DASH_DOT_DOT = new LineType("Штрих-пунктир с 2 точками", 8.0, 6.0);

    private final String name;
    private final List<Double> pattern;
    private double dashLength;
    private double spaceLength;

    public LineType(String name, double dashLength, double spaceLength) {
        this.name = name;
        pattern = new ArrayList<>();
        setDashSpace(List.of(dashLength, spaceLength));
    }

    public LineType(String name) {
        this.name = name;
        pattern = new ArrayList<>();
    }

    public List<Double> getPattern(double scale) {
        return pattern.stream()
                .map(value -> value * scale)
                .collect(Collectors.toList());
    }

    public List<Double> getDashSpace() {
        if (name.equals("Сплошная"))
            return List.of();
        return List.of(dashLength, spaceLength);
    }

    public void setDashSpace(List<Double> dashSpace) {
        pattern.clear();

        if (dashSpace.isEmpty())
            return;

        dashLength = dashSpace.get(0);
        spaceLength = dashSpace.get(1);
        switch (name) {
            case "Штриховая":
                pattern.addAll(List.of(dashLength, spaceLength));
                break;
            case "Штрих-пунктирная":
                pattern.addAll(List.of(dashLength, spaceLength / 2, 0.5, spaceLength / 2));
                break;
            case "Штрих-пунктир с 2 точками":
                pattern.addAll(List.of(dashLength, spaceLength / 3, 0.5, spaceLength / 3, 0.5, spaceLength / 3));
                break;
        }
    }

    public static LineType copy(LineType origin) {
        if (origin.getDashSpace().isEmpty())
            return new LineType(origin.name);

        return new LineType(
                origin.name,
                origin.getDashSpace().get(0),
                origin.getDashSpace().get(1)
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
