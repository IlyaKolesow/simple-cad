package com.example.simplecad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineType {
    public static final LineType SOLID = new LineType("Сплошная", 0, 0);
    public static final LineType DASHED = new LineType("Штриховая", 8.0, 2.0);
    public static final LineType DASH_DOT = new LineType("Штрих-пунктирная", 8.0, 5.0);
    public static final LineType DASH_DOT_DOT = new LineType("Штрих-пунктир с 2 точками", 8.0, 6.0);

    private final String name;
    private final List<Double> pattern;
    private double dashLength;
    private double spaceLength;

    public LineType(String name, double dashLength, double spaceLength) {
        this.name = name;
        this.pattern = new ArrayList<>();
        setDashSpace(new double[]{dashLength, spaceLength});
    }

    public List<Double> getPattern(double scale) {
        return pattern.stream()
                .map(value -> value * scale)
                .collect(Collectors.toList());
    }

    public double[] getDashSpace() {
        return new double[]{dashLength, spaceLength};
    }

    public void setDashSpace(double[] dashSpace) {
        dashLength = dashSpace[0];
        spaceLength = dashSpace[1];
        pattern.clear();
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
        return new LineType(
                origin.name,
                origin.getDashSpace()[0],
                origin.getDashSpace()[1]
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
