package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;
import org.junit.After;
import org.junit.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReservationDAOImplAdditionalTest {
    private static final String DETAILS_SELECT = "SELECT  r.reservation_id, r.reservation_code, r.check_in, r.check_out, r.num_guests, r.status AS reservation_status, r.special_requests,  g.guest_id, g.first_name, g.last_name, g.phone, g.email, g.nic_passport,  r.room_id, rm.room_number,  rt.room_type_id, rt.type_name, rt.nightly_rate FROM reservations r JOIN guests g ON r.guest_id = g.guest_id JOIN room_types rt ON r.room_type_id = rt.room_type_id LEFT JOIN rooms rm ON r.room_id = rm.room_id ";

    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void findById_returnsMappedReservation_whenFound() throws Exception {
        String sql = "SELECT reservation_id, reservation_code, guest_id, room_type_id, room_id, check_in, check_out, num_guests, status, special_requests, created_by FROM reservations WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("reservation_id", 5, "reservation_code", "RES-5", "guest_id", 2, "room_type_id", 3, "room_id", 9, "check_in", Date.valueOf("2026-03-10"), "check_out", Date.valueOf("2026-03-12"), "num_guests", 4, "status", "CONFIRMED", "special_requests", "Late", "created_by", null)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Reservation result = new ReservationDAOImpl().findById(5);

        assertNotNull(result);
        assertEquals("RES-5", result.getReservationCode());
        assertEquals(Integer.valueOf(9), result.getRoomId());
        assertNull(result.getCreatedBy());
    }

    @Test
    public void findDetailsById_returnsNull_whenMissing() throws Exception {
        String sql = DETAILS_SELECT + "WHERE r.reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        assertNull(new ReservationDAOImpl().findDetailsById(100));
    }

    @Test
    public void findDetailsByCode_returnsMappedDetails_whenFound() throws Exception {
        String sql = DETAILS_SELECT + "WHERE r.reservation_code = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(detailRow()));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        ReservationDetails details = new ReservationDAOImpl().findDetailsByCode("RES-88");

        assertNotNull(details);
        assertEquals("John", details.getGuestFirstName());
        assertEquals("B202", details.getRoomNumber());
    }

    @Test
    public void findTodayArrivals_returnsMappedList() throws Exception {
        String sql = DETAILS_SELECT + "WHERE r.check_in = ? ORDER BY r.check_in ASC, r.reservation_id DESC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Arrays.asList(detailRow(), detailRow("reservation_id", 89, "reservation_code", "RES-89")));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<ReservationDetails> result = new ReservationDAOImpl().findTodayArrivals(java.time.LocalDate.of(2026,3,10));

        assertEquals(2, result.size());
        assertEquals(Date.valueOf("2026-03-10"), ps.params.get(1));
    }

    @Test
    public void findTodayDepartures_returnsMappedList() throws Exception {
        String sql = DETAILS_SELECT + "WHERE r.check_out = ? ORDER BY r.check_out ASC, r.reservation_id DESC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(detailRow()));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<ReservationDetails> result = new ReservationDAOImpl().findTodayDepartures(java.time.LocalDate.of(2026,3,12));

        assertEquals(1, result.size());
        assertEquals("RES-88", result.get(0).getReservationCode());
    }

    @Test
    public void searchDetails_buildsSqlForAllFilters_andMapsResults() throws Exception {
        String sql = DETAILS_SELECT + "WHERE 1=1  AND g.nic_passport = ?  AND g.phone = ?  AND g.email = ?  AND rm.room_number = ?  AND r.check_in >= ?  AND r.check_in <= ?  ORDER BY r.check_in DESC, r.reservation_id DESC ";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(detailRow()));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<ReservationDetails> result = new ReservationDAOImpl().searchDetails(" NIC1 ", " 070 ", " mail@test.com ", " B202 ", java.time.LocalDate.of(2026,3,1), java.time.LocalDate.of(2026,3,31));

        assertEquals(1, result.size());
        assertEquals("NIC1", ps.params.get(1));
        assertEquals("070", ps.params.get(2));
        assertEquals("mail@test.com", ps.params.get(3));
        assertEquals("B202", ps.params.get(4));
        assertEquals(Date.valueOf("2026-03-01"), ps.params.get(5));
        assertEquals(Date.valueOf("2026-03-31"), ps.params.get(6));
    }

    @Test
    public void updateReservation_setsNullRoomId_whenMissing() throws Exception {
        String sql = "UPDATE reservations SET room_type_id=?, room_id=?, check_in=?, check_out=?, num_guests=?, status=?, special_requests=? WHERE reservation_id=?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Reservation r = new Reservation();
        r.setReservationId(11);
        r.setRoomTypeId(2);
        r.setRoomId(null);
        r.setCheckIn(java.time.LocalDate.of(2026,3,10));
        r.setCheckOut(java.time.LocalDate.of(2026,3,12));
        r.setNumGuests(2);
        r.setStatus("CONFIRMED");
        r.setSpecialRequests("Window");

        new ReservationDAOImpl().updateReservation(r);

        assertEquals(2, ps.params.get(1));
        assertTrue(ps.params.get(2) instanceof DbTestSupport.SqlNull);
        assertEquals(11, ps.params.get(8));
        assertTrue(ps.executed);
    }

    private java.util.LinkedHashMap<String, Object> detailRow(Object... overrides) {
        java.util.LinkedHashMap<String, Object> row = DbTestSupport.row(
                "reservation_id", 88, "reservation_code", "RES-88", "check_in", Date.valueOf("2026-03-10"), "check_out", Date.valueOf("2026-03-12"),
                "num_guests", 2, "reservation_status", "CONFIRMED", "special_requests", "Late",
                "guest_id", 9, "first_name", "John", "last_name", "Doe", "phone", "077", "email", "mail@test.com", "nic_passport", "NIC1",
                "room_id", 7, "room_number", "B202", "room_type_id", 3, "type_name", "Suite", "nightly_rate", 250.0
        );
        for (int i = 0; i < overrides.length; i += 2) row.put(String.valueOf(overrides[i]), overrides[i + 1]);
        return row;
    }
}
