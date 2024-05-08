package org.example.projectpulse;

import dao.EmployeeDao;
import dao.TaskDao;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import model.Employee;
import model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksController {

    @FXML
    private Button addButton;

    @FXML
    private HBox addTaskHbox;

    @FXML
    private Button completedTasks;

    @FXML
    private TextField newTaskField;

    @FXML
    private ImageView priorityView;

    @FXML
    private ListView<HBox> taskList;

    @FXML
    private Button priorityButton;

    @FXML
    private Label alertLabel;

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

        completedTasks.setOnAction(event -> {
            taskList.getItems().clear();
            try {
                loadContent(completedTasks.getText());
                if (completedTasks.getText().equals("Completed Tasks")) {
                    completedTasks.setText("Ongoing Tasks");
                    addTaskHbox.setVisible(false);
                    addTaskHbox.setPrefSize(1000, 0);
                } else {
                    completedTasks.setText("Completed Tasks");
                    addTaskHbox.setVisible(true);
                    addTaskHbox.setPrefSize(1000, 90);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #234232; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: transparent; -fx-border-color: #234232; -fx-border-radius: 10; -fx-border-width: 1px; -fx-text-fill: #234232;"));
        completedTasks.setOnMouseEntered(e -> completedTasks.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white"));
        completedTasks.setOnMouseExited(e -> completedTasks.setStyle("-fx-background-color: transparent; -fx-text-fill: #0D2BC1; -fx-border-color: #0D2BC1; -fx-border-radius: 10"));

        taskList.setOnMouseClicked(event -> {
            try {
                handleTaskListSelection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        taskList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setInvisibleCompletedButtons());

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

    private void loadContent(String buttonContent) throws SQLException {
        ArrayList<Task> tasks;
        TaskDao taskDao = new TaskDao();
        tasks = taskDao.getAllTask(MainPageController.getAuthor());
        for (Task task : tasks) {
            if (buttonContent.equals("Completed Tasks") && task.getCompleted() != 0) {
                addNewTask(task);
            } else if (buttonContent.equals("Ongoing Tasks") && task.getCompleted() != 1) {
                addNewTask(task);
            }
        }

        sortTaskListToPriorityLevel();
    }

    public void addNewTask(Task task) {
        HBox taskBox = new HBox();

        Label taskLabel = new Label(task.getContent());
        Label priorityLabel = new Label("" + task.getPriorityLevel());
        Label line = new Label("|");
        Label assigneeLabel = new Label(Optional.ofNullable(task.getAssignee()).filter(s -> !s.isEmpty()).orElse("Idle Task"));

        Button completeButton = new Button();
        Button deleteButton = new Button();
        Button tagButton = new Button();
        Button assignButton = new Button();

        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setSpacing(5);
        taskBox.setPadding(new Insets(0, 10, 0, 0));

        taskLabel.setPrefSize(514, 17);
        taskLabel.setStyle("-fx-font-size: 14px");

        assigneeLabel.setPrefSize(175, 17);
        assigneeLabel.setStyle("-fx-font-size: 14px; font-weight: bold; text-fill: #234232");

        priorityLabel.setPrefSize(1, 1);
        priorityLabel.setMinSize(1, 1);
        priorityLabel.setVisible(false);

        completeButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; text-fill: #0D2BC1; -fx-font-size: 14px; -fx-border-radius: 12");
        completeButton.setPrefSize(100, 30);
        completeButton.setCursor(Cursor.HAND);
        completeButton.setVisible(false);

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
        assignButton.setGraphic(assign);
        assignButton.setStyle("-fx-background-color: transparent");

        completeButton.setOnMouseEntered(e -> completeButton.setStyle("-fx-background-color: #0D2BC1; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 14px"));
        completeButton.setOnMouseExited(e -> completeButton.setStyle("-fx-background-color: transparent; -fx-border-color: #0D2BC1; -fx-text-fill: #0D2BC1; -fx-font-size: 14px; -fx-border-radius: 10"));

        if (task.getCompleted() == 1) {
            completeButton.setText("Undo");
        } else {
            completeButton.setText("Completed");
            assignButton.setCursor(Cursor.HAND);
        }
        taskBox.getChildren().addAll(tagButton, taskLabel, line, assignButton, assigneeLabel, completeButton, deleteButton, priorityLabel);
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
            Button completedButton = (Button) hBox.getChildren().get(5);
            Button deleteButton = (Button) hBox.getChildren().get(6);
            completedButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    private void handleAddClick() throws SQLException {
        String newTask = newTaskField.getText();
        if (!newTask.isEmpty()) {
            TaskDao taskDao = new TaskDao();
            ArrayList<String> existingContents = taskDao.getAllContents(MainPageController.getAuthor());

            boolean taskExists = false;
            for (String content : existingContents) {
                if (content.equals(newTask)) {
                    taskExists = true;
                    break;
                }
            }

            if (!taskExists) {
                Task task = getTaskForHandleAddClick(newTask);

                taskDao.saveTask(task);

                addNewTask(task);
                sortTaskListToPriorityLevel();
                newTaskField.clear();
            } else {
                alertLabel.setText("Task already exists!");
                alertLabel.setStyle("-fx-text-fill: #FF2600; -fx-font-size: 18px");

                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(3),
                        event -> setDefaultAlertLabel()
                ));
                timeline.play();
            }
        }
    }

    private Task getTaskForHandleAddClick(String newTask) {
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
        task.setAssignee("Idle Task");
        return task;
    }

    private void setDefaultAlertLabel() {
        alertLabel.setText("Task");
        alertLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #234232");
    }

    private void handleTaskListSelection() throws SQLException {
        HBox selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Label taskLabel = (Label) selectedTask.getChildren().get(1);
            Label assigneeLabel = (Label) selectedTask.getChildren().get(4);

            Button completedButton = (Button) selectedTask.getChildren().get(5);
            Button deleteButton = (Button) selectedTask.getChildren().get(6);
            Button assignButton = (Button) selectedTask.getChildren().get(3);

            completedButton.setVisible(true);
            deleteButton.setVisible(true);
            completedButton.setOnAction(event -> {
                try {
                    handleTaskListCompletedButton(taskLabel.getText(), completedButton.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                taskList.getItems().remove(selectedTask);
            });
            deleteButton.setOnAction(event -> {
                try {
                    handleTaskListDeleteButton(taskLabel.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                taskList.getItems().remove(selectedTask);

            });
            if (completedTasks.getText().equals("Completed Tasks"))
                handleAssignButton(assignButton, taskLabel.getText(), assigneeLabel);
        }
    }

    private void handleTaskListCompletedButton(String content, String buttonContent) throws SQLException {
        TaskDao taskDao = new TaskDao();
        if (buttonContent.equals("Completed")) taskDao.completeTask(MainPageController.getAuthor(), content);
        else taskDao.undoComplete(MainPageController.getAuthor(), content);
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

    private void handleAssignButton(Button assignButton, String content, Label assigneeLabel) throws SQLException {
        ContextMenu contextMenu = new ContextMenu();

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        ArrayList<Employee> employees;
        EmployeeDao employeeDao = new EmployeeDao();

        employees = employeeDao.getEmployees();

        for (Employee employee : employees) {
            MenuItem menuItem = new MenuItem(Arrays.stream(employee.getNameSurname().split("\\s+"))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .collect(Collectors.joining(" ")));
            menuItems.add(menuItem);

            menuItem.setOnAction(actionEvent -> {
                TaskDao taskDao = new TaskDao();
                EmployeeDao empDao = new EmployeeDao();

                Task task = new Task();
                task.setAuthor(MainPageController.getAuthor());
                task.setContent(content);
                task.setAssignee(menuItem.getText());
                assigneeLabel.setText(menuItem.getText());

                ImageView assigneePhoto;
                try {
                    assigneePhoto = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(empDao.getAssigneePhotoPath(menuItem.getText())))));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                assigneePhoto.setFitHeight(30);
                assigneePhoto.setFitWidth(30);

                assignButton.setGraphic(assigneePhoto);

                try {
                    taskDao.updateAssignee(task);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        assignButton.setOnAction(actionEvent -> {
            if (!contextMenu.isShowing()) {
                for (MenuItem menuItem : menuItems) {
                    contextMenu.getItems().add(menuItem);
                    contextMenu.setStyle("-fx-background-color: white; -fx-background-radius: 10");
                }
                contextMenu.show(assignButton, Side.BOTTOM, 0, 0);
            } else contextMenu.hide();
        });
    }

    @FXML
    private void handlePriorityButton() {
        ContextMenu contextMenu = new ContextMenu();
        if(!contextMenu.isShowing()) {
            MenuItem red = new MenuItem();
            MenuItem orange = new MenuItem();
            MenuItem yellow = new MenuItem();
            MenuItem green = new MenuItem();

            red.setId("red");
            orange.setId("orange");
            yellow.setId("yellow");
            green.setId("green");

            red.setOnAction(event -> handlePriorityView(red));
            orange.setOnAction(event -> handlePriorityView(orange));
            yellow.setOnAction(event -> handlePriorityView(yellow));
            green.setOnAction(event -> handlePriorityView(green));

            ImageView redImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/red.circle.fill.priority.view.png"))));
            ImageView orangeImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/orange.circle.fill.priority.view.png"))));
            ImageView yellowImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/yellow.circle.fill.priority.view.png"))));
            ImageView greenImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Resources/green.circle.fill.priority.view.png"))));

            redImage.setFitWidth(30); redImage.setFitHeight(30);
            orangeImage.setFitWidth(30); orangeImage.setFitHeight(30);
            yellowImage.setFitWidth(30); yellowImage.setFitHeight(30);
            greenImage.setFitWidth(30); greenImage.setFitHeight(30);

            red.setGraphic(redImage);
            orange.setGraphic(orangeImage);
            yellow.setGraphic(yellowImage);
            green.setGraphic(greenImage);

            contextMenu.getItems().addAll(red, orange, yellow, green);
            contextMenu.setStyle("-fx-background-color: white; -fx-background-radius: 10");
            contextMenu.show(priorityButton, Side.BOTTOM, 0, 0);

        }
        else contextMenu.hide();
    }

}
