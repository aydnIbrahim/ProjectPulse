package org.example.projectpulse;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MSocialTaskController {

    @FXML
    private Button addButton;

    @FXML
    private TextField newTaskField;

    @FXML
    private ListView<String> taskList;

    @FXML
    public void initialize() {
        taskList.getItems().add("newTask");
        addButton.setOnAction(event -> handleAddClick());

        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #234232; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: transparent; -fx-border-color: #234232; -fx-border-radius: 10; -fx-border-width: 1px; -fx-text-fill: #234232;"));
    }

    private void handleAddClick(){
        String newTask = newTaskField.getText();
        if(!newTask.isEmpty()){
            taskList.getItems().add(newTask);
            newTaskField.clear();
        }
    }

}
