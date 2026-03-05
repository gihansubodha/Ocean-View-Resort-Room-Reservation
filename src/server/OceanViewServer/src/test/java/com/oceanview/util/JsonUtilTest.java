package com.oceanview.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonUtilTest {

    @Test
    public void escape_returnsEmptyString_forNullInput() {
        assertEquals("", JsonUtil.escape(null));
    }

    @Test
    public void escape_escapesBackslashesAndQuotes() {
        assertEquals("A\\\\B\\\"C", JsonUtil.escape("A\\B\"C"));
    }
}
