module com.example.simplecad {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simplecad to javafx.fxml;

    exports com.example.simplecad;
    exports com.example.simplecad.util;
    opens com.example.simplecad.util to javafx.fxml;
}