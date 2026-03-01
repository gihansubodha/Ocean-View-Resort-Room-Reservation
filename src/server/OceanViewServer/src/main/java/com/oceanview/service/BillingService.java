package com.oceanview.service;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.dao.RoomTypeDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;
import com.oceanview.model.RoomType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

public class BillingService {

    private final BillDAO billDAO;
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    private static final BigDecimal TAX_RATE = new BigDecimal("0.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.00");

    public BillingService(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    // Old method kept safe for existing code
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
        return buildBill(
                reservation.getReservationId(),
                nights,
                rate
        );
    }

    // New method for reservation details page / bill page
    public Bill calculateBill(ReservationDetails d) {
        if (d == null) {
            throw new IllegalArgumentException("Reservation details are required");
        }
        if (d.getReservationId() <= 0) {
            throw new IllegalArgumentException("Reservation ID is required");
        }
        if (d.getCheckIn() == null || d.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }

        long nightsLong = ChronoUnit.DAYS.between(d.getCheckIn(), d.getCheckOut());
        if (nightsLong <= 0) {
            throw new IllegalArgumentException("Invalid dates: check-out must be after check-in");
        }

        int nights = (int) nightsLong;
        BigDecimal nightlyRate = BigDecimal.valueOf(d.getNightlyRate());

        return buildBill(d.getReservationId(), nights, nightlyRate);
    }

    public long calculateNights(ReservationDetails d) {
        if (d == null || d.getCheckIn() == null || d.getCheckOut() == null) {
            throw new IllegalArgumentException("Reservation dates are required");
        }
        return ChronoUnit.DAYS.between(d.getCheckIn(), d.getCheckOut());
    }

    public int createBill(Bill bill) throws Exception {
        return billDAO.create(bill);
    }

    private Bill buildBill(int reservationId, int nights, BigDecimal nightlyRate) {
        BigDecimal cleanRate = nightlyRate.setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = cleanRate.multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = subtotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax).subtract(discount).setScale(2, RoundingMode.HALF_UP);

        Bill bill = new Bill();
        bill.setReservationId(reservationId);
        bill.setNights(nights);
        bill.setNightlyRate(cleanRate);
        bill.setSubtotal(subtotal);
        bill.setTaxAmount(tax);
        bill.setDiscountAmount(discount);
        bill.setTotal(total);

        return bill;
    }
}