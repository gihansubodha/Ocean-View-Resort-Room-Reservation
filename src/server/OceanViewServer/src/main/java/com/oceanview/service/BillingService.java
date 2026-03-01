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
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    public BillingService(BillDAO billDAO) {
        this.billDAO = billDAO;
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

        int nights = (int) nightsLong;

        RoomType roomType = roomTypeDAO.findById(reservation.getRoomTypeId());
        if (roomType == null) {
            throw new IllegalArgumentException("Room type not found");
        }

        BigDecimal rate = roomType.getNightlyRate();
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(nights));
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(discount).add(tax);

        Bill bill = new Bill();
        bill.setReservationId(reservation.getReservationId());
        bill.setNights(nights);
        bill.setNightlyRate(rate);
        bill.setSubtotal(subtotal);
        bill.setDiscountAmount(discount);
        bill.setTaxAmount(tax);
        bill.setTotal(total);

        return bill;
    }

    public int createBill(Bill bill) throws Exception {
        return billDAO.create(bill);
    }
}