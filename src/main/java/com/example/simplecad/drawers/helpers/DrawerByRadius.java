package com.example.simplecad.drawers.helpers;

import com.example.simplecad.drawers.Drawer;
import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.util.InputBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.List;

public abstract class DrawerByRadius extends Drawer {
    private Point center;

    public DrawerByRadius(DrawingContext context, InputBuilder inputBuilder) {
        super(context);
        this.inputBuilder = inputBuilder;
    }

    public void setupDrawing() {
        setFirstActionPrompts();

        workSpace.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (center != null) {
                    setFirstActionPrompts();
                    workSpace.getChildren().add(buildFigure(center, new Point(e.getX(), e.getY())));
                    workSpace.getChildren().remove(center);
                    center = null;
                } else {
                    center = new Point(e.getX(), e.getY());
                    workSpace.getChildren().add(center);
                    setSecondActionPrompts();
                }
            }
        });

        toolBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                List<Double> inputs = inputBuilder.readInputValues();
                if (center != null) {
                    double radius = inputs.get(0) * drawingContext.getScale();
                    setFirstActionPrompts();
                    workSpace.getChildren().add(buildFigure(center, radius));
                    workSpace.getChildren().remove(center);
                    center = null;
                } else {
                    double x = (coordsCenter.getX() + inputs.get(0) * drawingContext.getScale());
                    double y = (coordsCenter.getY() - inputs.get(1) * drawingContext.getScale());
                    center = new Point(x, y);
                    workSpace.getChildren().add(center);
                    setSecondActionPrompts();
                }
            }
        });
    }

    protected abstract Figure buildFigure(Point center, double radius);
    protected abstract Figure buildFigure(Point center, Point vertex);
    protected abstract void setFirstActionPrompts();
    protected abstract void setSecondActionPrompts();
}
