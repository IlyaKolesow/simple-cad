package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class CircleDrawer extends FigureDrawer {
    private Point center;

    public CircleDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.BY_RADIUS, Mode.BY_3_POINTS);
        modesComboBox.setValue(Mode.BY_RADIUS);
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case BY_RADIUS:
                drawByCenterAndRadius();
                break;
            case BY_3_POINTS:
                drawBy3Points();
                break;
        }
    }

    public void drawByCenterAndRadius() {
        setPrompts("Укажите координаты центральной точки", "Y", "X", null);

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (center != null) {
                    double radius = getPointsDistance(center, new Point(e.getX(), e.getY()));
                    Circle circle = new Circle(center, radius);
                    workSpace.getChildren().add(circle);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "Y", "X", null);
                } else {
                    center = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "R", null, null);
                }
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (center != null) {
                    double radius = Double.parseDouble(input1.getText()) * drawingContext.getScale();
                    Circle circle = new Circle(center, radius);
                    workSpace.getChildren().add(circle);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "Y", "X", null);
                } else {
                    double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
                    double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
                    center = new Point(x, y);
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "R", null, null);
                }
            }
        });
    }

    public void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, 3) {
            @Override
            public void drawFigure(Point[] points) {
                Circle circle = new Circle(points[0], points[1], points[2]);
                workSpace.getChildren().add(circle);
            }
        };

        byPoints.setupDrawing();
    }
}
