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
                if (rs.next()) {
                    Guest guest = new Guest();
                    guest.setGuestId(rs.getInt("guest_id"));
                    guest.setFirstName(rs.getString("first_name"));
                    guest.setLastName(rs.getString("last_name"));
                    guest.setNicPassport(rs.getString("nic_passport"));
                    guest.setPhone(rs.getString("phone"));
                    guest.setEmail(rs.getString("email"));
                    guest.setAddress(rs.getString("address"));
                    return guest;
                }
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
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new Exception("Failed to insert guest.");
    }
}