package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ReservationServiceTest {

    private StubReservationDAO dao;
    private ReservationService service;

    @Before
    public void setUp() {
        dao = new StubReservationDAO();
        service = new ReservationService(dao);
    }

    @Test
    public void createReservation_throws_whenReservationIsNull() throws Exception {
        try {
            service.createReservation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Reservation is null"));
        }
    }

    @Test
    public void createReservation_throws_whenGuestIdInvalid() throws Exception {
        Reservation r = validReservation();
        r.setGuestId(0);

        try {
            service.createReservation(r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("guest ID"));
        }
    }

    @Test
    public void createReservation_throws_whenDatesMissing() throws Exception {
        Reservation r = validReservation();
        r.setCheckOut(null);

        try {
            service.createReservation(r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Dates required"));
        }
    }

    @Test
    public void createReservation_throws_whenCheckoutNotAfterCheckin() throws Exception {
        Reservation r = validReservation();
        r.setCheckOut(r.getCheckIn());

        try {
            service.createReservation(r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Check-out must be after check-in"));
        }
    }

    @Test
    public void createReservation_throws_whenRoomTypeMissing() throws Exception {
        Reservation r = validReservation();
        r.setRoomTypeId(0);

        try {
            service.createReservation(r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Room type"));
        }
    }

    @Test
    public void createReservation_throws_whenGuestCountInvalid() throws Exception {
        Reservation r = validReservation();
        r.setNumGuests(0);

        try {
            service.createReservation(r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Number of guests"));
        }
    }

    @Test
    public void createReservation_returnsGeneratedId_whenValid() throws Exception {
        dao.createResult = 55;
        Reservation r = validReservation();

        int id = service.createReservation(r);

        assertEquals(55, id);
        assertSame(r, dao.lastCreated);
    }

    @Test
    public void getReservationById_returnsNull_whenIdNotPositive() throws Exception {
        assertNull(service.getReservationById(0));
    }

    @Test
    public void getReservationById_returnsReservation_whenFound() throws Exception {
        Reservation expected = validReservation();
        expected.setReservationId(10);
        dao.findByIdResult = expected;

        Reservation actual = service.getReservationById(10);

        assertSame(expected, actual);
    }

    @Test
    public void updateStatus_throws_whenReservationIdInvalid() throws Exception {
        try {
            service.updateStatus(0, "CONFIRMED");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Invalid reservation id"));
        }
    }

    @Test
    public void updateStatus_delegatesToDao_whenInputsValid() throws Exception {
        service.updateStatus(12, "CONFIRMED");
        assertEquals(12, dao.updatedReservationId);
        assertEquals("CONFIRMED", dao.updatedStatus);
    }

    @Test
    public void cancel_updatesReservationToCancelled_whenRoomIdIsNull() throws Exception {
        dao.findRoomIdResult = null;

        service.cancel(20);

        assertEquals(20, dao.updatedReservationId);
        assertEquals("CANCELLED", dao.updatedStatus);
    }

    @Test
    public void checkIn_throws_whenReservationNotFound() throws Exception {
        dao.findByIdResult = null;

        try {
            service.checkIn(9);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Reservation not found"));
        }
    }

    @Test
    public void checkIn_throws_whenReservationNotConfirmed() throws Exception {
        Reservation r = validReservation();
        r.setStatus("PENDING");
        dao.findByIdResult = r;

        try {
            service.checkIn(9);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Confirm the reservation first"));
        }
    }

    @Test
    public void checkOut_throws_whenReservationNotFound() throws Exception {
        dao.findByIdResult = null;

        try {
            service.checkOut(11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Reservation not found"));
        }
    }

    @Test
    public void checkOut_throws_whenReservationNotCheckedIn() throws Exception {
        Reservation r = validReservation();
        r.setStatus("CONFIRMED");
        dao.findByIdResult = r;

        try {
            service.checkOut(11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Check-out allowed only after CHECKED_IN."));
        }
    }

    private Reservation validReservation() {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setGuestId(1);
        r.setRoomTypeId(1);
        r.setRoomId(1);
        r.setCheckIn(LocalDate.of(2026, 3, 10));
        r.setCheckOut(LocalDate.of(2026, 3, 12));
        r.setNumGuests(2);
        r.setStatus("CONFIRMED");
        return r;
    }

    private static class StubReservationDAO implements ReservationDAO {
        private int createResult;
        private Reservation lastCreated;
        private Reservation findByIdResult;
        private Integer findRoomIdResult;
        private int updatedReservationId;
        private String updatedStatus;

        @Override
        public int create(Reservation reservation) {
            this.lastCreated = reservation;
            return createResult;
        }

        @Override
        public Reservation findById(int id) {
            return findByIdResult;
        }

        @Override
        public ReservationDetails findDetailsById(int reservationId) {
            return null;
        }

        @Override
        public void updateStatus(int reservationId, String status) {
            this.updatedReservationId = reservationId;
            this.updatedStatus = status;
        }

        @Override
        public Integer findRoomId(int reservationId) {
            return findRoomIdResult;
        }

        @Override
        public void updateReservation(Reservation reservation) {
        }
    }
}
