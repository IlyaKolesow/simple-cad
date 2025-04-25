package com.example.simplecad.dxf.parsers;

import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Polygon;
import com.example.simplecad.figures.Rectangle;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;

public class PolylineEntityParser extends AbstractEntityParser {
    @Override
    public boolean canParse(String entityType) {
        return "POLYLINE".equals(entityType);
    }

    @Override
    public void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace) {
    }

    public static void createPolyline(List<Point> points, String dxfLineType, double thickness, DrawingContext context, Pane workspace) {
        double scale = context.getScale();
        AbstractEntityParser parser = new AbstractEntityParser() {
            @Override
            public boolean canParse(String entityType) {
                return false;
            }
            
            @Override
            public void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace) {
            }
        };
        
        if (isRectangle(points)) {
            Rectangle rect = new Rectangle(points);
            parser.applyProperties(rect, dxfLineType, thickness, scale);
            workspace.getChildren().add(rect);
        } else {
            Point center = calculateCenter(points);
            Polygon polygon = new Polygon(center, points);
            parser.applyProperties(polygon, dxfLineType, thickness, scale);
            workspace.getChildren().add(polygon);
        }
    }

    private static boolean isRectangle(List<Point> points) {
        double[] xs = points.stream().mapToDouble(Point::getX).distinct().toArray();
        double[] ys = points.stream().mapToDouble(Point::getY).distinct().toArray();
        return xs.length == 2 && ys.length == 2;
    }

    private static Point calculateCenter(List<Point> points) {
        double sumX = 0, sumY = 0;
        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }
}