module org.example.projectpulse {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.projectpulse to javafx.fxml;
    exports org.example.projectpulse;
}
