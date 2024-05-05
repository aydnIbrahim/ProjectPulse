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
            t.setAuthor(rs.getString("author"));
            t.setContent(rs.getString("content"));
            t.setCompleted(rs.getInt("completed"));
            tasks.add(t);
        }
        return tasks;
    }

}
