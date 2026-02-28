package com.oceanview.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class PasswordUtil {

    private static final SecureRandom RNG = new SecureRandom();

    private PasswordUtil() {}

    /** Returns random salt as hex string (16 bytes => 32 hex chars). */
    public static String generateSaltHex() {
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        return toHex(salt);
    }

    /** Returns SHA-256(saltHex + password) as hex string. */
    public static String sha256Hex(String saltHex, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = saltHex + (password == null ? "" : password);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    /** Create stored value: salt:hash */
    public static String createStoredPassword(String plainPassword) {
        String salt = generateSaltHex();
        String hash = sha256Hex(salt, plainPassword);
        return salt + ":" + hash;
    }

    /** Verify stored value salt:hash */
    public static boolean verifyPassword(String plainPassword, String storedSaltColonHash) {
        if (storedSaltColonHash == null) return false;
        String[] parts = storedSaltColonHash.split(":");
        if (parts.length != 2) return false;

        String salt = parts[0];
        String expectedHash = parts[1];
        String actualHash = sha256Hex(salt, plainPassword);

        // constant-time compare (simple)
        return slowEquals(expectedHash, actualHash);
    }

    private static boolean slowEquals(String a, String b) {
        if (a == null || b == null) return false;
        int diff = a.length() ^ b.length();
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte x : bytes) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}
