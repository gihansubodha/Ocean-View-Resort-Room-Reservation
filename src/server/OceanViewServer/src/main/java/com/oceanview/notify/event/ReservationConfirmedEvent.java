package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationConfirmedEvent {
    private final Reservation reservation;
    private final String guestEmail;

    public ReservationConfirmedEvent(Reservation reservation, String guestEmail) {
        this.reservation = reservation;
        this.guestEmail = guestEmail;
    }

    public Reservation getReservation() { return reservation; }
    public String getGuestEmail() { return guestEmail; }
}