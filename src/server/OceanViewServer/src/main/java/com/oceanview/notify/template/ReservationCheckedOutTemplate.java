package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public class ReservationCheckedOutTemplate implements ReservationEmailTemplate {

    @Override
    public String subject(Reservation r) {
        return "OceanView - Checked Out [" + r.getReservationCode() + "]";
    }

    @Override
    public String bodyHtml(Reservation r) {
        return String.format(
                "<div style='font-family:Arial;line-height:1.5'>"
                        + "<h2>Check-out Completed</h2>"
                        + "<p>Status updated to <b>CHECKED_OUT</b>.</p>"
                        + "<p><b>Reservation Code:</b> %s</p>"
                        + "<p>Thank you for staying with OceanView.</p>"
                        + "<hr/>"
                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                        + "</div>",
                r.getReservationCode()
        );
    }
}