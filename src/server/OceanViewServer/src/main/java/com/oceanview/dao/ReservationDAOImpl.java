package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public int create(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservations " +
                "(reservation_code, guest_id, room_type_id, room_id, check_in, check_out, num_guests, status, special_requests, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reservation.getReservationCode());
            ps.setInt(2, reservation.getGuestId());
            ps.setInt(3, reservation.getRoomTypeId());

            if (reservation.getRoomId() == null) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, reservation.getRoomId());
            }

            ps.setDate(5, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(6, Date.valueOf(reservation.getCheckOut()));
            ps.setInt(7, reservation.getNumGuests());
            ps.setString(8, reservation.getStatus());
            ps.setString(9, reservation.getSpecialRequests());

            if (reservation.getCreatedBy() == null) {
                ps.setNull(10, java.sql.Types.INTEGER);
            } else {
                ps.setInt(10, reservation.getCreatedBy());
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new Exception("Failed to insert reservation.");
    }

    @Override
    public Reservation findById(int id) throws Exception {
        String sql = "SELECT reservation_id, reservation_code, guest_id, room_type_id, room_id, check_in, check_out, " +
                "num_guests, status, special_requests, created_by " +
                "FROM reservations WHERE reservation_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reservation r = new Reservation();
                    r.setReservationId(rs.getInt("reservation_id"));
                    r.setReservationCode(rs.getString("reservation_code"));
                    r.setGuestId(rs.getInt("guest_id"));
                    r.setRoomTypeId(rs.getInt("room_type_id"));

                    int roomId = rs.getInt("room_id");
                    r.setRoomId(rs.wasNull() ? null : roomId);

                    r.setCheckIn(rs.getDate("check_in").toLocalDate());
                    r.setCheckOut(rs.getDate("check_out").toLocalDate());
                    r.setNumGuests(rs.getInt("num_guests"));
                    r.setStatus(rs.getString("status"));
                    r.setSpecialRequests(rs.getString("special_requests"));

                    int createdBy = rs.getInt("created_by");
                    r.setCreatedBy(rs.wasNull() ? null : createdBy);

                    return r;
                }
            }
        }

        return null;
    }
}