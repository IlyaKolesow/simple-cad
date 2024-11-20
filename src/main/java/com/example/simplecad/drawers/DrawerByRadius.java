package com.example.simplecad.drawers;

import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public abstract class DrawerByRadius extends Drawer {
    private Point center;

    public DrawerByRadius(DrawingContext context) {
        super(context);
    }

    public void setupDrawing() {
//        setPrompts("Укажите координаты центральной точки", "X", "Y");
//
//        workSpace.setOnMouseClicked(e -> {
//            if (e.getButton() == MouseButton.PRIMARY) {
//                if (center != null) {
//                    double radius = getPointsDistance(center, new Point(e.getX(), e.getY()));
//                    drawFigure(center, radius);
//                    workSpace.getChildren().remove(center);
//                    center = null;
//                    setPrompts("Укажите координаты центральной точки", "X", "Y");
//                } else {
//                    center = new Point(e.getX(), e.getY());
//                    workSpace.getChildren().add(center);
//                    setPrompts("Укажите радиус", "R", null);
//                }
//            }
//        });
//
//        toolBar.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                if (center != null) {
//                    double radius = Double.parseDouble(input1.getText()) * drawingContext.getScale();
//                    drawFigure(center, radius);
//                    workSpace.getChildren().remove(center);
//                    center = null;
//                    setPrompts("Укажите координаты центральной точки", "X", "Y");
//                } else {
//                    double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
//                    double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
//                    center = new Point(x, y);
//                    workSpace.getChildren().add(center);
//                    setPrompts("Укажите радиус", "R", null);
//                }
//            }
//        });
    }

    public abstract void drawFigure(Point center, double radius);
}
