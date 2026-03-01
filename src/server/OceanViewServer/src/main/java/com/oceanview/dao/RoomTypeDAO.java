package com.oceanview.dao;

import com.oceanview.model.RoomType;

import java.util.List;

public interface RoomTypeDAO {
    List<RoomType> findActiveRoomTypes() throws Exception;

    RoomType findById(int roomTypeId) throws Exception;
}