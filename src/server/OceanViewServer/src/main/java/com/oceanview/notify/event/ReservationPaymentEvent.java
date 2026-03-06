package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationPaymentEvent extends ReservationEvent {

    private final double amount;
    private final String method;
    private final String paidTotal;
    private final String balance;

    public ReservationPaymentEvent(
            Reservation reservation,
            String guestEmail,
            double amount,
            String method,
            String paidTotal,
            String balance
    ) {
        super(reservation, guestEmail);
        this.amount = amount;
        this.method = method;
        this.paidTotal = paidTotal;
        this.balance = balance;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getPaidTotal() {
        return paidTotal;
    }

    public String getBalance() {
        return balance;
    }
}