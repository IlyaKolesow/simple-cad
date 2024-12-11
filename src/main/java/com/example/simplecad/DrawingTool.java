package com.example.simplecad;

import com.example.simplecad.figures.Figure;
import com.example.simplecad.figures.Point;
import com.example.simplecad.util.DrawingContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.List;

public class DrawingTool {
    private final DrawingContext context;
    private final Pane workSpace;
    private final ToolBar toolBar;
    private final Point center;
    private double scale;

    public DrawingTool(DrawingContext context) {
        this.context = context;
        workSpace = context.getWorkSpace();
        toolBar = context.getToolBar();
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
            context.getCursor().update(e);

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
                ((Figure) elem).scale(zoomCoef, context.getCursor().getPosition());
        });

        scale *= zoomCoef;
        context.setScale(scale);
    }

    public void updateCoords(MouseEvent e, Label mouseX, Label mouseY) {
        mouseX.setText("X: " + String.format("%.1f", (e.getX() - center.getX()) / scale));
        mouseY.setText("Y: " + String.format("%.1f", (center.getY() - e.getY()) / scale));
    }

    public void rotate(List<Figure> figures) {
        Label label = new Label("Выберите объекты для поворта");
        TextField input1 = new TextField();
        TextField input2 = new TextField();
        Label prompt1 = new Label();
        Label prompt2 = new Label();
        HBox hBox1 = new HBox(prompt1, input1);
        HBox hBox2 = new HBox(prompt2, input2);
        Button button = new Button("Готово");

        toolBar.getItems().clear();
        toolBar.getItems().addAll(label, hBox1, hBox2, button);

        hBox1.setSpacing(10);
        hBox2.setSpacing(10);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox1.setVisible(false);
        hBox2.setVisible(false);
        input1.setMaxWidth(50);
        input2.setMaxWidth(50);

        final Point[] centralPoint = new Point[1];
        final double[] angle = new double[1];

        EventHandler<ActionEvent> eventHandler = event -> {
            if (!figures.isEmpty() && centralPoint[0] == null && !hBox1.isVisible() && !hBox2.isVisible()) {
                label.setText("Укажите координаты центральной точки вращения:");
                prompt1.setText("X:");
                prompt2.setText("Y:");
                hBox1.setVisible(true);
                hBox2.setVisible(true);
            } else if (centralPoint[0] == null && hBox1.isVisible() && hBox2.isVisible()) {
                double x = (center.getX() + Double.parseDouble(input1.getText()) * scale);
                double y = (center.getY() - Double.parseDouble(input2.getText()) * scale);
                centralPoint[0] = new Point(x, y);
                label.setText("Укажите угол поворота:");
                prompt1.setText("Угол:");
                hBox2.setVisible(false);
            } else if (centralPoint[0] != null) {
                angle[0] = Double.parseDouble(input1.getText());
                figures.forEach(figure -> figure.rotate(centralPoint[0], angle[0]));
                centralPoint[0] = null;
                label.setText("Выберите объекты для поворта");
                hBox1.setVisible(false);
                hBox2.setVisible(false);
            }
        };

        button.setOnAction(eventHandler);

//        toolBar.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//
//            }
//        });
    }
}
