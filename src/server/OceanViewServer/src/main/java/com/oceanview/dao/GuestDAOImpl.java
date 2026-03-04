package com.oceanview.dao;

import com.oceanview.model.Guest;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GuestDAOImpl implements GuestDAO {

    @Override
    public Guest findByNicPassport(String nicPassport) throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address " +
                "FROM guests WHERE nic_passport = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nicPassport);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public Guest findById(int guestId) throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address " +
                "FROM guests WHERE guest_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public int insert(Guest guest) throws Exception {
        String sql = "INSERT INTO guests (first_name, last_name, nic_passport, phone, email, address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, guest.getFirstName());
            ps.setString(2, guest.getLastName());
            ps.setString(3, guest.getNicPassport());
            ps.setString(4, guest.getPhone());
            ps.setString(5, guest.getEmail());
            ps.setString(6, guest.getAddress());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new Exception("Failed to insert guest.");
    }

    private Guest map(ResultSet rs) throws Exception {
        Guest g = new Guest();
        g.setGuestId(rs.getInt("guest_id"));
        g.setFirstName(rs.getString("first_name"));
        g.setLastName(rs.getString("last_name"));
        g.setNicPassport(rs.getString("nic_passport"));
        g.setPhone(rs.getString("phone"));
        g.setEmail(rs.getString("email"));
        g.setAddress(rs.getString("address"));
        return g;
    }
}