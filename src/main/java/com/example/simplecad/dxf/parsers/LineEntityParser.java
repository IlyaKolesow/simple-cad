package com.example.simplecad.dxf.parsers;

import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;

import java.util.Map;

public class LineEntityParser extends AbstractEntityParser {
    @Override
    public boolean canParse(String entityType) {
        return "LINE".equals(entityType);
    }

    @Override
    public void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace) {
        double x1 = getDoubleValue(entityData, 10, "0");
        double y1 = getDoubleValue(entityData, 20, "0");
        double x2 = getDoubleValue(entityData, 11, "0");
        double y2 = getDoubleValue(entityData, 21, "0");
        String lineType = getStringValue(entityData, 6, "CONTINUOUS");
        double thickness = getDoubleValue(entityData, 370, "100") / 100.0;

        double scale = context.getScale();
        Point point1 = transformPoint(x1, y1, context);
        Point point2 = transformPoint(x2, y2, context);

        Line line = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        applyProperties(line, lineType, thickness, scale);
        workspace.getChildren().add(line);
    }
}