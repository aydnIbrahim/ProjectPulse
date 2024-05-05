module org.example.projectpulse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.projectpulse to javafx.fxml;
    exports org.example.projectpulse;
}
