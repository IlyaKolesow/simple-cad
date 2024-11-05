module com.example.simplecad {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simplecad to javafx.fxml;

    exports com.example.simplecad;
}