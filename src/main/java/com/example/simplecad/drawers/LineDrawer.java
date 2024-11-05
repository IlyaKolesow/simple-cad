package com.example.simplecad.drawers;

import com.example.simplecad.DrawingContext;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import javafx.scene.input.KeyCode;

public class LineDrawer extends Drawer {
    private Point firstPoint;

    public LineDrawer(DrawingContext context) {
        super(context);
    }

    public void drawBy2Points() {
        DrawerByPoints drawer = new DrawerByPoints(drawingContext, 2) {
            @Override
            public void drawFigure(Point[] points) {
                Line line = new Line(points[0], points[1]);
                workSpace.getChildren().add(line);
            }
        };

        drawer.setupDrawing();
//        setPrompts("Укажите координаты первой точки", "X", "Y");
//        pointInputHandler.handleInput((x, y) -> {
//            if (firstPoint != null) {
//                Line line = new Line(firstPoint, new Point(x, y));
//                workSpace.getChildren().add(line);
//            }
//            firstPoint = new Point(x, y);
//            workSpace.getChildren().add(firstPoint);
//            inputLabel.setText("Укажите координаты следующей точки");
//        });
    }

    public void drawByAngleAndLength() {
        setPrompts("Укажите координаты первой точки", "X", "Y");

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (firstPoint != null) {
                    double angle = Double.parseDouble(input1.getText());
                    double length = Double.parseDouble(input2.getText());
                    Line line = new Line(firstPoint, angle, length);
                    workSpace.getChildren().add(line);
                    firstPoint = new Point(line.getPoint2());
                } else {
                    double x = coordsCenter.getX() + Double.parseDouble(input1.getText());
                    double y = coordsCenter.getY() - Double.parseDouble(input2.getText());
                    firstPoint = new Point(x, y);
                    setPrompts("Укажите угол и длину линии", "Угол", "Длина");
                }
                workSpace.getChildren().add(firstPoint);
            }
        });
    }
}
