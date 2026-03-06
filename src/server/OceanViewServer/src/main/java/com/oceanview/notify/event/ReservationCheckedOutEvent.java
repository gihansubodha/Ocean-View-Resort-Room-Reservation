package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationCheckedOutEvent extends ReservationEvent {

    public ReservationCheckedOutEvent(Reservation reservation, String guestEmail) {
        super(reservation, guestEmail);
    }
}