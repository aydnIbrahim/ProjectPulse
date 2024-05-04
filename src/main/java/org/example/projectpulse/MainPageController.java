package org.example.projectpulse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Button carcounterTab;

    @FXML
    private Button drugtrackingsystemTab;

    @FXML
    private AnchorPane mainPageAnchor;

    @FXML
    private Button msocialTab;

    @FXML
    private Button recipegenTab;

    @FXML
    private Button todolistTab;

    private Button selectedButton;

    @FXML
    public void initialize() {

        carcounterTab.setOnAction(event -> handleButtonClick(carcounterTab));
        drugtrackingsystemTab.setOnAction(event -> handleButtonClick(drugtrackingsystemTab));
        msocialTab.setOnAction(event -> handleButtonClick(msocialTab));
        recipegenTab.setOnAction(event -> handleButtonClick(recipegenTab));
        todolistTab.setOnAction(event -> handleButtonClick(todolistTab));
    }

    private void handleButtonClick(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #fff; -fx-border-radius: 10; -fx-text-fill: white");
        }

        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #C0C0C0; -fx-border-radius: 10; -fx-text-fill: #C0C0C0");

        loadContent();
    }

    private void loadContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-content.fxml"));
            BorderPane content = loader.load();
            mainPageAnchor.getChildren().setAll(content.getChildren());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
