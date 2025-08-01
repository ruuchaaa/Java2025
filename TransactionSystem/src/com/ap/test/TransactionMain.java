package com.ap.test;

import java.sql.Connection;
import java.util.Scanner;

import com.ap.model.AuthService;
import com.ap.model.DBConnection;
import com.ap.model.User;
import com.ap.model.UserDao;

public class TransactionMain {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            UserDao userDao = new UserDao(con);
            AccountDao accountDao = new AccountDao(con);
            TransactionDao transactionDao = new TransactionDao(con);
            AuthService authService = new AuthService(userDao);

            while (true) {
                System.out.println("\n--- Welcome to Secure Bank ---");
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("PIN: ");
                String pin = sc.nextLine();

                String authResult = authService.authenticate(username, pin);
                if (authResult == null) {
                    System.out.println("Invalid credentials.");
                    continue;
                }

                String[] split = authResult.split(":");
                String role = split[1];

                if (role.equalsIgnoreCase("admin")) {
                    handleAdmin(userDao);
                } else {
                    handleCustomer(accountDao, transactionDao, username);
                }

                System.out.print("\nDo you want to log in again? (yes/no): ");
                if (!sc.nextLine().equalsIgnoreCase("yes")) break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("System error: " + e.getMessage());
        }
    }

    private static void handleAdmin(UserDao userDao) throws Exception {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add New User");
            System.out.println("2. Delete User");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    System.out.print("Enter new username: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter PIN: ");
                    String pin = sc.nextLine();
                    System.out.print("Enter role (admin/customer): ");
                    String role = sc.nextLine();

                    byte[] salt = AuthService.generateSalt();
                    String hash = new AuthService(userDao).hashPin(pin, salt);

                    User user = new User();
                    user.setUsername(uname);
                    user.setPasswordHash(hash);
                    user.setSalt(salt);
                    user.setRole(role);

                    userDao.addUser(user);
                    System.out.println("User added.");
                    break;

                case "2":
                    System.out.print("Enter username to delete: ");
                    String delUser = sc.nextLine();
                    userDao.deleteUser(delUser);
                    System.out.println("User deleted.");
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleCustomer(AccountDao accountDao, TransactionDao transactionDao, String username) throws Exception {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            String ch = sc.nextLine();

            int accId = accountDao.getAccountId(username);

            switch (ch) {
                case "1":
                    double bal = accountDao.getBalance(username);
                    System.out.println("Balance: ₹" + bal);
                    break;

                case "2":
                    System.out.print("Enter deposit amount: ");
                    double dep = Double.parseDouble(sc.nextLine());
                    accountDao.updateBalance(accId, dep, true);
                    transactionDao.logTransaction(accId, "CREDIT", dep, "Deposit");
                    System.out.println("Deposited ₹" + dep);
                    break;

                case "3":
                    System.out.print("Enter withdrawal amount: ");
                    double with = Double.parseDouble(sc.nextLine());
                    double currentBal = accountDao.getBalance(username);
                    if (with > currentBal) {
                        System.out.println("Insufficient funds.");
                    } else {
                        accountDao.updateBalance(accId, with, false);
                        transactionDao.logTransaction(accId, "DEBIT", with, "Withdrawal");
                        System.out.println("Withdrawn ₹" + with);
                    }
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
