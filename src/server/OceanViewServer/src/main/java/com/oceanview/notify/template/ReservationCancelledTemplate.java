package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public class ReservationCancelledTemplate implements ReservationEmailTemplate {

    @Override
    public String subject(Reservation r) {
        return "OceanView - Reservation Cancelled [" + r.getReservationCode() + "]";
    }

    @Override
    public String bodyHtml(Reservation r) {
        return String.format(
                "<div style='font-family:Arial;line-height:1.5'>"
                        + "<h2>Reservation Cancelled</h2>"
                        + "<p>Your reservation has been <b>CANCELLED</b>.</p>"
                        + "<p><b>Reservation Code:</b> %s</p>"
                        + "<p>If this was a mistake, please contact reception.</p>"
                        + "<hr/>"
                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                        + "</div>",
                r.getReservationCode()
        );
    }
}