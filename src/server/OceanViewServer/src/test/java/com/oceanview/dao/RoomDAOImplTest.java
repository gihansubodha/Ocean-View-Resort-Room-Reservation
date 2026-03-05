package com.oceanview.dao;

import com.oceanview.model.Room;
import org.junit.After;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RoomDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void findByStatus_returnsMappedRooms() throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status FROM rooms r JOIN room_types rt ON r.room_type_id = rt.room_type_id WHERE r.status = ? ORDER BY r.room_number";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("room_id", 10, "room_number", "A101", "room_type_id", 2, "type_name", "Deluxe", "status", "AVAILABLE")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<Room> result = new RoomDAOImpl().findByStatus("AVAILABLE");

        assertEquals(1, result.size());
        assertEquals("A101", result.get(0).getRoomNumber());
        assertEquals("AVAILABLE", ps.params.get(1));
    }

    @Test
    public void findAvailableByRoomType_returnsOnlyMappedAvailableRooms() throws Exception {
        String sql = "SELECT room_id, room_number, room_type_id, status FROM rooms WHERE room_type_id = ? AND status = 'AVAILABLE' ORDER BY room_number ASC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Arrays.asList(
                DbTestSupport.row("room_id", 1, "room_number", "101", "room_type_id", 1, "status", "AVAILABLE"),
                DbTestSupport.row("room_id", 2, "room_number", "102", "room_type_id", 1, "status", "AVAILABLE")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<Room> result = new RoomDAOImpl().findAvailableByRoomType(1);

        assertEquals(2, result.size());
        assertNull(result.get(0).getRoomTypeName());
        assertEquals(1, ps.params.get(1));
    }

    @Test
    public void findById_returnsNull_whenRoomMissing() throws Exception {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, rt.type_name, r.status FROM rooms r JOIN room_types rt ON r.room_type_id = rt.room_type_id WHERE r.room_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Room result = new RoomDAOImpl().findById(44);

        assertNull(result);
    }

    @Test
    public void updateStatus_writesParametersAndExecutesUpdate() throws Exception {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        new RoomDAOImpl().updateStatus(22, "OCCUPIED");

        assertEquals("OCCUPIED", ps.params.get(1));
        assertEquals(22, ps.params.get(2));
        assertTrue(ps.executed);
    }
}
