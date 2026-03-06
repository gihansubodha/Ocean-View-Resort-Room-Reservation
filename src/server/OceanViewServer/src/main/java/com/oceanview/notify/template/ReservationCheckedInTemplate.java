package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public class ReservationCheckedInTemplate implements ReservationEmailTemplate {

    @Override
    public String subject(Reservation r) {
        return "OceanView - Checked In [" + r.getReservationCode() + "]";
    }

    @Override
    public String bodyHtml(Reservation r) {
        return String.format(
                "<div style='font-family:Arial;line-height:1.5'>"
                        + "<h2>Check-in Successful</h2>"
                        + "<p>Status updated to <b>CHECKED_IN</b>.</p>"
                        + "<p><b>Reservation Code:</b> %s</p>"
                        + "<p>Enjoy your stay at OceanView!</p>"
                        + "<hr/>"
                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                        + "</div>",
                r.getReservationCode()
        );
    }
}