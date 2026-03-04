package com.oceanview.dao;

import com.oceanview.model.Bill;
import com.oceanview.util.DbUtil;

import java.sql.*;

public class BillDAOImpl implements BillDAO {

    @Override
    public int create(Bill bill) throws Exception {
        String sql = "INSERT INTO bills " +
                "(reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, bill.getReservationId());
            ps.setInt(2, bill.getNights());
            ps.setBigDecimal(3, bill.getNightlyRate());
            ps.setBigDecimal(4, bill.getSubtotal());
            ps.setBigDecimal(5, bill.getDiscountAmount());
            ps.setBigDecimal(6, bill.getTaxAmount());
            ps.setBigDecimal(7, bill.getTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new Exception("Failed to create bill.");
    }

    @Override
    public void generateBill(int reservationId) throws Exception {
        if (reservationId <= 0) throw new IllegalArgumentException("Invalid reservation ID");
        try (Connection con = DbUtil.getConnection();
             CallableStatement cs = con.prepareCall("{CALL sp_generate_bill(?)}")) {
            cs.setInt(1, reservationId);
            cs.execute();
        }
    }

    @Override
    public void addPayment(int billId, double amount, String method) throws Exception {
        if (billId <= 0) throw new IllegalArgumentException("Invalid bill ID");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (method == null || method.isBlank()) throw new IllegalArgumentException("Payment method required");

        // Only allow valid methods (matches your DB enum)
        String m = method.trim().toUpperCase();
        if (!("CASH".equals(m) || "CARD".equals(m) || "TRANSFER".equals(m))) {
            throw new IllegalArgumentException("Invalid payment method");
        }

        try (Connection con = DbUtil.getConnection();
             CallableStatement cs = con.prepareCall("{CALL sp_add_payment(?,?,?)}")) {
            cs.setInt(1, billId);
            cs.setBigDecimal(2, new java.math.BigDecimal(String.valueOf(amount)));
            cs.setString(3, m);
            cs.execute();
        }
    }

    @Override
    public Bill findByReservationId(int reservationId) throws Exception {
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, " +
                "paid_total, balance, bill_status " +
                "FROM bills WHERE reservation_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setReservationId(rs.getInt("reservation_id"));
                    bill.setNights(rs.getInt("nights"));
                    bill.setNightlyRate(rs.getBigDecimal("rate_per_night"));
                    bill.setSubtotal(rs.getBigDecimal("subtotal"));
                    bill.setDiscountAmount(rs.getBigDecimal("discount_amount"));
                    bill.setTaxAmount(rs.getBigDecimal("tax_amount"));
                    bill.setTotal(rs.getBigDecimal("total_amount"));

                    // Payment summary
                    bill.setPaidTotal(rs.getBigDecimal("paid_total"));
                    bill.setBalance(rs.getBigDecimal("balance"));
                    bill.setBillStatus(rs.getString("bill_status"));

                    return bill;
                }
            }
        }

        return null;
    }
}