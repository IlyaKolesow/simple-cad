package com.example.simplecad.util;

import com.example.simplecad.LineType;
import com.example.simplecad.Mode;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputBuilder {
    private final ToolBar toolBar;
    private final Label label;
    private final List<Label> prompts;
    private final List<TextField> inputs;
    private final ComboBox<Mode> modes;
    private final ComboBox<LineType> lineTypes;
    private Button applyBtn;
    private TextField thickness;

    public InputBuilder(ToolBar toolBar) {
        this.toolBar = toolBar;
        label = new Label();
        prompts = new ArrayList<>();
        inputs = new ArrayList<>();
        thickness = new TextField();
        modes = new ComboBox<>();
        lineTypes = new ComboBox<>();
    }

    public void setPrompts(String label, String... prompts) {
        this.prompts.clear();
        this.inputs.clear();
        
        for (String text : prompts) {
            this.prompts.add(new Label(text + ":"));
            inputs.add(new TextField());
        }
        this.label.setText(label + ":");
        
        update();
    }

    public void setPrompts(String label, Map<String, Double> prompts, double scale) {
        this.prompts.clear();
        this.inputs.clear();

        for (Map.Entry<String, Double> entry : prompts.entrySet()) {
            this.prompts.add(new Label(entry.getKey() + ":"));
            String value = String.format("%.1f", entry.getValue() / scale);
            inputs.add(new TextField(value.replace(",", ".")));
        }

        this.label.setText(label + ":");

        update();
    }

    public List<TextField> getInputs() {
        return inputs;
    }

    public List<Double> readInputValues() {
        List<Double> values = new ArrayList<>();
        for (TextField field : inputs) {
            String text = field.getText();
            values.add(Double.parseDouble(text.replace(",", ".")));
        }
        return values;
    }

    private void update() {
        toolBar.getItems().clear();
        if (!modes.getItems().isEmpty())
            toolBar.getItems().add(modes);
        toolBar.getItems().add(label);

        for (int i = 0; i < prompts.size(); i++) {
            HBox hBox = new HBox(prompts.get(i), inputs.get(i));
            toolBar.getItems().add(hBox);

            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            inputs.get(i).setMaxWidth(50);
        }

        if (!lineTypes.getItems().isEmpty())
            toolBar.getItems().add(lineTypes);
        if (applyBtn != null)
            toolBar.getItems().add(applyBtn);
    }
    
    public ComboBox<Mode> addModeSelection() {
        toolBar.getItems().addFirst(modes);
        return modes;
    }

    public ComboBox<LineType> addLineTypeSelection() {
        toolBar.getItems().addLast(lineTypes);
        return lineTypes;
    }

    public Button addApplyButton() {
        applyBtn = new Button("Готово");
        toolBar.getItems().addLast(applyBtn);
        return applyBtn;
    }

    public void addThicknessInput(double defaultValue) {
        thickness = new TextField(String.valueOf(defaultValue));
        prompts.add(new Label("Толщина линии:"));
        inputs.add(thickness);
        toolBar.getItems().add(thickness);
        update();
    }

    public double getThicknessValue() {
        return Double.parseDouble(thickness.getText().replace(",", "."));
    }
}
