package com.fo.dao;

import com.fo.util.DBConnection;
import java.sql.*;

public class UserDao {
    public String authenticate(String username, String password) {
        String role = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT role FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role; 
    }
}
