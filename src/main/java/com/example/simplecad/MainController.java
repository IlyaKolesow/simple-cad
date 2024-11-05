package com.example.simplecad;

import com.example.simplecad.drawers.CircleDrawer;
import com.example.simplecad.drawers.LineDrawer;
import com.example.simplecad.drawers.RectDrawer;
import com.example.simplecad.figures.Line;
import com.example.simplecad.figures.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane workSpace;

    @FXML
    private Label mouseX;

    @FXML
    private Label mouseY;

    @FXML
    private ToggleButton lineBtn;

    @FXML
    private ToggleButton rectBtn;

    @FXML
    private ToggleButton circleBtn;

    @FXML
    private ToggleButton panBtn;

    @FXML
    private ComboBox<String> lineMode;

    @FXML
    private ComboBox<String> rectMode;

    @FXML
    private ComboBox<String> circleMode;

    @FXML
    private ToolBar inputTool;

    private CustomCursor cursor;
    private Tool tool;
    private DrawingContext drawingContext;

    @FXML
    public void initialize() {
        cursorInit();
        createCoordsSystem();
        tool = new Tool(workSpace);
        drawingContext = new DrawingContext(workSpace, inputTool);
        borderPane.setLeft(null);

        workSpace.setOnMouseMoved(e -> {
            tool.updateCoords(e, mouseX, mouseY);
            cursor.update(e);
        });
    }

    private void createCoordsSystem() {
        Point center = new Point(workSpace.getPrefWidth() / 2, workSpace.getPrefHeight() / 2);
        center.setId("center");
        center.setColor(Color.YELLOW);

        Line lineX = new Line(center.getX(), center.getY(), center.getX() + 2000, center.getY());
        Line lineY = new Line(center.getX(), center.getY(), center.getX(), center.getY() - 2000);
        lineX.setColor(Color.RED);
        lineY.setColor(Color.GREEN);

        lineX.getPoint1().setColor(null);
        lineX.getPoint2().setColor(null);
        lineY.getPoint1().setColor(null);
        lineY.getPoint2().setColor(null);

        workSpace.getChildren().addAll(lineX, lineY, center);
    }

    private void cursorInit() {
        cursor = new CustomCursor();
        cursor.setId("cursor");
        workSpace.getChildren().add(cursor);
        workSpace.setCursor(Cursor.NONE);
    }

    @FXML
    private void startLineDrawing(ActionEvent event) {
        if (lineBtn.isSelected()) {
            LineDrawer lineDrawer = new LineDrawer(drawingContext);

            resetToggleButtons();
            lineBtn.setSelected(true);
            borderPane.setLeft(inputTool);

            if (lineMode.getValue().equals("2 точки"))
                lineDrawer.drawBy2Points();
            else if (lineMode.getValue().equals("Угол и длина"))
                lineDrawer.drawByAngleAndLength();

        } else {
            workSpace.setOnMouseClicked(null);
            borderPane.setLeft(null);
        }
    }

    @FXML
    private void startRectDrawing(ActionEvent event) {
        if (rectBtn.isSelected()) {
            RectDrawer rectDrawer = new RectDrawer(drawingContext);

            resetToggleButtons();
            rectBtn.setSelected(true);
            borderPane.setLeft(inputTool);

            if (rectMode.getValue().equals("2 точки"))
                rectDrawer.drawBy2Points();
            else if (rectMode.getValue().equals("2 стороны"))
                rectDrawer.drawBy2Sides();

        } else {
            workSpace.setOnMouseClicked(null);
            borderPane.setLeft(null);
        }
    }

    @FXML
    private void startCircleDrawing(ActionEvent event) {
        if (circleBtn.isSelected()) {
            CircleDrawer circleDrawer = new CircleDrawer(drawingContext);

            resetToggleButtons();
            circleBtn.setSelected(true);
            borderPane.setLeft(inputTool);

            if (circleMode.getValue().equals(Mode.BY_RADIUS))
                circleDrawer.drawByCenterAndRadius();
            else if (circleMode.getValue().equals(Mode.BY_POINTS))
                circleDrawer.drawBy3Points();

        } else {
            workSpace.setOnMouseClicked(null);
            borderPane.setLeft(null);
        }
    }

    @FXML
    private void panByLBM(ActionEvent event) {
        if (panBtn.isSelected()) {
            resetToggleButtons();
            panBtn.setSelected(true);

            tool.pan(MouseButton.PRIMARY);
        } else {
            tool.pan(MouseButton.MIDDLE);
        }
    }

    private void resetToggleButtons() {
        lineBtn.setSelected(false);
        rectBtn.setSelected(false);
        circleBtn.setSelected(false);
        panBtn.setSelected(false);

        workSpace.setOnMouseClicked(null);

        tool.pan(MouseButton.MIDDLE);
    }

    @FXML
    private void zoomPlus(ActionEvent event) {
        tool.zoom(1.1);
    }

    @FXML
    private void zoomMinus(ActionEvent event) {
        tool.zoom(0.9);
    }
}
