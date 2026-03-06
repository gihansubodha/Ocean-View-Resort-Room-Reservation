package com.oceanview.notify.listener;

import com.oceanview.notify.NotificationService;
import com.oceanview.notify.event.ReservationCancelledEvent;
import com.oceanview.notify.event.ReservationCheckedInEvent;
import com.oceanview.notify.event.ReservationCheckedOutEvent;
import com.oceanview.notify.event.ReservationConfirmedEvent;
import com.oceanview.notify.event.ReservationCreatedEvent;
import com.oceanview.notify.event.ReservationEvent;
import com.oceanview.notify.event.ReservationPaymentEvent;
import com.oceanview.notify.template.ReservationCancelledTemplate;
import com.oceanview.notify.template.ReservationCheckedInTemplate;
import com.oceanview.notify.template.ReservationCheckedOutTemplate;
import com.oceanview.notify.template.ReservationConfirmedTemplate;
import com.oceanview.notify.template.ReservationCreatedTemplate;
import com.oceanview.notify.template.ReservationPaymentTemplate;

public class ReservationEmailListener implements ReservationEventListener {

    private final NotificationService notificationService;

    public ReservationEmailListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(ReservationEvent event) throws Exception {
        if (event == null || event.getReservation() == null) return;
        if (event.getGuestEmail() == null || event.getGuestEmail().isBlank()) return;

        if (event instanceof ReservationCreatedEvent) {
            notificationService.sendReservationEmail(
                    event.getGuestEmail(),
                    event.getReservation(),
                    new ReservationCreatedTemplate()
            );
            return;
        }

        if (event instanceof ReservationConfirmedEvent) {
            notificationService.sendReservationEmail(
                    event.getGuestEmail(),
                    event.getReservation(),
                    new ReservationConfirmedTemplate()
            );
            return;
        }

        if (event instanceof ReservationPaymentEvent) {
            ReservationPaymentEvent e = (ReservationPaymentEvent) event;
            notificationService.sendReservationEmail(
                    e.getGuestEmail(),
                    e.getReservation(),
                    new ReservationPaymentTemplate(
                            e.getAmount(),
                            e.getMethod(),
                            e.getPaidTotal(),
                            e.getBalance()
                    )
            );
            return;
        }

        if (event instanceof ReservationCancelledEvent) {
            notificationService.sendReservationEmail(
                    event.getGuestEmail(),
                    event.getReservation(),
                    new ReservationCancelledTemplate()
            );
            return;
        }

        if (event instanceof ReservationCheckedInEvent) {
            notificationService.sendReservationEmail(
                    event.getGuestEmail(),
                    event.getReservation(),
                    new ReservationCheckedInTemplate()
            );
            return;
        }

        if (event instanceof ReservationCheckedOutEvent) {
            notificationService.sendReservationEmail(
                    event.getGuestEmail(),
                    event.getReservation(),
                    new ReservationCheckedOutTemplate()
            );
        }
    }
}