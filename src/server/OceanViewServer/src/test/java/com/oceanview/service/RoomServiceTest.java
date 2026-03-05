package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RoomServiceTest {

    private StubRoomDAO roomDAO;
    private RoomService service;

    @Before
    public void setUp() throws Exception {
        roomDAO = new StubRoomDAO();
        service = new RoomService(roomDAO);
    }

    @Test
    public void getAllRooms_returnsDaoResults() throws Exception {
        roomDAO.allRooms = Arrays.asList(new Room(), new Room());
        assertEquals(2, service.getAllRooms().size());
    }

    @Test
    public void getRoomsByStatus_returnsAllRooms_whenStatusIsNull() throws Exception {
        roomDAO.allRooms = Arrays.asList(new Room(), new Room(), new Room());
        assertEquals(3, service.getRoomsByStatus(null).size());
        assertNull(roomDAO.lastStatus);
    }

    @Test
    public void getRoomsByStatus_returnsAllRooms_whenStatusIsBlank() throws Exception {
        roomDAO.allRooms = Arrays.asList(new Room());
        assertEquals(1, service.getRoomsByStatus("   ").size());
        assertNull(roomDAO.lastStatus);
    }

    @Test
    public void getRoomsByStatus_filtersByStatus_whenValueProvided() throws Exception {
        roomDAO.statusRooms = Collections.singletonList(new Room());
        List<Room> result = service.getRoomsByStatus("AVAILABLE");
        assertEquals(1, result.size());
        assertEquals("AVAILABLE", roomDAO.lastStatus);
    }

    private static class StubRoomDAO implements RoomDAO {
        private List<Room> allRooms = Collections.emptyList();
        private List<Room> statusRooms = Collections.emptyList();
        private String lastStatus;

        @Override public List<Room> findAll() { return allRooms; }
        @Override public List<Room> findByStatus(String status) { this.lastStatus = status; return statusRooms; }
        @Override public List<Room> findAvailableByRoomType(int roomTypeId) { return Collections.emptyList(); }
        @Override public Room findById(int roomId) { return null; }
        @Override public void updateStatus(int roomId, String status) { }
    }
}
