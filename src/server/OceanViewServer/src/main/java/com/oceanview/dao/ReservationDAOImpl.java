package com.oceanview.dao;

import com.oceanview.model.Reservation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservationDAOImpl implements ReservationDAO {

    private static final Map<Integer, Reservation> STORE = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    @Override
    public int create(Reservation reservation) {
        int id = ID_GENERATOR.getAndIncrement();
        reservation.setReservationId(id);
        STORE.put(id, reservation);
        return id;
    }

    @Override
    public Reservation findById(int id) {
        return STORE.get(id);
    }
}