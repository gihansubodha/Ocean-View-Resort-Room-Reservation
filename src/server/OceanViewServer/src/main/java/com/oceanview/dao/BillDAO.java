package com.oceanview.dao;

import com.oceanview.model.Bill;

public interface BillDAO {

    // Keep existing (compatibility)
    int create(Bill bill) throws Exception;

    // Updated to return full bill info including payment summary
    Bill findByReservationId(int reservationId) throws Exception;

    // Stored procedure: sp_generate_bill(reservationId)
    void generateBill(int reservationId) throws Exception;

    // Stored procedure: sp_add_payment(billId, amount, method)
    void addPayment(int billId, double amount, String method) throws Exception;
}