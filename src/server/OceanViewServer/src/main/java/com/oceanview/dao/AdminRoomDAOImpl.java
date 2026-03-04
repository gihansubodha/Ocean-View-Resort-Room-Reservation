package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRoomDAOImpl implements AdminRoomDAO {

    @Override
    public List<Room> findAll() throws Exception {
        String sql = "SELECT room_id, room_number, room_type_id, status FROM rooms ORDER BY room_number";
        List<Room> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public Room findById(int roomId) throws Exception {
        String sql = "SELECT room_id, room_number, room_type_id, status FROM rooms WHERE room_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    @Override
    public int create(Room r) throws Exception {
        String sql = "INSERT INTO rooms (room_number, room_type_id, status) VALUES (?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getRoomNumber());     // ✅ String
            ps.setInt(2, r.getRoomTypeId());
            ps.setString(3, r.getStatus());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    @Override
    public void update(Room r) throws Exception {
        String sql = "UPDATE rooms SET room_number=?, room_type_id=?, status=? WHERE room_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getRoomNumber());     // ✅ String
            ps.setInt(2, r.getRoomTypeId());
            ps.setString(3, r.getStatus());
            ps.setInt(4, r.getRoomId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int roomId) throws Exception {
        String sql = "DELETE FROM rooms WHERE room_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.executeUpdate();
        }
    }

    private Room map(ResultSet rs) throws Exception {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));   // ✅ String
        r.setRoomTypeId(rs.getInt("room_type_id"));
        r.setStatus(rs.getString("status"));
        return r;
    }
}