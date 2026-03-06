package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationCancelledEvent extends ReservationEvent {

    public ReservationCancelledEvent(Reservation reservation, String guestEmail) {
        super(reservation, guestEmail);
    }
}