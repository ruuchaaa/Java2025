package com.ap.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        con.setAutoCommit(false);
        return con;
    }
}
