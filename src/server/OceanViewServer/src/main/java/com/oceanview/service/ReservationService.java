package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.util.DbUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
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

    // ✅ NEW: call stored procedure to generate / re-generate bill
    public void generateBill(int reservationId) throws Exception {
        if (reservationId <= 0) throw new IllegalArgumentException("Invalid reservation ID");
        try (Connection con = DbUtil.getConnection();
             CallableStatement cs = con.prepareCall("{CALL sp_generate_bill(?)}")) {
            cs.setInt(1, reservationId);
            cs.execute();
        }
    }

    private void validateReservation(Reservation r) {
        if (r == null) throw new IllegalArgumentException("Reservation is null");
        if (r.getGuestId() <= 0)
            throw new IllegalArgumentException("Valid guest ID required");

        LocalDate in = r.getCheckIn();
        LocalDate out = r.getCheckOut();
        if (in == null || out == null)
            throw new IllegalArgumentException("Dates required");
        if (!out.isAfter(in))
            throw new IllegalArgumentException("Check-out must be after check-in");

        // minimal safety (won't affect existing behaviour unless invalid data)
        if (r.getRoomTypeId() <= 0)
            throw new IllegalArgumentException("Room type is required");
        if (r.getNumGuests() <= 0)
            throw new IllegalArgumentException("Number of guests must be > 0");
    }
    // --- ADD THESE METHODS INSIDE ReservationService ---

    public void updateStatus(int reservationId, String status) throws Exception {
        if (reservationId <= 0) throw new IllegalArgumentException("Invalid reservation id");
        reservationDAO.updateStatus(reservationId, status);
    }

    public void confirm(int reservationId) throws Exception {
        updateStatus(reservationId, "CONFIRMED");
        generateBill(reservationId); // you already added generateBill()
    }

    public void cancel(int reservationId) throws Exception {
        Integer roomId = reservationDAO.findRoomId(reservationId);
        updateStatus(reservationId, "CANCELLED");
        // room becomes AVAILABLE via trigger, but this is safe extra
        if (roomId != null) {
            new com.oceanview.dao.RoomDAOImpl().updateStatus(roomId, "AVAILABLE");
        }
    }

    public void checkIn(int reservationId) throws Exception {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) throw new IllegalArgumentException("Reservation not found");

        LocalDate today = LocalDate.now();
        if (today.isBefore(r.getCheckIn())) {
            throw new IllegalArgumentException("Check-in not valid yet (before check-in date).");
        }

        updateStatus(reservationId, "CHECKED_IN");
        generateBill(reservationId);
    }

    public void checkOut(int reservationId) throws Exception {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) throw new IllegalArgumentException("Reservation not found");

        if (!"CHECKED_IN".equalsIgnoreCase(r.getStatus())) {
            throw new IllegalArgumentException("Check-out allowed only after CHECKED_IN.");
        }

        updateStatus(reservationId, "CHECKED_OUT");

        // room becomes AVAILABLE via trigger, but also safe to do in Java
        if (r.getRoomId() != null) {
            new com.oceanview.dao.RoomDAOImpl().updateStatus(r.getRoomId(), "AVAILABLE");
        }
    }
}