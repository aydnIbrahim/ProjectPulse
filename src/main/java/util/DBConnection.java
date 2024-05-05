package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private final Connection connection;

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/msocialdb";
            String username = "root";
            String password = "";
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database Connection Creation Failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null)
            instance = new DBConnection();
        else if (instance.getConnection().isClosed())
            instance = new DBConnection();

        return instance;
    }

}
