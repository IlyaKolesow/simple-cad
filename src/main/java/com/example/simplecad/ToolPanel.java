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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

public class ToolPanel {
    private final DrawingContext context;
    private final Pane workSpace;
    private final ToolBar inputTool;
    private final Point center;
    private final CustomCursor cursor;
    private double scale;

    public ToolPanel(DrawingContext context) {
        this.context = context;
        workSpace = context.getWorkSpace();
        cursor = context.getCursor();
        inputTool = context.getInputTool();
        scale = context.getScale();
        center = context.getCoordsCenter();
        pan(MouseButton.MIDDLE);
        zoomByScroll();
    }

    public void pan(MouseButton mouseButton) {
        final double[] start = new double[2];

        workSpace.setOnMousePressed(e -> {
            if (e.getButton() == mouseButton) {
                start[0] = e.getX();
                start[1] = e.getY();
            }
        });

        workSpace.setOnMouseDragged(e -> {
            cursor.update(e);

            if (e.getButton() == mouseButton) {
                double deltaX = e.getX() - start[0];
                double deltaY = e.getY() - start[1];

                workSpace.getChildren().forEach(elem -> {
                    if (elem instanceof Figure)
                        ((Figure) elem).move(deltaX, deltaY);
                });

                start[0] = e.getX();
                start[1] = e.getY();
            }
        });
    }

    public void zoomByScroll() {
        workSpace.setOnScroll(e -> {
            double wheelDirection = e.getDeltaY();
            double zoomCoef;

            zoomCoef = 1.1;
            if (wheelDirection < 0)
                zoomCoef = 0.9;

            zoom(zoomCoef);
        });
    }

    public void zoom(double zoomCoef) {
        workSpace.getChildren().forEach(elem -> {
            if (elem instanceof Figure)
                ((Figure) elem).scale(zoomCoef, cursor.getPosition());
        });

        scale *= zoomCoef;
        context.setScale(scale);
    }

    public void updateCoords(MouseEvent e, Label mouseX, Label mouseY) {
        mouseX.setText("X: " + String.format("%.1f", (e.getX() - center.getX()) / scale));
        mouseY.setText("Y: " + String.format("%.1f", (center.getY() - e.getY()) / scale));
    }

    public void rotate(List<Figure> figures) {
        InputBuilder inputBuilder = new InputBuilder(inputTool);
        inputBuilder.setPrompts("Выберите объекты для поворта");
        Button button = new Button("Готово");

        final Point[] centralPoint = new Point[1];
        final Double[] angle = new Double[1];

        EventHandler<ActionEvent> eventHandler = event -> {
            if (centralPoint[0] != null && angle[0] == null) {
                angle[0] = Double.parseDouble(inputBuilder.getInputs().get(0).getText());
                figures.forEach(figure -> figure.rotate(centralPoint[0], angle[0]));
//                centralPoint[0] = null;
//                angle[0] = null;
                inputBuilder.setPrompts("Выберите объекты для поворта");
                inputTool.getItems().add(button);
            } else if (centralPoint[0] != null && angle[0] != null) {
                inputBuilder.setPrompts("Укажите координаты центральной точки вращения", "X", "Y");
                inputTool.getItems().add(button);
                centralPoint[0] = null;
                angle[0] = null;
            } else if (!figures.isEmpty()) {
                inputBuilder.setPrompts("Укажите координаты центральной точки вращения", "X", "Y");
                inputTool.getItems().add(button);
            }



            if (!figures.isEmpty() && centralPoint[0] == null && !inputBuilder.getInputs().isEmpty()) {
                inputBuilder.setPrompts("Укажите координаты центральной точки вращения", "X", "Y");
            } else if (centralPoint[0] == null && !inputBuilder.getInputs().isEmpty()) {
                double x = (center.getX() + Double.parseDouble(inputBuilder.getInputs().get(0).getText()) * scale);
                double y = (center.getY() - Double.parseDouble(inputBuilder.getInputs().get(1).getText()) * scale);
                centralPoint[0] = new Point(x, y);
                inputBuilder.setPrompts("Укажите угол поворота:", "Угол:");
            } else if (centralPoint[0] != null) {
                angle[0] = Double.parseDouble(inputBuilder.getInputs().get(0).getText());
                figures.forEach(figure -> figure.rotate(centralPoint[0], angle[0]));
                centralPoint[0] = null;
                inputBuilder.setPrompts("Выберите объекты для поворта");
            }
        };

        button.setOnAction(eventHandler);

//        toolBar.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//
//            }
//        });

        workSpace.setOnMouseClicked(context.getDefaultMouseClickedHandler());
    }
}
