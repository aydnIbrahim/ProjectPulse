package dao;

import model.Employee;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDao {

    public ArrayList<Employee> getEmployees() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees");
        ResultSet rs = ps.executeQuery();
        ArrayList<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setNameSurname(rs.getString("name_surname"));
            employee.setProfilePhotoPath(rs.getString("profile_photo_path"));
            employee.setDepartmentName(rs.getString("department"));
            employees.add(employee);
        }
        return employees;
    }

    public String getAssigneePhotoPath(String nameSurname) throws SQLException {
        if (nameSurname != null) {
            nameSurname = nameSurname.toLowerCase();
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT profile_photo_path FROM employees WHERE name_surname = ?");
            ps.setString(1, nameSurname.toLowerCase());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString("profile_photo_path");
        }
        return null;
    }

}
