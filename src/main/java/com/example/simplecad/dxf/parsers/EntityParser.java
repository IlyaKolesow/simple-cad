package com.example.simplecad.dxf.parsers;

import com.example.simplecad.util.DrawingContext;
import javafx.scene.layout.Pane;
import java.util.Map;

public interface EntityParser {
    boolean canParse(String entityType);
    void parse(Map<Integer, String> entityData, DrawingContext context, Pane workspace);
}