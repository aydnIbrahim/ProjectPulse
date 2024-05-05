package org.example.projectpulse;

import dao.TaskDao;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MSocialTaskController {

    @FXML
    private Button addButton;

    @FXML
    private Button completedTasks;

    @FXML
    private MenuItem green;

    @FXML
    private TextField newTaskField;

    @FXML
    private MenuItem orange;

    @FXML
    private MenuButton priorityMenu;

    @FXML
    private ImageView priorityView;

    @FXML
    private MenuItem red;

    @FXML
    private ListView<HBox> taskList;

    @FXML
    private MenuItem yellow;


    @FXML
    public void initialize() throws SQLException {

        loadContent();

        addButton.setOnAction(event -> {
            try {
                handleAddClick();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #234232; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: transparent; -fx-border-color: #234232; -fx-border-radius: 10; -fx-border-width: 1px; -fx-text-fill: #234232;"));

        taskList.setOnMouseClicked(event -> handleTaskListSelection());
        taskList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setInvisibleCompletedButtons());

        red.setOnAction(event -> handlePriorityView(red));
        orange.setOnAction(event -> handlePriorityView(orange));
        yellow.setOnAction(event -> handlePriorityView(yellow));
        green.setOnAction(event -> handlePriorityView(green));

    }

    private void handleAddClick() throws SQLException {
        String newTask = newTaskField.getText();
        if(!newTask.isEmpty()){
            Task task = new Task();
            task.setAuthor(MainPageController.getAuthor());
            task.setContent(newTask);
            task.setCompleted(0);
            task.setPriorityPath(priorityView.getImage().getUrl());
            System.out.println(priorityView.getImage().getUrl());
            Pattern pattern = Pattern.compile("Resourcees/(.*?)\\.png");
            Matcher matcher = pattern.matcher(priorityView.getImage().getUrl());
            if(matcher.find()){
                task.setPriorityPath(matcher.group(0));
                System.out.println(matcher.group(0));
            }

            TaskDao taskDao = new TaskDao();
            taskDao.saveTask(task);

            addNewTask(newTask);
            newTaskField.clear();
        }
    }

    private void addNewTask(String newTask) {
        HBox taskBox = new HBox();
        Label taskLabel = new Label(newTask);
        Button completedButton = new Button("Completed");
        Button deleteButton = new Button();

        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setSpacing(5);
        taskBox.setPadding(new Insets(0, 10, 0, 10));

        taskLabel.setPrefSize(290, 17);

        completedButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 10px; -fx-border-radius: 10");
        completedButton.setPrefSize(62, 22);
        completedButton.setCursor(Cursor.HAND);
        completedButton.setVisible(false);

        ImageView minusCircle = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/minus.circle.png"))));
        ImageView minusCircleFill = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/minus.circle.fill.png"))));
        deleteButton.setGraphic(minusCircle);
        deleteButton.setStyle("-fx-background-color: transparent");
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setVisible(false);

        completedButton.setOnMouseEntered(e -> completedButton.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 10px"));
        completedButton.setOnMouseExited(e -> completedButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 10px; -fx-border-radius: 10"));
        deleteButton.setOnMouseEntered(e -> deleteButton.setGraphic(minusCircleFill));
        deleteButton.setOnMouseExited(e -> deleteButton.setGraphic(minusCircle));

        taskBox.getChildren().addAll(taskLabel, completedButton, deleteButton);
        taskList.getItems().add(taskBox);
    }

    private void handleTaskListSelection() {
        HBox selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Label taskLabel = (Label) selectedTask.getChildren().getFirst();
            Button completedButton = (Button) selectedTask.getChildren().get(1);
            Button deleteButton = (Button) selectedTask.getChildren().get(2);
            completedButton.setVisible(true);
            deleteButton.setVisible(true);
            completedButton.setOnAction(event -> {
                try {
                    handleTaskListCompletedButton(taskLabel.getText());
                    taskList.getItems().remove(selectedTask);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            deleteButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Task");
                alert.setHeaderText("Are you sure you want to delete this task?");

                ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.NO);
                ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.YES);
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {
                    try {
                        handleTaskListDeleteButton(taskLabel.getText());
                        taskList.getItems().remove(selectedTask);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private void setInvisibleCompletedButtons() {
        for (HBox hBox : taskList.getItems()) {
            Button completedButton = (Button) hBox.getChildren().get(1);
            Button deleteButton = (Button) hBox.getChildren().get(2);
            completedButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    private void handleTaskListCompletedButton(String content) throws SQLException {
        TaskDao taskDao = new TaskDao();
        taskDao.completeTask(MainPageController.getAuthor(), content);
    }

    private void handleTaskListDeleteButton(String content) throws SQLException {
        TaskDao taskDao = new TaskDao();
        taskDao.deleteTask(MainPageController.getAuthor(), content);
    }

    private void loadContent() throws SQLException {
        ArrayList<Task> tasks;
        TaskDao taskDao = new TaskDao();
        tasks = taskDao.getAllTask(MainPageController.getAuthor());
        for (Task task : tasks) {
            if (task.getCompleted() != 1)
                addNewTask(task.getContent());
        }
    }

    private void handlePriorityView(MenuItem menuItem) {
        String id = menuItem.getId();
        priorityView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/" + id + ".circle.fill.priority.view.png"))));
    }
}
