package com.example.simplecad.dxf;

import com.example.simplecad.figures.*;
import com.example.simplecad.modes.LineType;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;

import java.io.BufferedWriter;
import java.io.IOException;

public class FigureWriter {
    private final Pane workspace;
    private final Point coordsCenter;
    private final double scale;
    private BufferedWriter bufferedWriter;

    public FigureWriter(DrawingContext drawingContext) {
        workspace = drawingContext.getWorkspace();
        scale = drawingContext.getScale();
        coordsCenter = drawingContext.getCoordsCenter();
    }

    public void write(BufferedWriter bufferedWriter) throws IOException {
        this.bufferedWriter = bufferedWriter;
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("SECTION\r\n");
        bufferedWriter.write("  2\r\n");
        bufferedWriter.write("ENTITIES\r\n");

        workspace.getChildren().stream()
                .filter(elem -> elem instanceof Figure figure &&
                        !"coordsLineX".equals(figure.getId()) &&
                        !"coordsLineY".equals(figure.getId()) &&
                        !"center".equals(figure.getId()))
                .map(node -> (Figure) node)
                .forEach(this::writeFigure);

        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("ENDSEC\r\n");
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("EOF\r\n");
    }

    private void writeFigure(Figure figure) {
        try {
            bufferedWriter.write("  0\r\n");
            bufferedWriter.write(figure.getDXFName() + "\r\n");

            // Слой
            bufferedWriter.write("  8\r\n");
            bufferedWriter.write("0\r\n");

            if (figure instanceof Line line)
                writeLineEntity(line);

            else if (figure instanceof Circle circle)
                writeCircleEntity(circle);

            else if (figure instanceof Arc arc)
                writeArcEntity(arc);

            else if (figure instanceof Rectangle rectangle)
                writePolylineEntity(rectangle.getPoints());

            else if (figure instanceof Polygon polygon)
                writePolylineEntity(polygon.getPoints());

            // Тип линии
            bufferedWriter.write("  6\r\n");
            bufferedWriter.write(getDxfLineType(figure.getLineType()) + "\r\n");

            // Толщина линии
            bufferedWriter.write("  370\r\n");
            bufferedWriter.write(figure.getThickness() * 100 + "\r\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeLineEntity(Line line) throws IOException {
        writePoint(line.getPoint1());
        writePoint2(line.getPoint2());
    }

    private void writeCircleEntity(Circle circle) throws IOException {
        writePoint(circle.getCenter());
        writeRadius(circle.getRadius());
    }

    private void writeArcEntity(Arc arc) throws IOException {
        writePoint(arc.getCenter());
        writeRadius(arc.getRadius());

        double startAngle = normalizeAngle(arc.getStartAngle());
        double endAngle = normalizeAngle(arc.getStartAngle() + arc.getLength());

        if (arc.getLength() < 0) {
            double temp = startAngle;
            startAngle = endAngle;
            endAngle = temp;
        }

        bufferedWriter.write("  50\r\n");
        bufferedWriter.write(startAngle + "\r\n");

        bufferedWriter.write("  51\r\n");
        bufferedWriter.write(endAngle + "\r\n");
    }

    private double normalizeAngle(double angle) {
        angle = angle % 360;
        if (angle < 0)
            angle += 360;
        return angle;
    }

    private void writePolylineEntity(Point[] points) throws IOException {
        // Флаг closed
        bufferedWriter.write("  70\r\n");
        bufferedWriter.write("1\r\n");

        for (Point point : points) {
            bufferedWriter.write("  0\r\n");
            bufferedWriter.write("VERTEX\r\n");
            bufferedWriter.write("  8\r\n");
            bufferedWriter.write("0\r\n");

            writePoint(point);
        }
        bufferedWriter.write("  0\r\n");
        bufferedWriter.write("SEQEND\r\n");
    }

    private void writePoint(Point point) throws IOException {
        double x = (point.getX() - coordsCenter.getX()) / scale;
        double y = (coordsCenter.getY() - point.getY()) / scale;

        bufferedWriter.write("  10\r\n");
        bufferedWriter.write(x + "\r\n");
        bufferedWriter.write("  20\r\n");
        bufferedWriter.write(y + "\r\n");
        bufferedWriter.write("  30\r\n");
        bufferedWriter.write("0.0\r\n");
    }

    private void writePoint2(Point point) throws IOException {
        double x = (point.getX() - coordsCenter.getX()) / scale;
        double y = (coordsCenter.getY() - point.getY()) / scale;

        bufferedWriter.write("  11\r\n");
        bufferedWriter.write(x + "\r\n");
        bufferedWriter.write("  21\r\n");
        bufferedWriter.write(y + "\r\n");
        bufferedWriter.write("  31\r\n");
        bufferedWriter.write("0.0\r\n");
    }

    private void writeRadius(double radius) throws IOException {
        bufferedWriter.write("  40\r\n");
        bufferedWriter.write((radius / scale) + "\r\n");
    }

    private String getDxfLineType(LineType lineType) {
        return switch (lineType) {
            case DASHED -> "DASHED";
            case DASH_DOT -> "DASHDOT";
            case DASH_DOT_DOT -> "DIVIDE";
            default -> "CONTINUOUS";
        };
    }
}
