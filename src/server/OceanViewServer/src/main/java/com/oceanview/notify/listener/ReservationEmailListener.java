package com.oceanview.notify.listener;

import com.oceanview.notify.NotificationService;
import com.oceanview.notify.event.ReservationConfirmedEvent;
import com.oceanview.notify.template.ReservationConfirmedTemplate;

public class ReservationEmailListener {

    private final NotificationService notificationService;

    public ReservationEmailListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void onConfirmed(ReservationConfirmedEvent event) throws Exception {
        notificationService.sendReservationEmail(
                event.getGuestEmail(),
                event.getReservation(),
                new ReservationConfirmedTemplate()
        );
    }
}