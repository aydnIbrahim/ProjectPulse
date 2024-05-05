package dao;

import model.Task;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskDao {

    public ArrayList<Task> getAllTask(String author) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT content, completed FROM tasks WHERE author = ?");
        ps.setString(1, author);
        ResultSet rs = ps.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            Task t = new Task();
            t.setAuthor(author);
            t.setContent(rs.getString("content"));
            t.setCompleted(rs.getInt("completed"));
            tasks.add(t);
        }
        return tasks;
    }

    public void saveTask(Task task) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO tasks (author, content, completed, priority_path, priority_level) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, task.getAuthor());
        ps.setString(2, task.getContent());
        ps.setInt(3, task.getCompleted());
        ps.setString(4, task.getPriorityPath());
        ps.setInt(5, task.getPriorityLevel());
        ps.executeUpdate();
    }

    public void completeTask(String author, String content) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE tasks SET completed = ? WHERE author = ? AND content = ?");
        ps.setInt(1, 1);
        ps.setString(2, author);
        ps.setString(3, content);
        ps.executeUpdate();
    }

    public void deleteTask(String author, String content) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM tasks WHERE author = ? AND content = ?");
        ps.setString(1, author);
        ps.setString(2, content);
        ps.executeUpdate();
    }

}
