package org.example.projectpulse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MainContentController {

    @FXML
    private Button dependenciesButton;

    @FXML
    private Button designsButton;

    @FXML
    private Button projectTab;

    @FXML
    private Button reportsButton;

    @FXML
    private Button tasksButton;

    private Button selectedButton;

    @FXML
    public void initialize() {
        projectTab.setOnAction(event -> handleButtonClick(projectTab));
        reportsButton.setOnAction(event -> handleButtonClick(reportsButton));
        tasksButton.setOnAction(event -> handleButtonClick(tasksButton));
        dependenciesButton.setOnAction(event -> handleButtonClick(dependenciesButton));
        designsButton.setOnAction(event -> handleButtonClick(designsButton));
    }

    private void handleButtonClick(Button clickedButton) {
        if (selectedButton != null){
            selectedButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        }

        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #fff; -fx-border-radius: 10; -fx-text-fill: white");
    }

}
