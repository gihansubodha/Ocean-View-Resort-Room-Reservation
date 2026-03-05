package com.oceanview.notify.sender;

public interface EmailSender {
    void send(String to, String subject, String htmlBody) throws Exception;
}