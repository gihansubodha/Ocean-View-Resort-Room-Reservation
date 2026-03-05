package com.oceanview.notify.event;

import com.oceanview.notify.listener.ReservationEmailListener;

public class ReservationEventBus {

    private final ReservationEmailListener emailListener;

    public ReservationEventBus(ReservationEmailListener emailListener) {
        this.emailListener = emailListener;
    }

    public void publish(ReservationConfirmedEvent event) throws Exception {
        emailListener.onConfirmed(event);
    }
}