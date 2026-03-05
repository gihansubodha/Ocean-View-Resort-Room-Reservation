package com.oceanview.util;

import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.GmailSmtpEmailSender;

public class GmailSmokeTest {
    public static void main(String[] args) throws Exception {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        System.out.println("TEST USER=" + user);
        System.out.println("TEST PASS LEN=" + (pass == null ? 0 : pass.length()));

        GmailConfig cfg = new GmailConfig(user, pass);
        GmailSmtpEmailSender sender = new GmailSmtpEmailSender(cfg);

        sender.send(
                user,
                "OceanView SMTP Test",
                "<h2>SMTP Test OK</h2><p>If you receive this, Gmail SMTP is working.</p>"
        );

        System.out.println("MAIL SENT OK");
    }
}