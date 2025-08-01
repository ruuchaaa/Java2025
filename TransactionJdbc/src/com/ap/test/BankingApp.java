package com.ap.test;

import com.ap.model.AuthService;
import com.ap.model.MenuService;
import com.ap.model.DBConnection;

import java.sql.Connection;
import java.util.Scanner;

public class BankingApp {

    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection(); Scanner sc = new Scanner(System.in)) {
            while (true) {
                String loginResult = AuthService.loginUser(con, sc);
                if (loginResult == null) {
                    System.out.println("Login failed. Try again.\n");
                    continue;
                }

                String[] parts = loginResult.split(":");
                String uname = parts[0];
                String role = parts[1];

                if (role.equalsIgnoreCase("ADMIN")) {
                    MenuService.adminMenu(con, sc);
                } else {
                    MenuService.customerMenu(con, sc, uname);
                }

                System.out.print("Do you want to log in again? (yes/no): ");
                String again = sc.nextLine().trim();
                if (!again.equalsIgnoreCase("yes")) break;
            }
        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}
