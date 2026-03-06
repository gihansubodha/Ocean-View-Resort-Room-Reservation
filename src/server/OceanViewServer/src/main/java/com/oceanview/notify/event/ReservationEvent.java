package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public abstract class ReservationEvent {

    private final Reservation reservation;
    private final String guestEmail;

    protected ReservationEvent(Reservation reservation, String guestEmail) {
        this.reservation = reservation;
        this.guestEmail = guestEmail;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getGuestEmail() {
        return guestEmail;
    }
}