package com.ap.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionLogger {

    public static void logTransaction(Connection con, int accId, String type, double amount, String description) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO transaction (account_id, type, amount, description) VALUES (?, ?, ?, ?)");
        ps.setInt(1, accId);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.setString(4, description);
        ps.executeUpdate();
        ps.close();
    }
}
