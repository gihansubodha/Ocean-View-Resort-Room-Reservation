package com.oceanview.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationUtilAdditionalTest {
    @Test
    public void isBlank_returnsTrue_forEmptyString() { assertTrue(ValidationUtil.isBlank("")); }
    @Test
    public void isBlank_returnsTrue_forWhitespaceOnly() { assertTrue(ValidationUtil.isBlank("   	")); }
    @Test
    public void isBlank_returnsFalse_forNonBlank() { assertFalse(ValidationUtil.isBlank(" room ")); }
}
