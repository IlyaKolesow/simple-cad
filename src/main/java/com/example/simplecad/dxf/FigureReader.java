package com.example.simplecad.dxf;

import com.example.simplecad.figures.*;
import com.example.simplecad.modes.LineType;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FigureReader {
    private final Pane workspace;
    private final Point coordsCenter;
    private final double scale;
    private BufferedReader reader;
    private String currentLine;

    private double x1, y1, x2, y2;
    private double centerX, centerY, radius;
    private double startAngle, endAngle;
    private String lineType;
    private double thickness;
    private List<Point> polylinePoints;

    public FigureReader(DrawingContext drawingContext) {
        workspace = drawingContext.getWorkspace();
        scale = drawingContext.getScale();
        coordsCenter = drawingContext.getCoordsCenter();
        resetParameters();
    }

    private void resetParameters() {
        x1 = y1 = x2 = y2 = centerX = centerY = radius = startAngle = endAngle = 0;
        lineType = "CONTINUOUS";
        thickness = 1.0;
        polylinePoints = new ArrayList<>();
    }

    public void read(BufferedReader reader) throws IOException {
        this.reader = reader;
        readEntities();
    }

    private void readEntities() throws IOException {
        while ((currentLine = reader.readLine()) != null) {
            currentLine = currentLine.trim();
            if (currentLine.equals("SECTION")) {
                String sectionCode = reader.readLine().trim();
                String sectionName = reader.readLine().trim();
                if (sectionName.equals("ENTITIES")) {
                    readFigures();
                    break;
                }
            }
        }
    }

    private void readFigures() throws IOException {
        resetParameters();
        boolean isInPolyline = false;
        String entityType = null;

        while ((currentLine = reader.readLine()) != null) {
            currentLine = currentLine.trim();

            if (!currentLine.equals("0") && !currentLine.equals("  0")) {
                processCodeAndValue(entityType);
                continue;
            }

            if (entityType != null && !entityType.equals("VERTEX"))
                processFigure(entityType);

            entityType = reader.readLine().trim();

            if (entityType.equals("ENDSEC")) {
                break;
            } else if (entityType.equals("SEQEND")) {
                processEndSequence();
                entityType = null;
                isInPolyline = false;
            } else if (entityType.equals("POLYLINE")) {
                isInPolyline = true;
                polylinePoints = new ArrayList<>();
            } else if (!entityType.equals("VERTEX") && !isInPolyline) {
                resetParameters();
            }
        }
    }

    private void processCodeAndValue(String entityType) throws IOException {
        if (entityType == null) return;

        int code;
        try {
            code = Integer.parseInt(currentLine);
        } catch (NumberFormatException e) {
            return;
        }

        String value = reader.readLine().trim();
        processEntityAttribute(entityType, code, value);
    }

    private void processEndSequence() {
        if (!polylinePoints.isEmpty()) {
            createPolyline(polylinePoints, lineType, thickness);
            polylinePoints.clear();
        }
    }

    private void processEntityAttribute(String entityType, int code, String value) {
        switch (entityType) {
            case "LINE" -> processLineAttribute(code, value);
            case "CIRCLE" -> processCircleAttribute(code, value);
            case "ARC" -> processArcAttribute(code, value);
            case "POLYLINE" -> processPolylineAttribute(code, value);
            case "VERTEX" -> processVertexAttribute(code, value);
        }
    }

    private void processFigure(String entityType) {
        switch (entityType) {
            case "LINE" -> createLine(x1, y1, x2, y2, lineType, thickness);
            case "CIRCLE" -> createCircle(centerX, centerY, radius, lineType, thickness);
            case "ARC" -> createArc(centerX, centerY, radius, startAngle, endAngle, lineType, thickness);
        }
    }

    private void processLineAttribute(int code, String value) {
        switch (code) {
            case 10 -> x1 = Double.parseDouble(value);
            case 20 -> y1 = Double.parseDouble(value);
            case 11 -> x2 = Double.parseDouble(value);
            case 21 -> y2 = Double.parseDouble(value);
            case 6 -> lineType = value;
            case 370 -> thickness = Double.parseDouble(value) / 100.0;
        }
    }

    private void processCircleAttribute(int code, String value) {
        switch (code) {
            case 10 -> centerX = Double.parseDouble(value);
            case 20 -> centerY = Double.parseDouble(value);
            case 40 -> radius = Double.parseDouble(value);
            case 6 -> lineType = value;
            case 370 -> thickness = Double.parseDouble(value) / 100.0;
        }
    }

    private void processArcAttribute(int code, String value) {
        switch (code) {
            case 10 -> centerX = Double.parseDouble(value);
            case 20 -> centerY = Double.parseDouble(value);
            case 40 -> radius = Double.parseDouble(value);
            case 50 -> startAngle = Double.parseDouble(value);
            case 51 -> endAngle = Double.parseDouble(value);
            case 6 -> lineType = value;
            case 370 -> thickness = Double.parseDouble(value) / 100.0;
        }
    }

    private void processPolylineAttribute(int code, String value) {
        switch (code) {
            case 6 -> lineType = value;
            case 370 -> thickness = Double.parseDouble(value) / 100.0;
        }
    }

    private void processVertexAttribute(int code, String value) {
        switch (code) {
            case 10 -> x1 = Double.parseDouble(value);
            case 20 -> {
                y1 = Double.parseDouble(value);
                double sceneX = coordsCenter.getX() + x1 * scale;
                double sceneY = coordsCenter.getY() - y1 * scale;
                polylinePoints.add(new Point(sceneX, sceneY));
            }
        }
    }

    private void createLine(double x1, double y1, double x2, double y2, String dxfLineType, double thickness) {
        double sceneX1 = coordsCenter.getX() + x1 * scale;
        double sceneY1 = coordsCenter.getY() - y1 * scale;
        double sceneX2 = coordsCenter.getX() + x2 * scale;
        double sceneY2 = coordsCenter.getY() - y2 * scale;

        Line line = new Line(sceneX1, sceneY1, sceneX2, sceneY2);
        applyProperties(line, dxfLineType, thickness);
        workspace.getChildren().add(line);
    }

    private void createCircle(double centerX, double centerY, double radius, String dxfLineType, double thickness) {
        double sceneCenterX = coordsCenter.getX() + centerX * scale;
        double sceneCenterY = coordsCenter.getY() - centerY * scale;
        double sceneRadius = radius * scale;

        Circle circle = new Circle(new Point(sceneCenterX, sceneCenterY), sceneRadius);
        applyProperties(circle, dxfLineType, thickness);
        workspace.getChildren().add(circle);
    }

    private void createArc(double centerX, double centerY, double radius, double startAngle, double endAngle, String dxfLineType, double thickness) {
        double sceneCenterX = coordsCenter.getX() + centerX * scale;
        double sceneCenterY = coordsCenter.getY() - centerY * scale;
        double sceneRadius = radius * scale;

        double startRadians = Math.toRadians(startAngle);
        double endRadians = Math.toRadians(endAngle);
        double midRadians = startRadians + (endRadians - startRadians) / 2;
        if (startRadians > endRadians)
            midRadians = startRadians + (endRadians + 2 * Math.PI - startRadians) / 2;

        Point point1 = new Point(
                sceneCenterX + sceneRadius * Math.cos(startRadians),
                sceneCenterY - sceneRadius * Math.sin(startRadians)
        );
        Point point2 = new Point(
                sceneCenterX + sceneRadius * Math.cos(midRadians),
                sceneCenterY - sceneRadius * Math.sin(midRadians)
        );
        Point point3 = new Point(
                sceneCenterX + sceneRadius * Math.cos(endRadians),
                sceneCenterY - sceneRadius * Math.sin(endRadians)
        );

        Arc arc = new Arc(point1, point2, point3);
        applyProperties(arc, dxfLineType, thickness);
        workspace.getChildren().add(arc);
    }

    private void createPolyline(List<Point> points, String dxfLineType, double thickness) {
        if (isRectangle(points)) {
            Rectangle rect = new Rectangle(points);
            applyProperties(rect, dxfLineType, thickness);
            workspace.getChildren().add(rect);
        } else {
            Point center = calculateCenter(points);
            Polygon polygon = new Polygon(center, points);
            applyProperties(polygon, dxfLineType, thickness);
            workspace.getChildren().add(polygon);
        }
    }

    private boolean isRectangle(List<Point> points) {
        double[] xs = points.stream().mapToDouble(Point::getX).distinct().toArray();
        double[] ys = points.stream().mapToDouble(Point::getY).distinct().toArray();
        return xs.length == 2 && ys.length == 2;
    }

    private Point calculateCenter(List<Point> points) {
        double sumX = 0, sumY = 0;
        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }

    private void applyProperties(Figure figure, String dxfLineType, double thickness) {
        LineType lineType = getLineType(dxfLineType);
        figure.setLineType(lineType, scale);
        figure.setThickness(thickness);
        figure.setColor(Color.WHITE);
    }

    private LineType getLineType(String lineType) {
        return switch (lineType) {
            case "DASHED" -> LineType.DASHED;
            case "DASHDOT" -> LineType.DASH_DOT;
            case "DIVIDE" -> LineType.DASH_DOT_DOT;
            default -> LineType.SOLID;
        };
    }
}