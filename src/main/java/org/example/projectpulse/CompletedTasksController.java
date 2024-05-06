package org.example.projectpulse;

import dao.TaskDao;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class CompletedTasksController {

    @FXML
    private Label alertLabel;

    @FXML
    private Button ongoingTask;

    @FXML
    private ListView<HBox> taskList;

    @FXML
    public void initialize() {

        ongoingTask.setOnMouseEntered(e -> ongoingTask.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white"));
        ongoingTask.setOnMouseExited(e -> ongoingTask.setStyle("-fx-background-color: transparent; -fx-text-fill: #0D2BC1; -fx-border-color: #0D2BC1; -fx-border-radius: 10"));
    }

    public void loadContent() throws SQLException {
        ArrayList<Task> tasks = new ArrayList<>();
        TaskDao taskDao = new TaskDao();
        tasks = taskDao.getAllTask(MainPageController.getAuthor());
        for (Task task : tasks) {
            if (task.getCompleted() != 0)
                addNewTask(task);
        }
    }

    public void addNewTask(Task task) throws SQLException {
        HBox taskBox = new HBox();

        Label taskLabel = new Label(task.getContent());
        Label priorityLabel = new Label("" + task.getPriorityLevel());
        Label line = new Label("|");
        Label assigneeLabel = new Label(Optional.ofNullable(task.getAssignee()).filter(s -> !s.isEmpty()).orElse("Idle Task"));

        Button undoButton = new Button("Undo");
        Button deleteButton = new Button();
        Button tagButton = new Button();

        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setSpacing(5);
        taskBox.setPadding(new Insets(0, 0, 0, 10));

        taskLabel.setPrefSize(514, 17);
        taskLabel.setStyle("-fx-font-size: 14");

        assigneeLabel.setPrefSize(175, 17);
        assigneeLabel.setStyle("-fx-font-size: 14; font-weight: bold; -fx-text-fill: #234232");

        priorityLabel.setPrefSize(1, 1);
        priorityLabel.setMinSize(1, 1);
        priorityLabel.setVisible(false);

        undoButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 14px; -fx-border-radius: 12");
        undoButton.setPrefSize(60, 30);
        undoButton.setCursor(Cursor.HAND);
        undoButton.setVisible(false);
        undoButton.setOnMouseEntered(e -> undoButton.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 14px"));
        undoButton.setOnMouseExited(e -> undoButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 14px; -fx-border-radius: 10"));

        ImageView minusCircle = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/minus.circle.png"))));
        ImageView minusCircleFill = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/minus.circle.fill.png"))));
        deleteButton.setGraphic(minusCircle);
        deleteButton.setStyle("-fx-background-color: transparent");
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setVisible(false);
        deleteButton.setOnMouseEntered(e -> deleteButton.setGraphic(minusCircleFill));
        deleteButton.setOnMouseExited(e -> deleteButton.setGraphic(minusCircle));

        ImageView tag = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/green.circle.fill.priority.view.png"))));
        if(task.getPriorityPath() != null) {
            tag = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(task.getPriorityPath()))));
        }
        tag.setFitWidth(20);
        tag.setFitHeight(20);
        tagButton.setGraphic(tag);
        tagButton.setStyle("-fx-background-color: transparent");

        ImageView assign;
        if (task.getAssigneePhotoPath() != null)
            assign = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(task.getAssigneePhotoPath()))));
        else
            assign = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/person.circle.fill.png"))));
        assign.setFitWidth(30);
        assign.setFitHeight(30);

        taskBox.getChildren().addAll(tagButton, taskLabel, line, assign, assigneeLabel, undoButton, deleteButton, priorityLabel);
        taskList.getItems().add(taskBox);
    }



}
