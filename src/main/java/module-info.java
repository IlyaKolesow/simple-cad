module com.example.simplecad {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simplecad to javafx.fxml;

    exports com.example.simplecad;
    exports com.example.simplecad.util;
    opens com.example.simplecad.util to javafx.fxml;
    exports com.example.simplecad.figures;
    opens com.example.simplecad.figures to javafx.fxml;
    exports com.example.simplecad.modes;
    opens com.example.simplecad.modes to javafx.fxml;
}