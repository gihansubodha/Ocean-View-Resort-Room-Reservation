package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public interface ReservationEmailTemplate {
    String subject(Reservation r);
    String bodyHtml(Reservation r);
}