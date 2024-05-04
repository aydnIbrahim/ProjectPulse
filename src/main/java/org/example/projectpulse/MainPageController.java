package org.example.projectpulse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Button dependency;

    @FXML
    private Button drugtrackingsystem;

    @FXML
    private AnchorPane mainPageAnchor;

    @FXML
    private Button msocial;

    @FXML
    private Button project;

    @FXML
    private Button task;

    @FXML
    private Button todolist;

    private Button selectedButton;

    private Button selectedButtonBar;

    @FXML
    public void initialize() {

        drugtrackingsystem.setOnAction(event -> handleButtonClick(drugtrackingsystem));
        msocial.setOnAction(event -> handleButtonClick(msocial));
        todolist.setOnAction(event -> handleButtonClick(todolist));
        project.setOnAction(actionEvent -> handleButtonBarClick(project));
        dependency.setOnAction(actionEvent -> handleButtonBarClick(dependency));
        task.setOnAction(actionEvent -> handleButtonBarClick(task));
    }

    private void handleButtonClick(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #fff; -fx-border-radius: 10; -fx-text-fill: white");
        }

        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #C0C0C0; -fx-border-radius: 10; -fx-text-fill: #C0C0C0");

        project.setText(clickedButton.getText());

        mainPageAnchor.getChildren().clear();
        handleButtonBarClean();
    }

    private void handleButtonBarClick(Button clickedButton) {
        if (selectedButtonBar != null) {
            selectedButtonBar.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        }

        selectedButtonBar = clickedButton;
        selectedButtonBar.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: white; -fx-border-radius: 10; -fx-text-fill: white");

        loadContent(selectedButton.getId(), selectedButtonBar.getId());
    }

    private void loadContent(String buttonId1, String buttonId2){
        mainPageAnchor.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(buttonId1 + buttonId2 + ".fxml"));
            BorderPane content = loader.load();
            mainPageAnchor.getChildren().setAll(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleButtonBarClean(){
        project.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        dependency.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        task.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
    }

}
