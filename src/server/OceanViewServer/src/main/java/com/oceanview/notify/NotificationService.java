package com.oceanview.notify;

import com.oceanview.model.Reservation;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

public class NotificationService {

    private final EmailSender emailSender;

    public NotificationService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendReservationEmail(String guestEmail, Reservation r, ReservationEmailTemplate template) throws Exception {
        emailSender.send(guestEmail, template.subject(r), template.bodyHtml(r));
    }
}