package com.oceanview.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void generateSaltHex_returns32HexCharacters() {
        String salt = PasswordUtil.generateSaltHex();
        assertEquals(32, salt.length());
        assertTrue(salt.matches("[0-9a-f]+"));
    }

    @Test
    public void createStoredPassword_returnsSaltAndHashFormat() {
        String stored = PasswordUtil.createStoredPassword("secret");
        String[] parts = stored.split(":");
        assertEquals(2, parts.length);
        assertEquals(32, parts[0].length());
        assertEquals(64, parts[1].length());
    }

    @Test
    public void verifyPassword_returnsTrue_forCorrectPassword() {
        String stored = PasswordUtil.createStoredPassword("secret");
        assertTrue(PasswordUtil.verifyPassword("secret", stored));
    }

    @Test
    public void verifyPassword_returnsFalse_forWrongPassword() {
        String stored = PasswordUtil.createStoredPassword("secret");
        assertFalse(PasswordUtil.verifyPassword("wrong", stored));
    }

    @Test
    public void verifyPassword_returnsFalse_forMalformedStoredValue() {
        assertFalse(PasswordUtil.verifyPassword("secret", "bad-format"));
    }

    @Test
    public void verifyPassword_returnsFalse_forNullStoredValue() {
        assertFalse(PasswordUtil.verifyPassword("secret", null));
    }
}
