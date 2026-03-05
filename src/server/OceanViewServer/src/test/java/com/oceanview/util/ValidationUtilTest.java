package com.oceanview.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationUtilTest {

    @Test
    public void isBlank_returnsTrue_forNull() {
        assertTrue(ValidationUtil.isBlank(null));
    }

    @Test
    public void isBlank_returnsTrue_forWhitespaceOnly() {
        assertTrue(ValidationUtil.isBlank("   \t  "));
    }

    @Test
    public void isBlank_returnsFalse_forText() {
        assertFalse(ValidationUtil.isBlank("OceanView"));
    }
}
