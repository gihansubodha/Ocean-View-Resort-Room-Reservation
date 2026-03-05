package com.oceanview.dao;

import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RoomTypeDAOImplAdditionalTest {
    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void findActiveRoomTypes_returnsEmptyList_whenNoRows() throws Exception {
        String sql = "SELECT room_type_id, type_name, description, nightly_rate, max_guests, is_active FROM room_types WHERE is_active = 1 ORDER BY type_name";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        assertTrue(new RoomTypeDAOImpl().findActiveRoomTypes().isEmpty());
    }
}
