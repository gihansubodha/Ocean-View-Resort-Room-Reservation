package com.oceanview.dao;

import com.oceanview.model.RoomType;
import java.util.List;

public interface AdminRoomTypeDAO {
    List<RoomType> findAll() throws Exception;
    RoomType findById(int roomTypeId) throws Exception;
    int create(RoomType rt) throws Exception;
    void update(RoomType rt) throws Exception;
    void delete(int roomTypeId) throws Exception;
}