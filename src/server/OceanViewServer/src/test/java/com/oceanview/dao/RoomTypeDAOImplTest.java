package com.oceanview.dao;

import com.oceanview.model.RoomType;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

import static org.junit.Assert.*;

public class RoomTypeDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void findActiveRoomTypes_returnsMappedOrderedList() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active FROM room_types WHERE is_active = 1 ORDER BY type_name";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Arrays.asList(
                DbTestSupport.row("room_type_id", 1, "type_name", "Deluxe", "description", "Sea", "nightly_rate", new BigDecimal("250.00"), "max_guests", 3, "is_active", 1),
                DbTestSupport.row("room_type_id", 2, "type_name", "Suite", "description", "Large", "nightly_rate", new BigDecimal("400.00"), "max_guests", 4, "is_active", 1)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<RoomType> result = new RoomTypeDAOImpl().findActiveRoomTypes();

        assertEquals(2, result.size());
        assertEquals("Deluxe", result.get(0).getTypeName());
        assertEquals(new BigDecimal("400.00"), result.get(1).getNightlyRate());
    }

    @Test
    public void findById_returnsNull_whenNotFound() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active FROM room_types WHERE room_type_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        RoomType result = new RoomTypeDAOImpl().findById(8);

        assertNull(result);
        assertEquals(8, ps.params.get(1));
    }

    @Test
    public void findById_returnsMappedRoomType_whenFound() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active FROM room_types WHERE room_type_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("room_type_id", 3, "type_name", "Standard", "description", "Basic", "nightly_rate", new BigDecimal("120.00"), "max_guests", 2, "is_active", 0)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        RoomType result = new RoomTypeDAOImpl().findById(3);

        assertNotNull(result);
        assertEquals("Standard", result.getTypeName());
        assertFalse(result.isActive());
    }
}
