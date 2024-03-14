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
    private static final String SQLpassword = "root";

    public void getHash(Scanner scanner) {          //main function

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

    private static String encryptPasswordSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
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

    private static boolean checkAuthentication(String username, String password) {
        String encryptedPassword = encryptPasswordSHA256(password);
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
