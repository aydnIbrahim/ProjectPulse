package org.example.projectpulse;

import dao.TaskDao;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Task;

import java.sql.SQLException;
import java.util.ArrayList;

public class MSocialTaskController {

    @FXML
    private Button addButton;

    @FXML
    private TextField newTaskField;

    @FXML
    private ListView<HBox> taskList;


    @FXML
    public void initialize() throws SQLException {

        loadContent();

        addButton.setOnAction(event -> handleAddClick());

        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #234232; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: transparent; -fx-border-color: #234232; -fx-border-radius: 10; -fx-border-width: 1px; -fx-text-fill: #234232;"));
    }

    private void handleAddClick(){
        String newTask = newTaskField.getText();
        if(!newTask.isEmpty()){
            addNewTask(newTask);
            newTaskField.clear();
        }
    }

    private void addNewTask(String newTask) {
        HBox taskBox = new HBox();
        Label taskLabel = new Label(newTask);
        Button completedButton = new Button("Completed");

        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setSpacing(10);
        taskBox.setPadding(new Insets(0, 10, 0, 10));

        taskLabel.setPrefSize(350, 17);

        completedButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 10px; -fx-border-radius: 10");
        completedButton.setPrefSize(62, 22);
        completedButton.setCursor(Cursor.HAND);

        completedButton.setOnMouseEntered(e -> completedButton.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 10px"));
        completedButton.setOnMouseExited(e -> completedButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 10px; -fx-border-radius: 10"));

        taskBox.getChildren().addAll(taskLabel, completedButton);
        taskList.getItems().add(taskBox);
    }

    private void loadContent() throws SQLException {
        ArrayList<Task> tasks;
        TaskDao taskDao = new TaskDao();
        tasks = taskDao.getAllTask("msocial");
        for (Task task : tasks) {
            if (task.getCompleted() != 1)
                addNewTask(task.getContent());
        }

    }
}
