package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import java.time.LocalDate;

public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public int createReservation(Reservation r) throws Exception {
        validateReservation(r);
        return reservationDAO.create(r);
    }

    public Reservation getReservationById(int id) throws Exception {
        if (id <= 0) return null;
        return reservationDAO.findById(id);
    }

    private void validateReservation(Reservation r) {
        if (r == null) throw new IllegalArgumentException("Reservation is null");
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty())
            throw new IllegalArgumentException("Guest name required");

        LocalDate in = r.getCheckIn();
        LocalDate out = r.getCheckOut();
        if (in == null || out == null) throw new IllegalArgumentException("Dates required");
        if (!out.isAfter(in)) throw new IllegalArgumentException("Check-out must be after check-in");
    }
}
