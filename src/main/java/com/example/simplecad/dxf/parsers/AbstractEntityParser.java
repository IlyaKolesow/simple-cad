package com.example.simplecad.dxf.parsers;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.modes.LineType;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.paint.Color;

import java.util.Map;

public abstract class AbstractEntityParser implements EntityParser {
    
    protected Point transformPoint(double x, double y, DrawingContext context) {
        Point coordsCenter = context.getCoordsCenter();
        double scale = context.getScale();
        
        double sceneX = coordsCenter.getX() + x * scale;
        double sceneY = coordsCenter.getY() - y * scale;
        
        return new Point(sceneX, sceneY);
    }
    
    protected double transformDistance(double distance, double scale) {
        return distance * scale;
    }
    
    protected void applyProperties(Figure figure, String dxfLineType, double thickness, double scale) {
        LineType lineType = getLineType(dxfLineType);
        figure.setLineType(lineType, scale);
        figure.setThickness(thickness);
        figure.setColor(Color.WHITE);
    }
    
    protected LineType getLineType(String lineType) {
        return switch (lineType) {
            case "DASHED" -> LineType.DASHED;
            case "DASHDOT" -> LineType.DASH_DOT;
            case "DIVIDE" -> LineType.DASH_DOT_DOT;
            default -> LineType.SOLID;
        };
    }
    
    protected double getDoubleValue(Map<Integer, String> entityData, int code, String defaultValue) {
        return Double.parseDouble(entityData.getOrDefault(code, defaultValue));
    }
    
    protected String getStringValue(Map<Integer, String> entityData, int code, String defaultValue) {
        return entityData.getOrDefault(code, defaultValue);
    }
} 