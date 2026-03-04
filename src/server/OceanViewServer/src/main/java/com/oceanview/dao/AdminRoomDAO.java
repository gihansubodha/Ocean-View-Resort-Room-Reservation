package com.oceanview.dao;

import com.oceanview.model.Room;
import java.util.List;

public interface AdminRoomDAO {
    List<Room> findAll() throws Exception;
    Room findById(int roomId) throws Exception;
    int create(Room r) throws Exception;
    void update(Room r) throws Exception;
    void delete(int roomId) throws Exception;
}