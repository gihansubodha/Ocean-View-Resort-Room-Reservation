package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RoomServiceAdditionalTest {
    private StubRoomDAO dao;
    private RoomService service;

    @Before
    public void setUp() {
        dao = new StubRoomDAO();
        service = new RoomService(dao);
    }

    @Test
    public void getAllRooms_returnsEmptyList_whenDaoEmpty() throws Exception {
        dao.allRooms = Collections.emptyList();
        assertTrue(service.getAllRooms().isEmpty());
    }

    @Test
    public void getRoomsByStatus_treatsTabsAndNewlinesAsBlank() throws Exception {
        dao.allRooms = Collections.singletonList(new Room());
        assertEquals(1, service.getRoomsByStatus(" \t \n ").size());
        assertNull(dao.lastStatus);
    }

    @Test
    public void getRoomsByStatus_passesThroughMixedCaseStatus() throws Exception {
        service.getRoomsByStatus("available");
        assertEquals("available", dao.lastStatus);
    }

    private static class StubRoomDAO implements RoomDAO {
        List<Room> allRooms = Collections.emptyList();
        String lastStatus;
        @Override public List<Room> findAll() { return allRooms; }
        @Override public List<Room> findByStatus(String status) { lastStatus = status; return Collections.emptyList(); }
        @Override public List<Room> findAvailableByRoomType(int roomTypeId) { return Collections.emptyList(); }
        @Override public Room findById(int roomId) { return null; }
        @Override public void updateStatus(int roomId, String status) { }
    }
}
