package com.oceanview.notify.event;

import com.oceanview.model.Reservation;

public class ReservationCheckedInEvent extends ReservationEvent {

    public ReservationCheckedInEvent(Reservation reservation, String guestEmail) {
        super(reservation, guestEmail);
    }
}