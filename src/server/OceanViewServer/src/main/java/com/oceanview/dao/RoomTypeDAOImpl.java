package com.oceanview.dao;

import com.oceanview.model.RoomType;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAOImpl implements RoomTypeDAO {

    @Override
    public List<RoomType> findActiveRoomTypes() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active " +
                "FROM room_types WHERE is_active = 1 ORDER BY type_name";

        List<RoomType> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRoomType(rs));
            }
        }

        return list;
    }

    @Override
    public RoomType findById(int roomTypeId) throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active " +
                "FROM room_types WHERE room_type_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRoomType(rs);
                }
            }
        }

        return null;
    }

    private RoomType mapRoomType(ResultSet rs) throws Exception {
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