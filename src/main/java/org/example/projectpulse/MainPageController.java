package org.example.projectpulse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController {


    @FXML
    private Button carcounter;

    @FXML
    private Button drugtrackingsystem;

    @FXML
    private AnchorPane mainPageAnchor;

    @FXML
    private Button msocial;

    @FXML
    private Button recipegen;

    @FXML
    private Button todolist;

    private Button selectedButton;

    @FXML
    public void initialize() {

        carcounter.setOnAction(event -> handleButtonClick(carcounter));
        drugtrackingsystem.setOnAction(event -> handleButtonClick(drugtrackingsystem));
        msocial.setOnAction(event -> handleButtonClick(msocial));
        recipegen.setOnAction(event -> handleButtonClick(recipegen));
        todolist.setOnAction(event -> handleButtonClick(todolist));
    }

    private void handleButtonClick(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #fff; -fx-border-radius: 10; -fx-text-fill: white");
        }

        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #C0C0C0; -fx-border-radius: 10; -fx-text-fill: #C0C0C0");

        loadContent(clickedButton.getId());
    }

    private void loadContent(String buttonId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(buttonId + ".fxml"));
            BorderPane content = loader.load();
            mainPageAnchor.getChildren().setAll(content.getChildren());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
