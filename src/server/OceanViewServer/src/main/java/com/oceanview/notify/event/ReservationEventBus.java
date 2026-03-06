package com.oceanview.notify.event;

import com.oceanview.notify.listener.ReservationEventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReservationEventBus {

    private final List<ReservationEventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(ReservationEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(ReservationEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(ReservationEvent event) throws Exception {
        for (ReservationEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}