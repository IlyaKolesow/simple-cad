package com.example.simplecad.drawers;

import com.example.simplecad.modes.DrawingMode;
import com.example.simplecad.drawers.helpers.DrawerByRadius;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Polygon;
import com.example.simplecad.util.DrawingContext;

public class PolygonDrawer extends FigureDrawer {
    private int n = 4;

    public PolygonDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(DrawingMode.INSCRIBED_IN_CIRCLE, DrawingMode.CIRCUMSCRIBED_AROUND_CIRCLE);
        modes.setValue(DrawingMode.INSCRIBED_IN_CIRCLE);
        inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");
        inputBuilder.getInputs().getLast().setText(String.valueOf(n));
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case INSCRIBED_IN_CIRCLE:
                draw(DrawingMode.INSCRIBED_IN_CIRCLE);
                break;
            case CIRCUMSCRIBED_AROUND_CIRCLE:
                draw(DrawingMode.CIRCUMSCRIBED_AROUND_CIRCLE);
                break;
        }
    }

    private void draw(DrawingMode mode) {
        DrawerByRadius byRadius = new DrawerByRadius(drawingContext, inputBuilder) {
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
                if (inputBuilder.getInputs().getLast().getText() != null)
                    n = Integer.parseInt(inputBuilder.getInputs().getLast().getText());
                inputBuilder.setPrompts("Укажите координаты центральной точки", "X", "Y", "Количество сторон");
                inputBuilder.getInputs().getLast().setText(String.valueOf(n));
            }

            @Override
            protected void setSecondActionPrompts() {
                if (inputBuilder.getInputs().getLast().getText() != null)
                    n = Integer.parseInt(inputBuilder.getInputs().getLast().getText());
                inputBuilder.setPrompts("Укажите радиус", "R", "Количество сторон");
                inputBuilder.getInputs().getLast().setText(String.valueOf(n));
            }
        };

        byRadius.setupDrawing();
    }
}
