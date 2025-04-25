package com.example.simplecad.dxf.parsers;

import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;

import java.util.Map;

public class CircleEntityParser extends AbstractEntityParser {
    @Override
    public boolean canParse(String entityType) {
        return "CIRCLE".equals(entityType);
    }

    @Override
    public void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace) {
        double centerX = getDoubleValue(entityData, 10, "0");
        double centerY = getDoubleValue(entityData, 20, "0");
        double radius = getDoubleValue(entityData, 40, "0");
        String lineType = getStringValue(entityData, 6, "CONTINUOUS");
        double thickness = getDoubleValue(entityData, 370, "100") / 100.0;

        double scale = context.getScale();
        Point center = transformPoint(centerX, centerY, context);
        double sceneRadius = transformDistance(radius, scale);

        Circle circle = new Circle(center, sceneRadius);
        applyProperties(circle, lineType, thickness, scale);
        workspace.getChildren().add(circle);
    }
} 