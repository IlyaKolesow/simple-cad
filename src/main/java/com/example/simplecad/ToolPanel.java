package com.example.simplecad;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.CustomCursor;
import com.example.simplecad.util.DrawingContext;
import com.example.simplecad.util.InputBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

public class ToolPanel {
    private final DrawingContext context;
    private final Pane workspace;
    private final ToolBar inputTool;
    private final Point center;
    private final CustomCursor cursor;
    private double scale;

    public ToolPanel(DrawingContext context) {
        this.context = context;
        workspace = context.getWorkspace();
        cursor = context.getCursor();
        inputTool = context.getInputTool();
        scale = context.getScale();
        center = context.getCoordsCenter();
        zoomByScroll();
    }

    public void pan(MouseEvent e, double[] start) {
        double deltaX = e.getX() - start[0];
        double deltaY = e.getY() - start[1];

        workspace.getChildren().forEach(elem -> {
            if (elem instanceof Figure)
                ((Figure) elem).move(deltaX, deltaY);
        });

        start[0] = e.getX();
        start[1] = e.getY();
    }

    public void zoomByScroll() {
        workspace.setOnScroll(e -> {
            double wheelDirection = e.getDeltaY();
            double zoomCoef;

            zoomCoef = 1.1;
            if (wheelDirection < 0)
                zoomCoef = 0.9;

            zoom(zoomCoef);
        });
    }

    public void zoom(double zoomCoef) {
        scale *= zoomCoef;
        context.setScale(scale);

        workspace.getChildren().forEach(elem -> {
            if (elem instanceof Figure) {
                ((Figure) elem).scale(zoomCoef, cursor.getPosition());
                ((Figure) elem).setLineType(((Figure) elem).getLineType(), scale);
            }
        });
    }

    public void updateCoords(MouseEvent e, Label mouseX, Label mouseY) {
        mouseX.setText("X: " + String.format("%.1f", (e.getX() - center.getX()) / scale));
        mouseY.setText("Y: " + String.format("%.1f", (center.getY() - e.getY()) / scale));
    }

    public void rotate(List<Figure> figures) {
        InputBuilder inputBuilder = new InputBuilder(inputTool);
        inputBuilder.setPrompts("Выберите объекты для поворта");
        Button button = inputBuilder.addApplyButton();
        button.requestFocus();

        EventHandler<ActionEvent> eventHandler = new EventHandler<>() {
            private Point centralPoint;
            private Double angle;

            @Override
            public void handle(ActionEvent event) {
                List<Double> inputs = inputBuilder.readInputValues();
                if (!figures.isEmpty() && centralPoint == null && inputBuilder.getCoordsInputs().isEmpty()) {
                    inputBuilder.setPrompts("Укажите координаты центральной точки вращения", "X", "Y");
                } else if (centralPoint == null && !inputBuilder.getCoordsInputs().isEmpty()) {
                    double x = (center.getX() + inputs.get(0) * scale);
                    double y = (center.getY() - inputs.get(1) * scale);
                    centralPoint = new Point(x, y);
                    inputBuilder.setPrompts("Укажите угол поворота", "Угол");
                } else if (centralPoint != null) {
                    angle = inputs.get(0);
                    figures.forEach(figure -> figure.rotate(centralPoint, angle));
                    centralPoint = null;
                    inputBuilder.setPrompts("Выберите объекты для поворта");
                }
            }
        };

        button.setOnAction(eventHandler);
        inputTool.setOnKeyPressed(null);
        workspace.setOnMouseClicked(context.getDefaultMouseClickedHandler());
    }
}
