package com.ap.model;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Scanner;

public class AdminController {

    public static void addNewUser(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.print("Set PIN: ");
        String pin = sc.nextLine().trim();
        System.out.print("Role (ADMIN/CUSTOMER): ");
        String role = sc.nextLine().trim().toUpperCase();

        if (!role.equals("ADMIN") && !role.equals("CUSTOMER")) {
            System.out.println("Invalid role.");
            return;
        }

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        String hashed = com.ap.model.AuthService.hashPin(pin, salt);  

        PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password_hash, salt, role) VALUES (?, ?, ?, ?)");
        ps.setString(1, username);
        ps.setString(2, hashed);
        ps.setBytes(3, salt);
        ps.setString(4, role);
        ps.executeUpdate();

        if (role.equals("CUSTOMER")) {
            PreparedStatement ps2 = con.prepareStatement("INSERT INTO accounts(name, balance, status) VALUES (?, 0, 'active')");
            ps2.setString(1, username);
            ps2.executeUpdate();
        }

        con.commit();
        System.out.println("User added successfully.");
    }

    public static void deleteUser(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter username to delete: ");
        String user = sc.nextLine().trim();

        int accId = CustomerController.getAccountId(con, user);
        if (accId == -1) {
            System.out.println("User not found.");
            return;
        }

        con.prepareStatement("DELETE FROM transaction WHERE account_id = " + accId).executeUpdate();
        con.prepareStatement("DELETE FROM accounts WHERE id = " + accId).executeUpdate();
        con.prepareStatement("DELETE FROM users WHERE username = '" + user + "'").executeUpdate();

        con.commit();
        System.out.println("User and data deleted.");
    }

    public static void viewUserTransactions(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter username: ");
        String user = sc.nextLine().trim();
        int accId = CustomerController.getAccountId(con, user);
        if (accId == -1) {
            System.out.println("Account not found.");
            return;
        }

        ResultSet rs = con.prepareStatement("SELECT * FROM transaction WHERE account_id = " + accId + " ORDER BY timestamp DESC").executeQuery();
        while (rs.next()) {
            System.out.printf("TxnID: %d | Type: %s | ₹%.2f | On: %s\n",
                rs.getInt("id"), rs.getString("type"),
                rs.getDouble("amount"), rs.getTimestamp("timestamp"));
        }
    }

    public static void viewAllAccounts(Connection con) throws SQLException {
        ResultSet rs = con.prepareStatement("SELECT * FROM accounts").executeQuery();
        while (rs.next()) {
            System.out.printf("ID: %d | %s | ₹%.2f | %s\n",
                rs.getInt("id"), rs.getString("name"),
                rs.getDouble("balance"), rs.getString("status"));
        }
    }

    public static void viewAllTransactions(Connection con) throws SQLException {
        ResultSet rs = con.prepareStatement("SELECT * FROM transaction ORDER BY timestamp DESC").executeQuery();
        while (rs.next()) {
            System.out.printf("TxnID: %d | AccID: %d | %s | ₹%.2f | %s\n",
                rs.getInt("id"), rs.getInt("account_id"),
                rs.getString("type"), rs.getDouble("amount"),
                rs.getTimestamp("timestamp"));
        }
    }
}
