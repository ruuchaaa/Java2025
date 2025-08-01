package com.ap.model;

import com.ap.model.AdminController;
import com.ap.model.CustomerController;

import java.sql.Connection;
import java.util.Scanner;

public class MenuService {

    public static void adminMenu(Connection con, Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add New User");
            System.out.println("2. Delete User");
            System.out.println("3. View All Accounts");
            System.out.println("4. View All Transactions");
            System.out.println("5. View User Transactions");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> AdminController.addNewUser(con, sc);
                    case "2" -> AdminController.deleteUser(con, sc);
                    case "3" -> AdminController.viewAllAccounts(con);
                    case "4" -> AdminController.viewAllTransactions(con);
                    case "5" -> AdminController.viewUserTransactions(con, sc);
                    case "6" -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
            }
        }
    }

    public static void customerMenu(Connection con, Scanner sc, String username) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> CustomerController.checkBalance(con, username);
                    case "2" -> CustomerController.deposit(con, sc, username);
                    case "3" -> CustomerController.withdraw(con, sc, username);
                    case "4" -> CustomerController.transferMoney(con, sc, username);
                    case "5" -> CustomerController.viewStatement(con, username);
                    case "6" -> {
                        System.out.println(" Logout successful.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
            }
        }
    }
}
