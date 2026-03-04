package com.oceanview.model;

import java.math.BigDecimal;

public class AdminDashboardStats {

    // Today
    private int todayReservations;
    private int todayCheckedIn;
    private int todayGuestsInHotel;
    private BigDecimal todayFullPaymentsTotal;

    // NEW: Today extras
    private BigDecimal todayOutstandingBalanceTotal;
    private int todayArrivalsNotCheckedIn;

    // Range
    private int rangeReservations;
    private int rangeCheckedIn;
    private int rangeCancelled;
    private BigDecimal rangeTotalPaid;

    public int getTodayReservations() { return todayReservations; }
    public void setTodayReservations(int todayReservations) { this.todayReservations = todayReservations; }

    public int getTodayCheckedIn() { return todayCheckedIn; }
    public void setTodayCheckedIn(int todayCheckedIn) { this.todayCheckedIn = todayCheckedIn; }

    public int getTodayGuestsInHotel() { return todayGuestsInHotel; }
    public void setTodayGuestsInHotel(int todayGuestsInHotel) { this.todayGuestsInHotel = todayGuestsInHotel; }

    public BigDecimal getTodayFullPaymentsTotal() { return todayFullPaymentsTotal; }
    public void setTodayFullPaymentsTotal(BigDecimal todayFullPaymentsTotal) { this.todayFullPaymentsTotal = todayFullPaymentsTotal; }

    public BigDecimal getTodayOutstandingBalanceTotal() { return todayOutstandingBalanceTotal; }
    public void setTodayOutstandingBalanceTotal(BigDecimal todayOutstandingBalanceTotal) { this.todayOutstandingBalanceTotal = todayOutstandingBalanceTotal; }

    public int getTodayArrivalsNotCheckedIn() { return todayArrivalsNotCheckedIn; }
    public void setTodayArrivalsNotCheckedIn(int todayArrivalsNotCheckedIn) { this.todayArrivalsNotCheckedIn = todayArrivalsNotCheckedIn; }

    public int getRangeReservations() { return rangeReservations; }
    public void setRangeReservations(int rangeReservations) { this.rangeReservations = rangeReservations; }

    public int getRangeCheckedIn() { return rangeCheckedIn; }
    public void setRangeCheckedIn(int rangeCheckedIn) { this.rangeCheckedIn = rangeCheckedIn; }

    public int getRangeCancelled() { return rangeCancelled; }
    public void setRangeCancelled(int rangeCancelled) { this.rangeCancelled = rangeCancelled; }

    public BigDecimal getRangeTotalPaid() { return rangeTotalPaid; }
    public void setRangeTotalPaid(BigDecimal rangeTotalPaid) { this.rangeTotalPaid = rangeTotalPaid; }
}