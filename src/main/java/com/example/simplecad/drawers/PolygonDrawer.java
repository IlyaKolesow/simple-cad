package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Polygon;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class PolygonDrawer extends FigureDrawer {
    private int n = 4;
    private Point center;

    public PolygonDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.INSCRIBED_IN_CIRCLE, Mode.CIRCUMSCRIBED_AROUND_CIRCLE);
        modesComboBox.setValue(Mode.INSCRIBED_IN_CIRCLE);
        input3.setText(String.valueOf(n));
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case INSCRIBED_IN_CIRCLE:
                draw(Mode.INSCRIBED_IN_CIRCLE);
                break;
            case CIRCUMSCRIBED_AROUND_CIRCLE:
                draw(Mode.CIRCUMSCRIBED_AROUND_CIRCLE);
                break;
        }
    }

    private void draw(Mode mode) {
        setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (input3.getText() != null)
                    n = Integer.parseInt(input3.getText());

                if (center != null) {
                    Polygon polygon = new Polygon(center, new Point(e.getX(), e.getY()), n, mode);
                    workSpace.getChildren().add(polygon);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");
                } else {
                    center = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "R", null, "Количество сторон");
                }
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (input3.getText() != null)
                    n = Integer.parseInt(input3.getText());

                if (center != null) {
                    double radius = Double.parseDouble(input1.getText()) * drawingContext.getScale();
                    Polygon polygon = new Polygon(center, radius, n, mode);
                    workSpace.getChildren().add(polygon);
                    workSpace.getChildren().remove(center);
                    center = null;
                    setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");
                } else {
                    double x = (coordsCenter.getX() + Double.parseDouble(input1.getText()) * drawingContext.getScale());
                    double y = (coordsCenter.getY() - Double.parseDouble(input2.getText()) * drawingContext.getScale());
                    center = new Point(x, y);
                    workSpace.getChildren().add(center);
                    setPrompts("Укажите радиус", "R", null, "Количество сторон");
                }
            }
        });
    }
}
