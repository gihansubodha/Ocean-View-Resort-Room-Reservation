package com.oceanview.service;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Bill;
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

    public void generateBill(int reservationId) throws Exception {
        if (reservationId <= 0) throw new IllegalArgumentException("Invalid reservation ID");
        try (Connection con = DbUtil.getConnection();
             CallableStatement cs = con.prepareCall("{CALL sp_generate_bill(?)}")) {
            cs.setInt(1, reservationId);
            cs.execute();
        }
    }

    public void updateStatus(int reservationId, String status) throws Exception {
        if (reservationId <= 0) throw new IllegalArgumentException("Invalid reservation id");
        reservationDAO.updateStatus(reservationId, status);
    }

    public void cancel(int reservationId) throws Exception {
        Integer roomId = reservationDAO.findRoomId(reservationId);
        updateStatus(reservationId, "CANCELLED");
        if (roomId != null) {
            new RoomDAOImpl().updateStatus(roomId, "AVAILABLE");
        }
    }

    public void checkIn(int reservationId) throws Exception {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        if (!"CONFIRMED".equalsIgnoreCase(r.getStatus())) {
            throw new IllegalArgumentException("Confirm the reservation first");
        }

        // Removed date restriction because your required flow is:
        // confirmed reservation -> allow check-in

        updateStatus(reservationId, "CHECKED_IN");
        generateBill(reservationId);

        if (r.getRoomId() != null) {
            new RoomDAOImpl().updateStatus(r.getRoomId(), "OCCUPIED");
        }
    }

    public void checkOut(int reservationId) throws Exception {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        if (!"CHECKED_IN".equalsIgnoreCase(r.getStatus())) {
            throw new IllegalArgumentException("Check-out allowed only after CHECKED_IN.");
        }

        BillDAO billDAO = new BillDAOImpl();
        Bill bill = billDAO.findByReservationId(reservationId);
        if (bill == null) {
            throw new IllegalArgumentException("Bill not found for reservation.");
        }

        if (bill.getBalance() != null && bill.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("PAY_FIRST");
        }

        updateStatus(reservationId, "CHECKED_OUT");

        if (r.getRoomId() != null) {
            new RoomDAOImpl().updateStatus(r.getRoomId(), "AVAILABLE");
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

        if (r.getRoomTypeId() <= 0)
            throw new IllegalArgumentException("Room type is required");
        if (r.getNumGuests() <= 0)
            throw new IllegalArgumentException("Number of guests must be > 0");
    }
}