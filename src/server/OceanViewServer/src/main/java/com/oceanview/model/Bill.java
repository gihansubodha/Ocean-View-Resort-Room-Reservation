package com.oceanview.model;

import java.math.BigDecimal;

public class Bill {
    private int reservationId;
    private int nights;
    private BigDecimal nightlyRate;
    private BigDecimal total;

    public Bill() {
    }

    public Bill(int reservationId, int nights, BigDecimal nightlyRate, BigDecimal total) {
        this.reservationId = reservationId;
        this.nights = nights;
        this.nightlyRate = nightlyRate;
        this.total = total;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public BigDecimal getNightlyRate() {
        return nightlyRate;
    }

    public void setNightlyRate(BigDecimal nightlyRate) {
        this.nightlyRate = nightlyRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}