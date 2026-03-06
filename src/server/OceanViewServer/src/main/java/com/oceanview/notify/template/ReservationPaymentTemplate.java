package com.oceanview.notify.template;

import com.oceanview.model.Reservation;

public class ReservationPaymentTemplate implements ReservationEmailTemplate {

    private final double amount;
    private final String method;
    private final String paidTotal;
    private final String balance;

    public ReservationPaymentTemplate(double amount, String method, String paidTotal, String balance) {
        this.amount = amount;
        this.method = method == null ? "" : method;
        this.paidTotal = paidTotal == null ? "0.00" : paidTotal;
        this.balance = balance == null ? "0.00" : balance;
    }

    @Override
    public String subject(Reservation r) {
        return "OceanView - Payment Received [" + r.getReservationCode() + "]";
    }

    @Override
    public String bodyHtml(Reservation r) {
        return String.format(
                "<div style='font-family:Arial;line-height:1.5'>"
                        + "<h2>Payment Received</h2>"
                        + "<p>We received your payment.</p>"
                        + "<p><b>Reservation Code:</b> %s</p>"
                        + "<p><b>Payment Amount:</b> %.2f<br/>"
                        + "<b>Method:</b> %s</p>"
                        + "<p><b>Total Paid:</b> %s<br/>"
                        + "<b>Balance:</b> %s</p>"
                        + "<hr/>"
                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                        + "</div>",
                r.getReservationCode(),
                amount,
                method,
                paidTotal,
                balance
        );
    }
}