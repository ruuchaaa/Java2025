package com.ap.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Scanner;

public class AuthService {

    public static String loginUser(Connection con, Scanner sc) {
        System.out.print("Login (username): ");
        String username = sc.nextLine().trim();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine().trim();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT password_hash, salt, role FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                byte[] salt = rs.getBytes("salt");

                String inputHash = hashPin(pin, salt);
                if (storedHash.equals(inputHash)) {
                    System.out.println(" Login successful as " + rs.getString("role"));
                    return username + ":" + rs.getString("role");
                } else {
                    System.out.println(" Incorrect PIN.");
                }
            } else {
                System.out.println(" User not found.");
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null;
    }

    static public String hashPin(String pin, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashed = md.digest(pin.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashed);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
