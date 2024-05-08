package dao;

import model.Task;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public ArrayList<Task> getAllTask(String author) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM tasks WHERE author = ?");
        ps.setString(1, author);
        ResultSet rs = ps.executeQuery();

        EmployeeDao empDao = new EmployeeDao();
        ArrayList<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            Task t = new Task();
            t.setId(rs.getInt("id"));
            t.setAuthor(rs.getString("author"));
            t.setContent(rs.getString("content"));
            t.setCompleted(rs.getInt("completed"));
            t.setPriorityPath(rs.getString("priority_path"));
            t.setPriorityLevel(rs.getInt("priority_level"));
            t.setAssignee(rs.getString("assignee"));
            t.setAssigneePhotoPath(empDao.getAssigneePhotoPath(rs.getString("assignee")));
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
        PreparedStatement ps = conn.prepareStatement("UPDATE tasks SET completed = ?, completed_date = ? WHERE author = ? AND content = ?");
        ps.setInt(1, 1);
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        ps.setString(3, author);
        ps.setString(4, content);
        ps.executeUpdate();
    }

    public void undoComplete(String author, String content) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE tasks SET completed = ?, undo_completed_date = ? WHERE author = ? AND content = ?");
        ps.setInt(1, 0);
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        ps.setString(3, author);
        ps.setString(4, content);
        ps.executeUpdate();
    }

    public void deleteTask(String author, String content) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM tasks WHERE author = ? AND content = ?");
        ps.setString(1, author);
        ps.setString(2, content);
        ps.executeUpdate();
    }

    public void updateAssignee(Task task) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE tasks SET assignee = ? WHERE author = ? AND content = ?");
        ps.setString(1, task.getAssignee());
        ps.setString(2, task.getAuthor());
        ps.setString(3, task.getContent());
        ps.executeUpdate();
    }

    public ArrayList<String> getAllContents(String author) throws SQLException {
        ArrayList<String> contents = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT content FROM tasks WHERE author = ?");
        ps.setString(1, author);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String content = rs.getString("content");
            contents.add(content);
        }
        return contents;
    }

}
