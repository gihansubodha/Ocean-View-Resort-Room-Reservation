package com.oceanview.service;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BillingServiceAdditionalTest {
    private BillingService service;
    private StubBillDAO billDAO;
    private StubRoomTypeDAO roomTypeDAO;

    @Before
    public void setUp() {
        billDAO = new StubBillDAO();
        roomTypeDAO = new StubRoomTypeDAO();
        service = new BillingService(billDAO, roomTypeDAO);
    }

    @Test
    public void generateBillObject_calculatesOneNightStay() throws Exception {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setRoomTypeId(4);
        r.setCheckIn(LocalDate.of(2026,3,10));
        r.setCheckOut(LocalDate.of(2026,3,11));
        RoomType rt = new RoomType();
        rt.setNightlyRate(new BigDecimal("75.50"));
        roomTypeDAO.findByIdResult = rt;

        Bill bill = service.generateBillObject(r);

        assertEquals(1, bill.getNights());
        assertEquals(new BigDecimal("75.50"), bill.getTotal());
    }

    @Test
    public void generateBillObject_propagatesRoomTypeLookupException() throws Exception {
        roomTypeDAO.throwOnFind = new Exception("lookup failed");
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setRoomTypeId(4);
        r.setCheckIn(LocalDate.of(2026,3,10));
        r.setCheckOut(LocalDate.of(2026,3,11));

        try {
            service.generateBillObject(r);
            fail("Expected Exception");
        } catch (Exception ex) {
            assertEquals("lookup failed", ex.getMessage());
        }
    }

    @Test
    public void createBill_propagatesDaoException() throws Exception {
        billDAO.throwOnCreate = new Exception("create failed");

        try {
            service.createBill(new Bill());
            fail("Expected Exception");
        } catch (Exception ex) {
            assertEquals("create failed", ex.getMessage());
        }
    }

    private static class StubBillDAO implements BillDAO {
        Exception throwOnCreate;
        @Override public int create(Bill bill) throws Exception { if (throwOnCreate != null) throw throwOnCreate; return 1; }
        @Override public Bill findByReservationId(int reservationId) { return null; }
        @Override public void generateBill(int reservationId) { }
        @Override public void addPayment(int billId, double amount, String method) { }
    }

    private static class StubRoomTypeDAO implements RoomTypeDAO {
        RoomType findByIdResult;
        Exception throwOnFind;
        @Override public List<RoomType> findActiveRoomTypes() { return Collections.emptyList(); }
        @Override public RoomType findById(int roomTypeId) throws Exception { if (throwOnFind != null) throw throwOnFind; return findByIdResult; }
    }
}
