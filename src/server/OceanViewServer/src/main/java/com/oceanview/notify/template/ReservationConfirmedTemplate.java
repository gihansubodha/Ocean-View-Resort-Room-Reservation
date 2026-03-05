package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public class ReservationConfirmedTemplate implements ReservationEmailTemplate {

    @Override
    public String subject(Reservation r) {
        return "OceanView - Reservation Confirmed (#" + r.getReservationId() + ")";
    }

    @Override
    public String bodyHtml(Reservation r) {
        return String.format(
                "<div style='font-family:Arial;line-height:1.5'>"
                        + "<h2>Reservation Created (Pending Confirmation)</h2>"
                        + "<p>Your reservation request has been created and is currently <b>PENDING</b>.</p>"
                        + "<p><b>Reservation Code:</b> %s</p>"
                        + "<p><b>Check-in:</b> %s<br/>"
                        + "<b>Check-out:</b> %s<br/>"
                        + "<b>Guests:</b> %d</p>"
                        + "<p>To confirm, please provide the required document details at the front desk / staff portal.</p>"
                        + "<hr/>"
                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                        + "</div>",
                r.getReservationCode(),
                String.valueOf(r.getCheckIn()),
                String.valueOf(r.getCheckOut()),
                r.getNumGuests()
        );
    }
}