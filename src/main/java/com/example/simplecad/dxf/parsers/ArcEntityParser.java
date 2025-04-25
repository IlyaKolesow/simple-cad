package com.example.simplecad.dxf.parsers;

import com.example.simplecad.figures.Arc;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;

import java.util.Map;

public class ArcEntityParser extends AbstractEntityParser {
    @Override
    public boolean canParse(String entityType) {
        return "ARC".equals(entityType);
    }

    @Override
    public void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace) {
        double centerX = getDoubleValue(entityData, 10, "0");
        double centerY = getDoubleValue(entityData, 20, "0");
        double radius = getDoubleValue(entityData, 40, "0");
        double startAngle = getDoubleValue(entityData, 50, "0");
        double endAngle = getDoubleValue(entityData, 51, "0");
        String lineType = getStringValue(entityData, 6, "CONTINUOUS");
        double thickness = getDoubleValue(entityData, 370, "100") / 100.0;

        double scale = context.getScale();
        Point center = transformPoint(centerX, centerY, context);
        double sceneRadius = transformDistance(radius, scale);

        double startRadians = Math.toRadians(startAngle);
        double endRadians = Math.toRadians(endAngle);
        double midRadians = startRadians + (endRadians - startRadians) / 2;
        if (startRadians > endRadians)
            midRadians = startRadians + (endRadians + 2 * Math.PI - startRadians) / 2;

        Point point1 = new Point(
                center.getX() + sceneRadius * Math.cos(startRadians),
                center.getY() - sceneRadius * Math.sin(startRadians)
        );
        Point point2 = new Point(
                center.getX() + sceneRadius * Math.cos(midRadians),
                center.getY() - sceneRadius * Math.sin(midRadians)
        );
        Point point3 = new Point(
                center.getX() + sceneRadius * Math.cos(endRadians),
                center.getY() - sceneRadius * Math.sin(endRadians)
        );

        Arc arc = new Arc(point1, point2, point3);
        applyProperties(arc, lineType, thickness, scale);
        workspace.getChildren().add(arc);
    }
} 