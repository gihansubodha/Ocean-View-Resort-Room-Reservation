package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Room;

import java.util.List;

public class RoomService {

    private final RoomDAO roomDAO;

    public RoomService() {
        this(new RoomDAOImpl());
    }

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public List<Room> getAllRooms() throws Exception {
        return roomDAO.findAll();
    }

    public List<Room> getRoomsByStatus(String status) throws Exception {
        if (status == null || status.trim().isEmpty()) {
            return roomDAO.findAll();
        }
        return roomDAO.findByStatus(status);
    }
}