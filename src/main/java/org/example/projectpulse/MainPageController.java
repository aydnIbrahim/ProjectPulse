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

    private static String author;

    @FXML
    public void initialize() {
        drugtrackingsystem.setOnAction(event -> handleProjectButtonClick(drugtrackingsystem));
        msocial.setOnAction(event -> handleProjectButtonClick(msocial));
        todolist.setOnAction(event -> handleProjectButtonClick(todolist));
        project.setOnAction(actionEvent -> handleButtonBarClick(project));
        dependency.setOnAction(actionEvent -> handleButtonBarClick(dependency));
        task.setOnAction(actionEvent -> handleButtonBarClick(task));
    }

    private void handleProjectButtonClick(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: #fff; -fx-border-radius: 10; -fx-text-fill: white");
        }

        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: #fff; -fx-border-color: #fff; -fx-text-fill: #234232; -fx-border-radius: 10; -fx-background-radius: 10");

        if (selectedButton != null && selectedButtonBar != null)
            loadContent(selectedButton.getId(), selectedButtonBar.getId());
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
        if (buttonId2.equals(task.getId())) {
            try {
                author = buttonId1;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("tasks.fxml"));
                BorderPane content = loader.load();
                mainPageAnchor.getChildren().setAll(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String getAuthor() {
        return author;
    }
}
