package com.oceanview.dao;

import com.oceanview.model.Reservation;

public interface ReservationDAO {
    int create(Reservation reservation) throws Exception;
    Reservation findById(int id) throws Exception;
}