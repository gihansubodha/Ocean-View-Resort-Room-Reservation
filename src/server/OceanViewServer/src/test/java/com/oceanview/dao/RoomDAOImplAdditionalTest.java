package com.oceanview.dao;

import com.oceanview.model.Room;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RoomDAOImplAdditionalTest {
    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void findAll_returnsMappedRooms() throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status FROM rooms r JOIN room_types rt ON r.room_type_id = rt.room_type_id ORDER BY r.room_number";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Arrays.asList(
                DbTestSupport.row("room_id", 1, "room_number", "101", "room_type_id", 1, "type_name", "Standard", "status", "AVAILABLE"),
                DbTestSupport.row("room_id", 2, "room_number", "102", "room_type_id", 2, "type_name", "Deluxe", "status", "OCCUPIED")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<Room> result = new RoomDAOImpl().findAll();

        assertEquals(2, result.size());
        assertEquals("Standard", result.get(0).getRoomTypeName());
        assertEquals("OCCUPIED", result.get(1).getStatus());
    }

    @Test
    public void findAvailableByRoomType_returnsEmptyList_whenNoRows() throws Exception {
        String sql = "SELECT room_id, room_number, room_type_id, status FROM rooms WHERE room_type_id = ? AND status = 'AVAILABLE' ORDER BY room_number ASC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<Room> result = new RoomDAOImpl().findAvailableByRoomType(5);

        assertTrue(result.isEmpty());
        assertEquals(5, ps.params.get(1));
    }

    @Test
    public void findById_returnsMappedRoom_whenFound() throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status FROM rooms r JOIN room_types rt ON r.room_type_id = rt.room_type_id WHERE r.room_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("room_id", 22, "room_number", "A202", "room_type_id", 3, "type_name", "Suite", "status", "MAINTENANCE")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Room room = new RoomDAOImpl().findById(22);

        assertNotNull(room);
        assertEquals("A202", room.getRoomNumber());
        assertEquals("Suite", room.getRoomTypeName());
    }
}
