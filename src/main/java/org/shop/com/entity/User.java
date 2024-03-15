package org.shop.com.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String username;
    private String passwordHash;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
    }

    public void edit(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }

    public void delete() {

    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Тесты для проверки работоспособности методов (пофиг можно удолить)
    public static void main(String[] args) {
        User user = new User("test_user", "bebebe");
        System.out.println("Original password hash: " + user.passwordHash);
        user.edit("new_password");
        System.out.println("New password hash: " + user.passwordHash);
        user.delete();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
