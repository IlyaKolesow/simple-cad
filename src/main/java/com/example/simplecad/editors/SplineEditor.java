package com.example.simplecad.editors;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.figures.Spline;
import com.example.simplecad.util.DrawingContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class SplineEditor {
    private final DrawingContext context;
    private final Pane workSpace;
    private final Spline spline;
    private Point selectedPoint;

    public SplineEditor(DrawingContext context, Figure figure) {
        this.context = context;
        workSpace = context.getWorkSpace();
        spline = (Spline) figure;
    }

    public void pointMovement() {
        final double[] start = new double[2];
        workSpace.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                selectedPoint = spline.getSelectedPoint(e.getX(), e.getY()).orElse(null);
                start[0] = e.getX();
                start[1] = e.getY();
            }
        });

        workSpace.setOnMouseDragged(e -> {
            context.getDefaultMouseDraggedHandler().handle(e);
            if (e.getButton() == MouseButton.PRIMARY) {
                if (selectedPoint != null) {
                    double deltaX = e.getX() - start[0];
                    double deltaY = e.getY() - start[1];

                    selectedPoint.move(deltaX, deltaY);
                    spline.update();

                    start[0] = e.getX();
                    start[1] = e.getY();
                }
            }
        });
    }
}
