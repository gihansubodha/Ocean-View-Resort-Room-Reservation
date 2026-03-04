package com.oceanview.dao;

import com.oceanview.model.RoomType;
import com.oceanview.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRoomTypeDAOImpl implements AdminRoomTypeDAO {

    @Override
    public List<RoomType> findAll() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active " +
                "FROM room_types ORDER BY room_type_id";
        List<RoomType> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public RoomType findById(int roomTypeId) throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active " +
                "FROM room_types WHERE room_type_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomTypeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    @Override
    public int create(RoomType rt) throws Exception {
        String sql = "INSERT INTO room_types (type_name, description, nightly_rate, max_guests, is_active) " +
                "VALUES (?,?,?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rt.getTypeName());
            ps.setString(2, rt.getDescription());
            ps.setBigDecimal(3, rt.getNightlyRate());
            ps.setInt(4, rt.getMaxGuests());
            ps.setInt(5, rt.isActive() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    @Override
    public void update(RoomType rt) throws Exception {
        String sql = "UPDATE room_types SET type_name=?, description=?, nightly_rate=?, max_guests=?, is_active=? " +
                "WHERE room_type_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rt.getTypeName());
            ps.setString(2, rt.getDescription());
            ps.setBigDecimal(3, rt.getNightlyRate());
            ps.setInt(4, rt.getMaxGuests());
            ps.setInt(5, rt.isActive() ? 1 : 0);
            ps.setInt(6, rt.getRoomTypeId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int roomTypeId) throws Exception {
        String sql = "DELETE FROM room_types WHERE room_type_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomTypeId);
            ps.executeUpdate();
        }
    }

    private RoomType map(ResultSet rs) throws Exception {
        RoomType rt = new RoomType();
        rt.setRoomTypeId(rs.getInt("room_type_id"));
        rt.setTypeName(rs.getString("type_name"));
        rt.setDescription(rs.getString("description"));
        rt.setNightlyRate(rs.getBigDecimal("nightly_rate"));
        rt.setMaxGuests(rs.getInt("max_guests"));
        rt.setActive(rs.getInt("is_active") == 1);
        return rt;
    }
}