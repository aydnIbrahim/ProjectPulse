package model;

public class Task {

    private int id;
    private String author;
    private String content;
    private int completed;
    private int priorityLevel;
    private String priorityPath;


    public Task() {}

    public Task(int id, String author, String content, int completed) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getPriorityPath() {
        return priorityPath;
    }

    public void setPriorityPath(String priorityPath) {
        this.priorityPath = priorityPath;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
}
