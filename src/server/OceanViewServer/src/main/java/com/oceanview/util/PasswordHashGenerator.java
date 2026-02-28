package com.oceanview.util;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        String plain = "admin123";; // change this
        String stored = PasswordUtil.createStoredPassword(plain);
        System.out.println("Stored password_hash value:");
        System.out.println(stored);
    }
}
