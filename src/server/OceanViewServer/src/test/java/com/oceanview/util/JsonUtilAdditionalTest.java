package com.oceanview.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilAdditionalTest {
    @Test
    public void escape_returnsEmptyString_whenNull() {
        assertEquals("", JsonUtil.escape(null));
    }

    @Test
    public void escape_escapesBackslashesAndQuotesTogether() {
        String input = "\\path\\\"name\"";
        String expected = "\\\\path\\\\\\\"name\\\"";
        assertEquals(expected, JsonUtil.escape(input));
    }
}
