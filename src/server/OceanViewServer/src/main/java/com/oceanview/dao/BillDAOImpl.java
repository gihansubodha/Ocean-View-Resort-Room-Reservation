package com.oceanview.dao;

import com.oceanview.model.Bill;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new Exception("Failed to create bill.");
    }

    @Override
    public Bill findByReservationId(int reservationId) throws Exception {
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount " +
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
                    return bill;
                }
            }
        }

        return null;
    }
}