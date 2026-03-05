package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.DbTestSupport;
import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ReservationServiceAdditionalTest {
    private StubReservationDAO dao;
    private ReservationService service;

    @Before
    public void setUp() {
        dao = new StubReservationDAO();
        service = new ReservationService(dao);
    }

    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void generateBill_throws_whenReservationIdInvalid() throws Exception {
        try {
            service.generateBill(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid reservation ID", ex.getMessage());
        }
    }

    @Test
    public void generateBill_callsStoredProcedure_whenReservationIdValid() throws Exception {
        String sql = "{CALL sp_generate_bill(?)}";
        DbTestSupport.StatementStub cs = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> callable = new HashMap<>();
        callable.put(sql, cs);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(Collections.emptyMap(), callable)));

        service.generateBill(12);

        assertEquals(12, cs.params.get(1));
        assertTrue(cs.executed);
    }

    @Test
    public void cancel_updatesRoomStatus_whenReservationHasRoom() throws Exception {
        dao.findRoomIdResult = 44;
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        service.cancel(7);

        assertEquals(7, dao.updatedReservationId);
        assertEquals("CANCELLED", dao.updatedStatus);
        assertEquals("AVAILABLE", ps.params.get(1));
        assertEquals(44, ps.params.get(2));
    }

    @Test
    public void checkIn_updatesStatus_generatesBill_andOccupiesRoom_whenConfirmed() throws Exception {
        Reservation r = validReservation("CONFIRMED", 9);
        dao.findByIdResult = r;

        DbTestSupport.StatementStub roomPs = DbTestSupport.statement();
        DbTestSupport.StatementStub billCs = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put("UPDATE rooms SET status = ? WHERE room_id = ?", roomPs);
        Map<String, DbTestSupport.StatementStub> callable = new HashMap<>();
        callable.put("{CALL sp_generate_bill(?)}", billCs);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, callable)));

        service.checkIn(9);

        assertEquals(9, dao.updatedReservationId);
        assertEquals("CHECKED_IN", dao.updatedStatus);
        assertEquals(9, billCs.params.get(1));
        assertEquals("OCCUPIED", roomPs.params.get(1));
        assertEquals(9, roomPs.params.get(2));
    }

    @Test
    public void checkIn_skipsRoomUpdate_whenRoomIdMissing() throws Exception {
        Reservation r = validReservation("CONFIRMED", null);
        dao.findByIdResult = r;
        DbTestSupport.StatementStub billCs = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> callable = new HashMap<>();
        callable.put("{CALL sp_generate_bill(?)}", billCs);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(Collections.emptyMap(), callable)));

        service.checkIn(5);

        assertEquals("CHECKED_IN", dao.updatedStatus);
        assertEquals(5, billCs.params.get(1));
    }

    @Test
    public void checkOut_throws_whenBillMissing() throws Exception {
        dao.findByIdResult = validReservation("CHECKED_IN", 3);
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        try {
            service.checkOut(3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Bill not found for reservation.", ex.getMessage());
        }
    }

    @Test
    public void checkOut_throwsPayFirst_whenBalancePositive() throws Exception {
        dao.findByIdResult = validReservation("CHECKED_IN", 3);
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row(
                "bill_id", 1, "reservation_id", 3, "nights", 2, "rate_per_night", new BigDecimal("100.00"), "subtotal", new BigDecimal("200.00"),
                "discount_amount", BigDecimal.ZERO, "tax_amount", BigDecimal.ZERO, "total_amount", new BigDecimal("200.00"),
                "paid_total", new BigDecimal("100.00"), "balance", new BigDecimal("100.00"), "bill_status", "PARTIAL"
        )));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        try {
            service.checkOut(3);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("PAY_FIRST", ex.getMessage());
        }
    }

    @Test
    public void checkOut_updatesStatus_andReleasesRoom_whenBalanceZero() throws Exception {
        dao.findByIdResult = validReservation("CHECKED_IN", 6);
        String billSql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub billPs = DbTestSupport.statement();
        billPs.queryResult = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row(
                "bill_id", 1, "reservation_id", 6, "nights", 2, "rate_per_night", new BigDecimal("100.00"), "subtotal", new BigDecimal("200.00"),
                "discount_amount", BigDecimal.ZERO, "tax_amount", BigDecimal.ZERO, "total_amount", new BigDecimal("200.00"),
                "paid_total", new BigDecimal("200.00"), "balance", BigDecimal.ZERO, "bill_status", "PAID"
        )));
        DbTestSupport.StatementStub roomPs = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(billSql, billPs);
        prepared.put("UPDATE rooms SET status = ? WHERE room_id = ?", roomPs);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        service.checkOut(6);

        assertEquals("CHECKED_OUT", dao.updatedStatus);
        assertEquals("AVAILABLE", roomPs.params.get(1));
        assertEquals(6, roomPs.params.get(2));
    }

    private Reservation validReservation(String status, Integer roomId) {
        Reservation r = new Reservation();
        r.setReservationId(roomId == null ? 5 : roomId);
        r.setGuestId(1);
        r.setRoomTypeId(1);
        r.setRoomId(roomId);
        r.setCheckIn(java.time.LocalDate.of(2026, 3, 10));
        r.setCheckOut(java.time.LocalDate.of(2026, 3, 12));
        r.setNumGuests(2);
        r.setStatus(status);
        return r;
    }

    private static class StubReservationDAO implements ReservationDAO {
        private Reservation findByIdResult;
        private Integer findRoomIdResult;
        private int updatedReservationId;
        private String updatedStatus;

        @Override public int create(Reservation reservation) { return 0; }
        @Override public Reservation findById(int id) { return findByIdResult; }
        @Override public ReservationDetails findDetailsById(int reservationId) { return null; }
        @Override public void updateStatus(int reservationId, String status) { updatedReservationId = reservationId; updatedStatus = status; }
        @Override public Integer findRoomId(int reservationId) { return findRoomIdResult; }
        @Override public void updateReservation(Reservation reservation) { }
    }
}
