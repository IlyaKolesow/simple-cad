package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.figures.Arc;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;

public class ArcDrawer extends FigureDrawer {
    public ArcDrawer(DrawingContext context) {
        super(context);
        modesComboBox.getItems().addAll(Mode.BY_3_POINTS, Mode.CHORD);
        modesComboBox.setValue(Mode.BY_3_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modesComboBox.getValue()) {
            case BY_3_POINTS:
                drawBy3Points();
                break;
            case CHORD:
                drawByChord();
                break;
        }
    }

    private void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, 3) {
            @Override
            public Figure buildFigure(Point[] points) {
                return new Arc(points[0], points[1], points[2]);
            }
        };

        byPoints.setupDrawing();
    }

    private void drawByChord() {

    }
}
