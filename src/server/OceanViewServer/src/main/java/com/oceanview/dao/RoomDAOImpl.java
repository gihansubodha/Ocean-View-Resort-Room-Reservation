package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public List<Room> findAll() throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status " +
                "FROM rooms r " +
                "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "ORDER BY r.room_number";

        List<Room> rooms = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomTypeId(rs.getInt("room_type_id"));
                room.setRoomTypeName(rs.getString("type_name"));
                room.setStatus(rs.getString("status"));
                rooms.add(room);
            }
        }

        return rooms;
    }

    @Override
    public List<Room> findByStatus(String status) throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status " +
                "FROM rooms r " +
                "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.status = ? " +
                "ORDER BY r.room_number";

        List<Room> rooms = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setRoomTypeId(rs.getInt("room_type_id"));
                    room.setRoomTypeName(rs.getString("type_name"));
                    room.setStatus(rs.getString("status"));
                    rooms.add(room);
                }
            }
        }

        return rooms;
    }
}