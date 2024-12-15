package com.example.simplecad.util;

import com.example.simplecad.Mode;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class InputBuilder {
    private final ToolBar toolBar;
    private final Label label;
    private final List<Label> prompts;
    private final List<TextField> inputs;
    private final ComboBox<Mode> modes;
    private Button applyBtn;

    public InputBuilder(ToolBar toolBar) {
        this.toolBar = toolBar;
        label = new Label();
        prompts = new ArrayList<>();
        inputs = new ArrayList<>();
        modes = new ComboBox<>();
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

    public List<TextField> getInputs() {
        return inputs;
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

        if (applyBtn != null)
            toolBar.getItems().add(applyBtn);
    }
    
    public ComboBox<Mode> addModeSelection() {
        toolBar.getItems().addFirst(modes);
        return modes;
    }

    public Button addApplyButton() {
        applyBtn = new Button("Готово");
        toolBar.getItems().addLast(applyBtn);
        return applyBtn;
    }
}
