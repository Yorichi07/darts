package com.darts;

import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class signUp {                           
    private static final String url = "jdbc:mysql://localhost:3306/medical_records?characterEncoding=utf8";
    private static final String SQLusername = "root";
    private static final String SQLpassword = "root";

    public void getRegister(Scanner scanner){          //main function

        System.out.println("Enter Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Username: ");
        String username = scanner.nextLine();

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        registerUser(name, username, password);

    }
    private static void registerUser(String name, String username, String password){
        String encryptedPassword=encryptPasswordSHA256(password);
        try(Connection conn=DriverManager.getConnection(url,SQLusername,SQLpassword)){
            String query="INSERT into users(name, username, password) VALUES(?,?,?)";
            try(PreparedStatement pstmt=conn.prepareStatement(query)){
                pstmt.setString(1,name);
                pstmt.setString(2,username);
                pstmt.setString(3,encryptedPassword);

                pstmt.executeUpdate();
                System.out.println("User registered successfully.");
            }
        }catch(SQLException e){
            e.printStackTrace();
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
}
