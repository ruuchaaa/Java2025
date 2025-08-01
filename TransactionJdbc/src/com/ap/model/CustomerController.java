package com.ap.model;

import com.ap.model.TransactionLogger;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerController {

    public static void checkBalance(Connection con, String username) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE name = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Current Balance: ₹" + rs.getDouble(1));
        } else {
            System.out.println("Account not found.");
        }
        ps.close();
    }

    public static void deposit(Connection con, Scanner sc, String username) throws SQLException {
        System.out.print("Enter amount to deposit: ₹");
        double amt = sc.nextDouble();
        sc.nextLine(); // consume newline
        PreparedStatement ps = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE name = ?");
        ps.setDouble(1, amt);
        ps.setString(2, username);
        int rows = ps.executeUpdate();
        if (rows == 1) {
            int accId = getAccountId(con, username);
            TransactionLogger.logTransaction(con, accId, "credit", amt, "Deposit");
            con.commit();
            System.out.println("Amount deposited.");
        } else {
            con.rollback();
            System.out.println("Deposit failed.");
        }
        ps.close();
    }

    public static void withdraw(Connection con, Scanner sc, String username) throws SQLException {
        System.out.print("Enter amount to withdraw: ₹");
        double amt = sc.nextDouble();
        sc.nextLine();
        PreparedStatement check = con.prepareStatement("SELECT balance FROM accounts WHERE name = ?");
        check.setString(1, username);
        ResultSet rs = check.executeQuery();
        if (rs.next() && rs.getDouble(1) >= amt) {
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE name = ?");
            ps.setDouble(1, amt);
            ps.setString(2, username);
            ps.executeUpdate();
            TransactionLogger.logTransaction(con, getAccountId(con, username), "debit", amt, "Withdrawal");
            con.commit();
            System.out.println("Amount withdrawn.");
            ps.close();
        } else {
            System.out.println("Insufficient balance.");
            con.rollback();
        }
        check.close();
    }

    public static void viewStatement(Connection con, String username) throws SQLException {
        int id = getAccountId(con, username);
        PreparedStatement ps = con.prepareStatement("SELECT * FROM transaction WHERE account_id = ? ORDER BY timestamp DESC");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.printf("%d | %s | ₹%.2f | %s\n",
                rs.getInt("id"), rs.getString("type"),
                rs.getDouble("amount"), rs.getTimestamp("timestamp"));
        }
        ps.close();
    }

    public static void transferMoney(Connection con, Scanner sc, String fromUser) {
        try {
            PreparedStatement senderStmt = con.prepareStatement("SELECT id, balance, status FROM accounts WHERE name = ?");
            senderStmt.setString(1, fromUser);
            ResultSet senderRs = senderStmt.executeQuery();

            if (!senderRs.next()) {
                System.out.println("Sender account not found.");
                return;
            }

            int fromId = senderRs.getInt("id");
            double senderBalance = senderRs.getDouble("balance");
            String senderStatus = senderRs.getString("status");

            if (!"active".equalsIgnoreCase(senderStatus)) {
                System.out.println("Your account is inactive. Cannot transfer.");
                return;
            }

            System.out.print("Enter recipient username: ");
            String toUser = sc.nextLine().trim();

            if (toUser.equalsIgnoreCase(fromUser)) {
                System.out.println("Cannot transfer to your own account.");
                return;
            }

            PreparedStatement receiverStmt = con.prepareStatement("SELECT id, status FROM accounts WHERE name = ?");
            receiverStmt.setString(1, toUser);
            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next()) {
                System.out.println("Recipient account not found.");
                return;
            }

            int toId = receiverRs.getInt("id");
            String recipientStatus = receiverRs.getString("status");

            if (!"active".equalsIgnoreCase(recipientStatus)) {
                System.out.println("Recipient account is inactive.");
                return;
            }

            System.out.print("Enter amount to transfer: ₹");
            double amount;
            try {
                amount = sc.nextDouble();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid amount.");
                sc.nextLine();
                return;
            }

            if (amount <= 0 || senderBalance < amount) {
                System.out.println("Invalid amount or insufficient funds.");
                return;
            }

            con.setAutoCommit(false);

            PreparedStatement debitStmt = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, fromId);

            PreparedStatement creditStmt = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, toId);

            int rows1 = debitStmt.executeUpdate();
            int rows2 = creditStmt.executeUpdate();

            if (rows1 == 1 && rows2 == 1) {
                TransactionLogger.logTransaction(con, fromId, "transfer", amount, "Sent to " + toUser);
                TransactionLogger.logTransaction(con, toId, "credit", amount, "Received from " + fromUser);
                con.commit();
                System.out.println("✅ ₹" + amount + " transferred successfully to " + toUser);
            } else {
                con.rollback();
                System.out.println("❌ Transfer failed.");
            }

            debitStmt.close();
            creditStmt.close();
            senderStmt.close();
            receiverStmt.close();
            con.setAutoCommit(true);

        } catch (Exception e) {
            rollback(con, "❌ Transfer failed: " + e.getMessage());
        }
    }

    public static int getAccountId(Connection con, String username) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
        return -1;
    }

    private static void rollback(Connection con, String message) {
        try {
            con.rollback();
            System.out.println(message);
        } catch (SQLException e) {
            System.out.println("Rollback failed: " + e.getMessage());
        }
    }
}
