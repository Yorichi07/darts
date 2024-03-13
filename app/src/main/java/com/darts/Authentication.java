package com.darts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {
    private static final String url = "jdbc:mysql://localhost:3306/medical_records?characterEncoding=utf8";
    private static final String SQLusername = "root";
    private static final String SQLpassword = "1234";

    public void getHash(Scanner scanner) {
        System.out.println("Enter UserId: ");
        int UID = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after nextInt()

        encryptPasswordAndSave(UID);

        System.out.println("Enter Username: ");
        String username = scanner.nextLine();

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        if (checkAuthentication(username, password)) {
            System.out.println("Authentication successful for user: " + username);
        } else {
            System.out.println("Authentication failed for user: " + username);
        }
    }

    private static void encryptPasswordAndSave(int UID) {
        try (Connection conn = DriverManager.getConnection(url, SQLusername, SQLpassword)) {
            String query = "SELECT username,password FROM users WHERE UID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, UID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        String password = rs.getString("password");

                        // Encrypting the password using MD5
                        String encryptedPassword = encryptPasswordMD5(password);

                        // Save encrypted password
                        saveEncryptedPassword(username, encryptedPassword);
                    } else {
                        System.out.println("User not found for UID: " + UID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String encryptPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveEncryptedPassword(String username, String encryptedPassword) {
        try (Connection conn = DriverManager.getConnection(url, SQLusername, SQLpassword)) {
            String query = "UPDATE users SET password=? WHERE username=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, encryptedPassword);
                pstmt.setString(2, username);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Encrypted password for the user " + username + " is saved successfully.");
                } else {
                    System.out.println("Failed to save encrypted password for the user " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkAuthentication(String username, String password) {
        String encryptedPassword = encryptPasswordMD5(password);
        try (Connection conn = DriverManager.getConnection(url, SQLusername, SQLpassword)) {
            String query = "SELECT password FROM users WHERE username=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");

                        // Checking if storedPassword matches user input password
                        return encryptedPassword.equals(storedPassword);
                    } else {
                        System.out.println("User not found: " + username);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
