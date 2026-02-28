package com.oceanview.dao;

import com.oceanview.model.Room;

import java.util.List;

public interface RoomDAO {
    List<Room> findAll() throws Exception;
    List<Room> findByStatus(String status) throws Exception;
}