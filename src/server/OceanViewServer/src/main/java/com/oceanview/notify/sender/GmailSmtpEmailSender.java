package com.oceanview.notify.sender;

import com.oceanview.notify.config.GmailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class GmailSmtpEmailSender implements EmailSender {

    private final GmailConfig config;

    public GmailSmtpEmailSender(GmailConfig config) {
        this.config = config;
    }

    @Override
    public void send(String to, String subject, String htmlBody) throws Exception {
        final String user = safe(config.getUsername());
        final String raw = config.getAppPassword();
        final String pass = normalizeAppPassword(raw);

        System.out.println("SMTP PASS rawLen=" + (raw == null ? 0 : raw.length())
                + " normLen=" + pass.length());

        if (user.isEmpty() || pass.isEmpty()) {
            throw new IllegalStateException("Gmail credentials missing (user/app password).");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(user));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject == null ? "" : subject);
        msg.setContent(htmlBody == null ? "" : htmlBody, "text/html; charset=UTF-8");

        Transport.send(msg);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private String normalizeAppPassword(String raw) {
        if (raw == null) return "";
        return raw
                .replace('\u00A0', ' ')
                .trim()
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", "");
    }
}