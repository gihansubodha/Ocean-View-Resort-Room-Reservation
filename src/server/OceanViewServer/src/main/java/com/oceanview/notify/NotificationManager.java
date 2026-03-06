package com.oceanview.notify;

import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.event.ReservationEvent;
import com.oceanview.notify.event.ReservationEventBus;
import com.oceanview.notify.listener.ReservationEmailListener;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.sender.GmailSmtpEmailSender;

public final class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    private final ReservationEventBus eventBus;

    private NotificationManager() {
        ReservationEventBus bus = new ReservationEventBus();

        try {
            String user = System.getenv("OCEANVIEW_GMAIL_USER");
            String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

            if (user != null && !user.isBlank() && pass != null && !pass.isBlank()) {
                GmailConfig cfg = new GmailConfig(user, pass);
                EmailSender sender = new GmailSmtpEmailSender(cfg);
                NotificationService notificationService = new NotificationService(sender);

                bus.registerListener(new ReservationEmailListener(notificationService));
            }
        } catch (Exception ignored) {
            // Keep notifications non-blocking for business flow
        }

        this.eventBus = bus;
    }

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    public void publish(ReservationEvent event) {
        if (event == null) return;

        try {
            eventBus.publish(event);
        } catch (Exception ignored) {
            // Do not break reservation flow if email fails
        }
    }
}