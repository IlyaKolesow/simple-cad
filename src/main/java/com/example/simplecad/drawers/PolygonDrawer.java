package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Circle;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Polygon;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import static com.example.simplecad.util.MathCalculation.getPointsDistance;

public class PolygonDrawer extends FigureDrawer {
    private int n = 4;
    private Point center;

    public PolygonDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.INSCRIBED_IN_CIRCLE, Mode.CIRCUMSCRIBED_AROUND_CIRCLE);
        modesComboBox.setValue(Mode.INSCRIBED_IN_CIRCLE);
        input0.setText(String.valueOf(n));
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case INSCRIBED_IN_CIRCLE:
                drawInscribed();
                break;
            case CIRCUMSCRIBED_AROUND_CIRCLE:
                drawCircumscribed();
                break;
        }
    }

    private void drawInscribed() {
        setPrompts("Укажите координаты центральной точки", "Количество сторон", "X", "Y");

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (input0.getText() != null)
                    n = Integer.parseInt(input0.getText());

                if (center != null) {
                    Polygon polygon = new Polygon(center, new Point(e.getX(), e.getY()), n, Mode.INSCRIBED_IN_CIRCLE);
                    workSpace.getChildren().add(polygon);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "Количество сторон", "X", "Y");
                } else {
                    center = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "Количество сторон", "R", null);
                }
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (input0.getText() != null)
                    n = Integer.parseInt(input0.getText());

                if (center != null) {
                    double radius = Double.parseDouble(input1.getText()) * drawingContext.getScale();
                    Polygon polygon = new Polygon(center, radius, n, Mode.INSCRIBED_IN_CIRCLE);
                    workSpace.getChildren().add(polygon);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "Количество сторон", "X", "Y");
                } else {
                    double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
                    double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
                    center = new Point(x, y);
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "Количество сторон", "R", null);
                }
            }
        });
    }

    private void drawCircumscribed() {

    }
}
