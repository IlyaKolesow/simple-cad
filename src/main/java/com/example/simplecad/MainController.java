package com.example.simplecad;

import com.example.simplecad.drawers.*;
import com.example.simplecad.editors.FigureEditor;
import com.example.simplecad.figures.*;
import com.example.simplecad.dxf.DXFReader;
import com.example.simplecad.dxf.DXFWriter;
import com.example.simplecad.util.CustomCursor;
import com.example.simplecad.util.DrawingContext;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane workspace;

    @FXML
    private Label mouseX, mouseY;

    @FXML
    private ToggleButton lineBtn, rectBtn, circleBtn, splineBtn, arcBtn, polygonBtn, panBtn, rotationBtn;

    @FXML
    private ToolBar inputTool;

    private CustomCursor cursor;
    private ToolPanel toolPanel;
    private DrawingContext drawingContext;
    private EventHandler<? super MouseEvent> previousMouseClickHandler;
    private EventHandler<MouseEvent> defaultMouseMovedHandler, defaultMouseClickedHandler, defaultMouseDraggedHandler, defaultMousePressedHandler;
    private final List<Figure> selectedFigures = new LinkedList<>();
    private final double[] panStart = new double[2];

    @FXML
    public void initialize() {
        cursorInit();
        coordsSystemInit();
        toggleGroupInit();
        defaultMouseHandlersInit();

        List<EventHandler<MouseEvent>> mouseHandlers = List.of(
                defaultMouseMovedHandler,
                defaultMouseClickedHandler,
                defaultMouseDraggedHandler,
                defaultMousePressedHandler
        );
        drawingContext = new DrawingContext(workspace, inputTool, mouseHandlers);
        toolPanel = new ToolPanel(drawingContext);

        borderPane.setLeft(null);
        borderPane.setOnKeyPressed(this::handleDeleteKey);
    }

    private void coordsSystemInit() {
        Point center = new Point(workspace.getPrefWidth() / 2, workspace.getPrefHeight() / 2);
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

        workspace.getChildren().addAll(lineX, lineY, center);
    }

    private void cursorInit() {
        cursor = new CustomCursor();
        cursor.setId("cursor");
        workspace.getChildren().add(cursor);
        workspace.setCursor(Cursor.NONE);
    }

    private void toggleGroupInit() {
        ToggleGroup toggleGroup = new ToggleGroup();
        List.of(lineBtn, rectBtn, circleBtn, splineBtn, arcBtn, polygonBtn, rotationBtn)
                .forEach(btn -> btn.setToggleGroup(toggleGroup));
    }

    private void setDefaultMouseMovedHandler() {
        defaultMouseMovedHandler = e -> {
            toolPanel.updateCoords(e, mouseX, mouseY);
            cursor.update(e);
            hovering(e);
        };
        workspace.setOnMouseMoved(defaultMouseMovedHandler);
    }

    private void setDefaultMouseClickedHandler() {
        defaultMouseClickedHandler = this::figureSelecting;
        workspace.setOnMouseClicked(defaultMouseClickedHandler);
    }

    private void setDefaultMousePressedHandler() {
        defaultMousePressedHandler = e -> {
            panStart[0] = e.getX();
            panStart[1] = e.getY();
        };
        workspace.setOnMousePressed(defaultMousePressedHandler);
    }

    private void setDefaultMouseDraggedHandler(MouseButton button) {
        defaultMouseDraggedHandler = event -> {
            cursor.update(event);
            if (event.getButton() == button)
                toolPanel.pan(event, panStart);
        };
        workspace.setOnMouseDragged(defaultMouseDraggedHandler);
    }

    private void defaultMouseHandlersInit() {
        setDefaultMouseMovedHandler();
        setDefaultMouseClickedHandler();
        setDefaultMousePressedHandler();
        setDefaultMouseDraggedHandler(MouseButton.MIDDLE);
    }

    private boolean isValidFigure(Figure figure) {
        return !"coordsLineX".equals(figure.getId()) && !"coordsLineY".equals(figure.getId()) && !"center".equals(figure.getId());
    }

    private Figure findHoveredFigure(MouseEvent e) {
        return (Figure) workspace.getChildren()
                .stream()
                .filter(elem ->
                        elem instanceof Figure figure &&
                                figure.isHover(e.getX(), e.getY()) &&
                                isValidFigure(figure))
                .findFirst()
                .orElse(null);
    }

    private void resetColors() {
        workspace.getChildren().forEach(elem -> {
            if (elem instanceof Figure figure && isValidFigure(figure))
                figure.setColor(Color.WHITE);
        });
    }

    private void hovering(MouseEvent e) {
        Figure hoveredFigure = findHoveredFigure(e);
        resetColors();
        if (hoveredFigure != null)
            hoveredFigure.setColor(Color.GRAY);
        selectedFigures.forEach(figure -> figure.setColor(Color.ORANGE));
    }

    private void figureSelecting(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            Figure hoveredFigure = findHoveredFigure(e);
            if (!rotationBtn.isSelected())
                borderPane.setLeft(null);
            resetColors();

            if (hoveredFigure != null) {
                if (!e.isShiftDown())
                    selectedFigures.clear();

                selectedFigures.add(hoveredFigure);
                selectedFigures.forEach(figure -> figure.setColor(Color.ORANGE));

                if (selectedFigures.size() == 1 && !rotationBtn.isSelected()) {
                    new FigureEditor(drawingContext, hoveredFigure).inputBarInit();
                    borderPane.setLeft(inputTool);
                }
            } else {
                selectedFigures.clear();
                workspace.setOnMouseDragged(defaultMouseDraggedHandler);
            }
        }
    }

    @FXML
    private void lineDrawing() {
        figureDrawing(lineBtn, new LineDrawer(drawingContext));
    }

    @FXML
    private void rectDrawing() {
        figureDrawing(rectBtn, new RectDrawer(drawingContext));
    }

    @FXML
    private void circleDrawing() {
        figureDrawing(circleBtn, new CircleDrawer(drawingContext));
    }

    @FXML
    private void splineDrawing() {
        figureDrawing(splineBtn, new SplineDrawer(drawingContext));
    }

    @FXML
    private void arcDrawing() {
        figureDrawing(arcBtn, new ArcDrawer(drawingContext));
    }

    @FXML
    private void polygonDrawing() {
        figureDrawing(polygonBtn, new PolygonDrawer(drawingContext));
    }

    private void figureDrawing(ToggleButton button, FigureDrawer drawer) {
        resetColors();
        selectedFigures.clear();

        if (button.isSelected()) {
            borderPane.setLeft(inputTool);
            panBtn.setSelected(false);
            drawer.startDrawing();
        } else {
            workspace.setOnMouseClicked(defaultMouseClickedHandler);
            borderPane.setLeft(null);
            previousMouseClickHandler = null;
        }
    }

    @FXML
    private void panByLBM() {
        if (workspace.getOnMouseClicked() != null)
            previousMouseClickHandler = workspace.getOnMouseClicked();

        if (panBtn.isSelected()) {
            workspace.setOnMouseClicked(null);
            setDefaultMouseDraggedHandler(MouseButton.PRIMARY);
        } else {
            setDefaultMouseDraggedHandler(MouseButton.MIDDLE);
            workspace.setOnMouseClicked(previousMouseClickHandler);
        }
    }

    @FXML
    private void zoomPlus() {
        toolPanel.zoom(1.1);
    }

    @FXML
    private void zoomMinus() {
        toolPanel.zoom(0.9);
    }

    @FXML
    private void rotate() {
        if (rotationBtn.isSelected()) {
            borderPane.setLeft(inputTool);
            toolPanel.rotate(selectedFigures);
        } else {
            workspace.setOnMouseClicked(defaultMouseClickedHandler);
            borderPane.setLeft(null);
        }
    }

    @FXML
    private void saveFile() {
        DXFWriter writer = new DXFWriter(drawingContext);
        writer.save();
    }

    @FXML
    private void openFile() {
        DXFReader reader = new DXFReader(drawingContext);
        reader.open();
    }

    private void handleDeleteKey(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && !selectedFigures.isEmpty()) {
            List<Figure> figuresToRemove = new LinkedList<>(selectedFigures);
            
            figuresToRemove.stream()
                .filter(this::isValidFigure)
                .forEach(figure -> workspace.getChildren().remove(figure));

            selectedFigures.clear();
            borderPane.setLeft(null);
        }
    }
}