module org.example.projectpulse {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.projectpulse to javafx.fxml;
    exports org.example.projectpulse;
    exports org.example.projectpulse.controllers;
    opens org.example.projectpulse.controllers to javafx.fxml;
}