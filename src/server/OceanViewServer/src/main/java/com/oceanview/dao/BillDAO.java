package com.oceanview.dao;

import com.oceanview.model.Bill;

public interface BillDAO {
    int create(Bill bill) throws Exception;

    Bill findByReservationId(int reservationId) throws Exception;
}