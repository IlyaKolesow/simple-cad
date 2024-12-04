package com.example.simplecad;

import com.example.simplecad.drawers.*;
import com.example.simplecad.editors.FigureEditor;
import com.example.simplecad.figures.*;
import com.example.simplecad.util.CustomCursor;
import com.example.simplecad.util.DrawingContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane workSpace;

    @FXML
    private Label mouseX, mouseY;

    @FXML
    private ToggleButton lineBtn, rectBtn, circleBtn, splineBtn, arcBtn, polygonBtn, panBtn;

    @FXML
    private ToolBar inputTool;

    private CustomCursor cursor;
    private DrawingTool drawingTool;
    private DrawingContext drawingContext;
    private EventHandler<? super MouseEvent> previousMouseClickHandler;
    private EventHandler<MouseEvent> defaultMouseMovedHandler;
    private EventHandler<MouseEvent> defaultMouseClickedHandler;
    private Figure selectedFigure;

    @FXML
    public void initialize() {
        cursorInit();
        coordsSystemInit();
        toggleGroupInit();
        setDefaultMouseMovedHandler();
        setDefaultMouseClickedHandler();

        drawingContext = new DrawingContext(workSpace, inputTool, defaultMouseMovedHandler);
        drawingTool = new DrawingTool(drawingContext);

        borderPane.setLeft(null);
    }

    private void coordsSystemInit() {
        Point center = new Point(workSpace.getPrefWidth() / 2, workSpace.getPrefHeight() / 2);
        center.setId("center");
        center.setColor(Color.YELLOW);

        Line lineX = new Line(center.getX(), center.getY(), center.getX() + 2000, center.getY());
        Line lineY = new Line(center.getX(), center.getY(), center.getX(), center.getY() - 2000);
        lineX.setId("coordsLineX");
        lineY.setId("coordsLineY");
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

    private void toggleGroupInit() {
        ToggleGroup toggleGroup = new ToggleGroup();
        lineBtn.setToggleGroup(toggleGroup);
        rectBtn.setToggleGroup(toggleGroup);
        circleBtn.setToggleGroup(toggleGroup);
        splineBtn.setToggleGroup(toggleGroup);
        arcBtn.setToggleGroup(toggleGroup);
        polygonBtn.setToggleGroup(toggleGroup);
    }

    private void setDefaultMouseMovedHandler() {
        defaultMouseMovedHandler = e -> {
            drawingTool.updateCoords(e, mouseX, mouseY);
            cursor.update(e);
            hovering(e);
        };
        workSpace.setOnMouseMoved(defaultMouseMovedHandler);
    }

    private void setDefaultMouseClickedHandler() {
        defaultMouseClickedHandler = this::figureSelecting;
        workSpace.setOnMouseClicked(defaultMouseClickedHandler);
    }

    private Figure findHoveredFigure(MouseEvent e) {
        return (Figure) workSpace.getChildren()
                .stream()
                .filter(elem ->
                        elem instanceof Figure &&
                        ((Figure) elem).isHover(e.getX(), e.getY()) &&
                        !Objects.equals(elem.getId(), "coordsLineX") &&
                        !Objects.equals(elem.getId(), "coordsLineY"))
                .findFirst()
                .orElse(null);
    }

    private void resetColors() {
        workSpace.getChildren().forEach(elem -> {
            if (elem instanceof Figure && !Objects.equals(elem.getId(), "coordsLineX") && !Objects.equals(elem.getId(), "coordsLineY")) {
                ((Figure) elem).setColor(Color.WHITE);

                if (Objects.equals(elem.getId(), "center"))
                    ((Figure) elem).setColor(Color.YELLOW);
            }
        });
    }

    private void hovering(MouseEvent e) {
        Figure hoveredFigure = findHoveredFigure(e);
        resetColors();
        if (hoveredFigure != null)
            hoveredFigure.setColor(Color.GRAY);
        if (selectedFigure != null)
            selectedFigure.setColor(Color.ORANGE);
    }

    private void figureSelecting(MouseEvent e) {
        Figure hoveredFigure = findHoveredFigure(e);
        selectedFigure = null;
        borderPane.setLeft(null);
        resetColors();
        if (hoveredFigure != null) {
            hoveredFigure.setColor(Color.ORANGE);
            selectedFigure = hoveredFigure;
            new FigureEditor(drawingContext, selectedFigure).toolBarInit();
            borderPane.setLeft(inputTool);
        }
    }

    @FXML
    private void lineDrawing(ActionEvent event) {
        figureDrawing(lineBtn, new LineDrawer(drawingContext));
    }

    @FXML
    private void rectDrawing(ActionEvent event) {
        figureDrawing(rectBtn, new RectDrawer(drawingContext));
    }

    @FXML
    private void circleDrawing(ActionEvent event) {
        figureDrawing(circleBtn, new CircleDrawer(drawingContext));
    }

    @FXML
    private void splineDrawing(ActionEvent event) {
    }

    @FXML
    private void arcDrawing(ActionEvent event) {
    }

    @FXML
    private void polygonDrawing(ActionEvent event) {
        figureDrawing(polygonBtn, new PolygonDrawer(drawingContext));
    }

    private void figureDrawing(ToggleButton button, FigureDrawer drawer) {
        if (button.isSelected()) {
            borderPane.setLeft(inputTool);
            drawingTool.pan(MouseButton.MIDDLE);
            panBtn.setSelected(false);
            drawer.startDrawing();
        } else {
            workSpace.setOnMouseClicked(defaultMouseClickedHandler);
            borderPane.setLeft(null);
            previousMouseClickHandler = null;
        }
    }

    @FXML
    private void panByLBM(ActionEvent event) {
        if (workSpace.getOnMouseClicked() != null)
            previousMouseClickHandler = workSpace.getOnMouseClicked();

        if (panBtn.isSelected()) {
            workSpace.setOnMouseClicked(null);
            drawingTool.pan(MouseButton.PRIMARY);
        } else {
            drawingTool.pan(MouseButton.MIDDLE);
            workSpace.setOnMouseClicked(previousMouseClickHandler);
        }
    }

    @FXML
    private void zoomPlus(ActionEvent event) {
        drawingTool.zoom(1.1);
    }

    @FXML
    private void zoomMinus(ActionEvent event) {
        drawingTool.zoom(0.9);
    }
}
