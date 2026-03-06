package com.oceanview.notify.listener;

import com.oceanview.notify.event.ReservationEvent;

public interface ReservationEventListener {
    void onEvent(ReservationEvent event) throws Exception;
}