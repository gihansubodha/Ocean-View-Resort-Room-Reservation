package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationCreatedEvent extends ReservationEvent {

    public ReservationCreatedEvent(Reservation reservation, String guestEmail) {
        super(reservation, guestEmail);
    }
}