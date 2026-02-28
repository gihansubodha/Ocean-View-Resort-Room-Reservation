package com.oceanview.service;

import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class BillingService {

    public Bill generateBill(Reservation r, BigDecimal nightlyRate) {
        if (r == null) throw new IllegalArgumentException("Reservation is null");
        if (nightlyRate == null || nightlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid nightly rate");
        }

        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
        BigDecimal total = nightlyRate.multiply(BigDecimal.valueOf(nights));

        Bill bill = new Bill();
        bill.setReservationId(r.getReservationId());
        bill.setNights((int) nights);
        bill.setNightlyRate(nightlyRate);
        bill.setTotal(total);
        return bill;
    }
}