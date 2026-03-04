package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;

public interface ReservationDAO {
    int create(Reservation reservation) throws Exception;
    Reservation findById(int id) throws Exception;
    ReservationDetails findDetailsById(int reservationId) throws Exception;

    // confirm/edit/cancel/checkin/checkout endpoints
    void updateStatus(int reservationId, String status) throws Exception;
    Integer findRoomId(int reservationId) throws Exception;
    void updateReservation(Reservation reservation) throws Exception;
}