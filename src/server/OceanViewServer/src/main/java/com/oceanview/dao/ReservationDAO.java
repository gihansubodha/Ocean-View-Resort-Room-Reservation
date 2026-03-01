package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;

public interface ReservationDAO {
    int create(Reservation reservation) throws Exception;
    Reservation findById(int id) throws Exception;
    ReservationDetails findDetailsById(int reservationId) throws Exception;
}