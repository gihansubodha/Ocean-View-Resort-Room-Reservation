package com.oceanview.service;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.dao.RoomTypeDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class BillingService {

    private final BillDAO billDAO;
    private final RoomTypeDAO roomTypeDAO;

    public BillingService(BillDAO billDAO) {
        this(billDAO, new RoomTypeDAOImpl());
    }

    public BillingService(BillDAO billDAO, RoomTypeDAO roomTypeDAO) {
        this.billDAO = billDAO;
        this.roomTypeDAO = roomTypeDAO;
    }

    public Bill generateBillObject(Reservation reservation) throws Exception {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is null");
        }
        if (reservation.getReservationId() <= 0) {
            throw new IllegalArgumentException("Reservation ID is required");
        }
        if (reservation.getRoomTypeId() <= 0) {
            throw new IllegalArgumentException("Room type ID is required");
        }
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }

        long nightsLong = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        if (nightsLong <= 0) {
            throw new IllegalArgumentException("Invalid reservation nights");
        }

        RoomType roomType = roomTypeDAO.findById(reservation.getRoomTypeId());
        if (roomType == null) {
            throw new IllegalArgumentException("Room type not found");
        }
        if (roomType.getNightlyRate() == null) {
            throw new IllegalArgumentException("Nightly rate not found for room type ID: " + reservation.getRoomTypeId());
        }

        BigDecimal rate = roomType.getNightlyRate();
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(nightsLong));

        Bill bill = new Bill();
        bill.setReservationId(reservation.getReservationId());
        bill.setNights((int) nightsLong);
        bill.setNightlyRate(rate);
        bill.setSubtotal(subtotal);
        bill.setDiscountAmount(BigDecimal.ZERO);
        bill.setTaxAmount(BigDecimal.ZERO);
        bill.setTotal(subtotal);
        bill.setPaidTotal(BigDecimal.ZERO);
        bill.setBalance(subtotal);
        bill.setBillStatus("UNPAID");

        return bill;
    }

    public int saveBill(Bill bill) throws Exception {
        if (bill == null) {
            throw new IllegalArgumentException("Bill is null");
        }
        return billDAO.create(bill);
    }

    public int createBill(Bill bill) throws Exception {
        return saveBill(bill);
    }
}