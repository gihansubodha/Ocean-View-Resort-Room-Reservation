package com.oceanview.util;

public final class JsonUtil {
    private JsonUtil() {}

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
