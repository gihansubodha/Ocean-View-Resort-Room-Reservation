package com.oceanview.dao;

import com.oceanview.model.Reservation;
import org.junit.After;
import org.junit.Test;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class ReservationDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void create_setsNullsForOptionalFields_andReturnsGeneratedId() throws Exception {
        String sql = "INSERT INTO reservations (reservation_code, guest_id, room_type_id, room_id, check_in, check_out, num_guests, status, special_requests, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.generatedKeys = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row("id", 33)));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Reservation r = new Reservation();
        r.setReservationCode("RES-1");
        r.setGuestId(2);
        r.setRoomTypeId(1);
        r.setRoomId(null);
        r.setCheckIn(LocalDate.of(2026,3,10));
        r.setCheckOut(LocalDate.of(2026,3,12));
        r.setNumGuests(2);
        r.setStatus("PENDING");
        r.setSpecialRequests("Late arrival");
        r.setCreatedBy(null);

        int id = new ReservationDAOImpl().create(r);

        assertEquals(33, id);
        assertTrue(ps.params.get(4) instanceof DbTestSupport.SqlNull);
        assertTrue(ps.params.get(10) instanceof DbTestSupport.SqlNull);
        assertEquals(Date.valueOf("2026-03-10"), ps.params.get(5));
    }

    @Test
    public void findRoomId_returnsNull_whenDatabaseValueIsNull() throws Exception {
        String sql = "SELECT room_id FROM reservations WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("room_id", null)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Integer roomId = new ReservationDAOImpl().findRoomId(7);

        assertNull(roomId);
    }

    @Test
    public void updateStatus_executesUpdateWithReservationId() throws Exception {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        new ReservationDAOImpl().updateStatus(14, "CONFIRMED");

        assertEquals("CONFIRMED", ps.params.get(1));
        assertEquals(14, ps.params.get(2));
        assertTrue(ps.executed);
    }
}
