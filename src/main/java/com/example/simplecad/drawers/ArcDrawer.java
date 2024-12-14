package com.example.simplecad.drawers;

import com.example.simplecad.Mode;
import com.example.simplecad.drawers.helpers.DrawerByPoints;
import com.example.simplecad.figures.Arc;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;

public class ArcDrawer extends FigureDrawer {
    public ArcDrawer(DrawingContext context) {
        super(context);
        modes.getItems().addAll(Mode.BY_3_POINTS, Mode.CHORD);
        modes.setValue(Mode.BY_3_POINTS);
    }

    @Override
    public void startDrawing() {
        switch (modes.getValue()) {
            case BY_3_POINTS:
                drawBy3Points();
                break;
            case CHORD:
                drawByChord();
                break;
        }
    }

    private void drawBy3Points() {
        DrawerByPoints byPoints = new DrawerByPoints(drawingContext, inputBuilder, 3) {
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
