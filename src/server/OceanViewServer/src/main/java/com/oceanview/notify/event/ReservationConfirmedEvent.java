package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationConfirmedEvent extends ReservationEvent {

    public ReservationConfirmedEvent(Reservation reservation, String guestEmail) {
        super(reservation, guestEmail);
    }
}