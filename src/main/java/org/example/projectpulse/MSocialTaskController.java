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

    private String currentPriorityViewUrl;


    @FXML
    public void initialize() throws SQLException {

        currentPriorityViewUrl = "Resources/green.circle.fill.priority.view.png";
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

    private void loadContent() throws SQLException {
        ArrayList<Task> tasks;
        TaskDao taskDao = new TaskDao();
        tasks = taskDao.getAllTask(MainPageController.getAuthor());
        for (Task task : tasks) {
            if (task.getCompleted() != 1)
                addNewTask(task);

        }
        sortTaskListToPriorityLevel();
    }

    private void addNewTask(Task task) {
        HBox taskBox = new HBox();
        Label taskLabel = new Label(task.getContent());
        Button completedButton = new Button("Completed");
        Button deleteButton = new Button();
        Button tagButton = new Button();
        Label priorityLabel = new Label("" + task.getPriorityLevel());

        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setSpacing(5);
        taskBox.setPadding(new Insets(0, 0, 0, 10));

        taskLabel.setPrefSize(255, 17);

        priorityLabel.setPrefSize(1, 1);
        priorityLabel.setMinSize(1, 1);
        priorityLabel.setVisible(false);

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

        ImageView tag = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(task.getPriorityPath()))));
        tag.setFitWidth(15);
        tag.setFitHeight(15);
        tagButton.setGraphic(tag);
        tagButton.setStyle("-fx-background-color: transparent");

        completedButton.setOnMouseEntered(e -> completedButton.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 10px"));
        completedButton.setOnMouseExited(e -> completedButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 10px; -fx-border-radius: 10"));
        deleteButton.setOnMouseEntered(e -> deleteButton.setGraphic(minusCircleFill));
        deleteButton.setOnMouseExited(e -> deleteButton.setGraphic(minusCircle));

        taskBox.getChildren().addAll(tagButton, taskLabel, completedButton, deleteButton, priorityLabel);
        taskList.getItems().add(taskBox);
    }

    private void sortTaskListToPriorityLevel() {
        ArrayList<HBox> sortedBoxes = new ArrayList<>(taskList.getItems());

        sortedBoxes.sort((box1, box2) -> {
            Label label1 = (Label) box1.getChildren().getLast();
            Label label2 = (Label) box2.getChildren().getLast();
            int priorityLevel1 = Integer.parseInt(label1.getText());
            int priorityLevel2 = Integer.parseInt(label2.getText());
            return Integer.compare(priorityLevel1, priorityLevel2);
        });

        taskList.getItems().setAll(sortedBoxes);
    }

    private void setInvisibleCompletedButtons() {
        for (HBox hBox : taskList.getItems()) {
            Button completedButton = (Button) hBox.getChildren().get(2);
            Button deleteButton = (Button) hBox.getChildren().get(3);
            completedButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    private void handleAddClick() throws SQLException {
        String newTask = newTaskField.getText();
        if(!newTask.isEmpty()){
            Task task = new Task();
            task.setAuthor(MainPageController.getAuthor());
            task.setContent(newTask);
            task.setCompleted(0);
            task.setPriorityPath(currentPriorityViewUrl);
            if (currentPriorityViewUrl.equals("Resources/red.circle.fill.priority.view.png"))
                task.setPriorityLevel(1);
            if (currentPriorityViewUrl.equals("Resources/orange.circle.fill.priority.view.png"))
                task.setPriorityLevel(2);
            if (currentPriorityViewUrl.equals("Resources/yellow.circle.fill.priority.view.png"))
                task.setPriorityLevel(3);
            if (currentPriorityViewUrl.equals("Resources/green.circle.fill.priority.view.png"))
                task.setPriorityLevel(4);

            TaskDao taskDao = new TaskDao();
            taskDao.saveTask(task);

            addNewTask(task);
            sortTaskListToPriorityLevel();
            newTaskField.clear();
        }
    }

    private void handleTaskListSelection() {
        HBox selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Label taskLabel = (Label) selectedTask.getChildren().get(1);
            Button completedButton = (Button) selectedTask.getChildren().get(2);
            Button deleteButton = (Button) selectedTask.getChildren().get(3);
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
                try {
                    handleTaskListDeleteButton(taskLabel.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                taskList.getItems().remove(selectedTask);

            });
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

    private void handlePriorityView(MenuItem menuItem) {
        String id = menuItem.getId();
        currentPriorityViewUrl = "Resources/" + id + ".circle.fill.priority.view.png";
        priorityView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/" + id + ".circle.fill.priority.view.png"))));
    }
}
