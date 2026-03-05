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

public class BillingServiceTest {

    private StubBillDAO billDAO;
    private StubRoomTypeDAO roomTypeDAO;
    private BillingService service;

    @Before
    public void setUp() throws Exception {
        billDAO = new StubBillDAO();
        roomTypeDAO = new StubRoomTypeDAO();
        service = new BillingService(billDAO, roomTypeDAO);
    }

    @Test
    public void generateBillObject_throws_whenReservationIsNull() throws Exception {
        expectIllegalArgument(() -> service.generateBillObject(null), "Reservation is null");
    }

    @Test
    public void generateBillObject_throws_whenReservationIdMissing() throws Exception {
        Reservation r = validReservation();
        r.setReservationId(0);
        expectIllegalArgument(() -> service.generateBillObject(r), "Reservation ID is required");
    }

    @Test
    public void generateBillObject_throws_whenRoomTypeMissing() throws Exception {
        Reservation r = validReservation();
        r.setRoomTypeId(0);
        expectIllegalArgument(() -> service.generateBillObject(r), "Room type ID is required");
    }

    @Test
    public void generateBillObject_throws_whenDatesMissing() throws Exception {
        Reservation r = validReservation();
        r.setCheckOut(null);
        expectIllegalArgument(() -> service.generateBillObject(r), "Check-in and check-out dates are required");
    }

    @Test
    public void generateBillObject_throws_whenNightsNotPositive() throws Exception {
        Reservation r = validReservation();
        r.setCheckOut(r.getCheckIn());
        expectIllegalArgument(() -> service.generateBillObject(r), "Invalid reservation nights");
    }

    @Test
    public void generateBillObject_throws_whenRoomTypeNotFound() throws Exception {
        Reservation r = validReservation();
        roomTypeDAO.findByIdResult = null;
        expectIllegalArgument(() -> service.generateBillObject(r), "Room type not found");
    }

    @Test
    public void generateBillObject_calculatesTotalForMultiNightStay() throws Exception {
        Reservation r = validReservation();
        roomTypeDAO.findByIdResult = buildRoomType(new BigDecimal("150.00"));

        Bill bill = service.generateBillObject(r);

        assertEquals(88, bill.getReservationId());
        assertEquals(2, bill.getNights());
        assertEquals(new BigDecimal("150.00"), bill.getNightlyRate());
        assertEquals(new BigDecimal("300.00"), bill.getSubtotal());
        assertEquals(new BigDecimal("0"), bill.getDiscountAmount());
        assertEquals(new BigDecimal("0"), bill.getTaxAmount());
        assertEquals(new BigDecimal("300.00"), bill.getTotal());
    }

    @Test
    public void createBill_delegatesToDao() throws Exception {
        billDAO.createResult = 99;
        Bill bill = new Bill();

        int id = service.createBill(bill);

        assertEquals(99, id);
        assertSame(bill, billDAO.lastCreated);
    }

    private Reservation validReservation() {
        Reservation r = new Reservation();
        r.setReservationId(88);
        r.setRoomTypeId(2);
        r.setCheckIn(LocalDate.of(2026, 3, 10));
        r.setCheckOut(LocalDate.of(2026, 3, 12));
        return r;
    }

    private RoomType buildRoomType(BigDecimal nightlyRate) {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(2);
        roomType.setNightlyRate(nightlyRate);
        return roomType;
    }

    private void expectIllegalArgument(ThrowingRunnable runnable, String message) throws Exception {
        try {
            runnable.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals(message, ex.getMessage());
        }
    }

    private interface ThrowingRunnable { void run() throws Exception; }

    private static class StubBillDAO implements BillDAO {
        private int createResult;
        private Bill lastCreated;

        @Override
        public int create(Bill bill) {
            this.lastCreated = bill;
            return createResult;
        }

        @Override public Bill findByReservationId(int reservationId) { return null; }
        @Override public void generateBill(int reservationId) { }
        @Override public void addPayment(int billId, double amount, String method) { }
    }

    private static class StubRoomTypeDAO implements RoomTypeDAO {
        private RoomType findByIdResult;

        @Override
        public List<RoomType> findActiveRoomTypes() throws Exception {
            return Collections.emptyList();
        }

        @Override
        public RoomType findById(int roomTypeId) throws Exception {
            return findByIdResult;
        }
    }
}
