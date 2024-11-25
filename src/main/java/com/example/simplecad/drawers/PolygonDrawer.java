package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Polygon;
import com.example.simplecad.util.DrawingContext;

public class PolygonDrawer extends FigureDrawer {
    private int n = 4;

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
        DrawerByRadius byRadius = new DrawerByRadius(drawingContext) {
            @Override
            protected Figure buildFigure(Point center, double radius) {
                return new Polygon(center, radius, n, mode);
            }

            @Override
            protected Figure buildFigure(Point center, Point vertex) {
                return new Polygon(center, vertex, n, mode);
            }

            @Override
            protected void setFirstActionPrompts() {
                if (input3.getText() != null)
                    n = Integer.parseInt(input3.getText());
                setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");
                input3.setText(String.valueOf(n));
            }

            @Override
            protected void setSecondActionPrompts() {
                if (input3.getText() != null)
                    n = Integer.parseInt(input3.getText());
                setPrompts("Укажите радиус", "R", null, "Количество сторон");
                input3.setText(String.valueOf(n));
            }
        };

        byRadius.setupDrawing();
    }
}
