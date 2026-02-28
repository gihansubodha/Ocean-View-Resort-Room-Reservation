package com.oceanview.model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private String guestName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String roomType;

    public Reservation() {
    }

    public Reservation(int reservationId, String guestName, LocalDate checkIn, LocalDate checkOut, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomType = roomType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}