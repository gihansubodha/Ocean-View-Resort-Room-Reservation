package com.oceanview.notify.config;

public class GmailConfig {
    private final String username;
    private final String appPassword;

    public GmailConfig(String username, String appPassword) {
        this.username = username;
        this.appPassword = appPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getAppPassword() {
        return appPassword;
    }
}