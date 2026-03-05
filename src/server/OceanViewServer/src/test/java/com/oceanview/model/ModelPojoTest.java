package com.oceanview.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class ModelPojoTest {

    @Test
    public void bill_gettersAndSetters_work() {
        Bill b = new Bill();
        b.setBillId(1); b.setReservationId(2); b.setNights(3); b.setNightlyRate(new BigDecimal("100.00"));
        b.setSubtotal(new BigDecimal("300.00")); b.setDiscountAmount(new BigDecimal("10.00")); b.setTaxAmount(new BigDecimal("5.00"));
        b.setTotal(new BigDecimal("295.00")); b.setPaidTotal(new BigDecimal("100.00")); b.setBalance(new BigDecimal("195.00")); b.setBillStatus("PARTIAL");
        assertEquals(1, b.getBillId()); assertEquals(2, b.getReservationId()); assertEquals(3, b.getNights()); assertEquals("PARTIAL", b.getBillStatus());
    }

    @Test
    public void guest_gettersAndSetters_work() {
        Guest g = new Guest();
        g.setGuestId(7); g.setFirstName("Ana"); g.setLastName("Silva"); g.setNicPassport("NIC"); g.setPhone("070"); g.setEmail("a@test.com"); g.setAddress("Colombo");
        assertEquals("Ana", g.getFirstName()); assertEquals("Colombo", g.getAddress());
    }

    @Test
    public void reservation_gettersAndSetters_work() {
        Reservation r = new Reservation();
        r.setReservationId(4); r.setReservationCode("RES-4"); r.setGuestId(1); r.setRoomTypeId(2); r.setRoomId(3);
        r.setCheckIn(LocalDate.of(2026,3,10)); r.setCheckOut(LocalDate.of(2026,3,12)); r.setNumGuests(2); r.setStatus("CONFIRMED"); r.setSpecialRequests("Late"); r.setCreatedBy(9);
        assertEquals("RES-4", r.getReservationCode()); assertEquals(Integer.valueOf(3), r.getRoomId()); assertEquals(Integer.valueOf(9), r.getCreatedBy());
    }

    @Test
    public void reservationDetails_gettersAndSetters_work() {
        ReservationDetails d = new ReservationDetails();
        d.setReservationId(8); d.setReservationCode("RES-8"); d.setCheckIn(LocalDate.of(2026,3,10)); d.setCheckOut(LocalDate.of(2026,3,12)); d.setNumGuests(2);
        d.setReservationStatus("CHECKED_IN"); d.setSpecialRequests("None"); d.setGuestId(1); d.setGuestFirstName("Joe"); d.setGuestLastName("Perera"); d.setGuestPhone("071"); d.setGuestEmail("j@test.com"); d.setGuestNicPassport("NIC7");
        d.setRoomId(2); d.setRoomNumber("A1"); d.setRoomTypeId(3); d.setRoomTypeName("Suite"); d.setNightlyRate(250.0);
        assertEquals("Joe", d.getGuestFirstName()); assertEquals("Suite", d.getRoomTypeName()); assertEquals(250.0, d.getNightlyRate(), 0.0);
    }

    @Test
    public void room_defaultAndParameterizedConstructors_work() {
        Room a = new Room();
        a.setRoomId(1); a.setRoomNumber("101"); a.setRoomTypeId(2); a.setRoomTypeName("Deluxe"); a.setStatus("AVAILABLE");
        Room b = new Room(2, "202", 3, "Suite", "OCCUPIED");
        assertEquals("Deluxe", a.getRoomTypeName()); assertEquals("202", b.getRoomNumber()); assertEquals("OCCUPIED", b.getStatus());
    }

    @Test
    public void roomType_gettersAndSetters_work() {
        RoomType rt = new RoomType();
        rt.setRoomTypeId(3); rt.setTypeName("Suite"); rt.setDescription("Sea view"); rt.setNightlyRate(new BigDecimal("300.00")); rt.setMaxGuests(4); rt.setActive(true);
        assertEquals("Suite", rt.getTypeName()); assertTrue(rt.isActive());
    }

    @Test
    public void user_defaultAndParameterizedConstructors_work() {
        Timestamp ts = Timestamp.valueOf("2026-03-06 10:00:00");
        User a = new User();
        a.setUserId(1); a.setUsername("admin"); a.setPasswordHash("salt:hash"); a.setRole("ADMIN"); a.setActive(true); a.setCreatedAt(ts);
        User b = new User(2, "staff", "x:y", "STAFF", false, ts);
        assertEquals("admin", a.getUsername()); assertEquals("STAFF", b.getRole()); assertFalse(b.isActive());
    }

    @Test
    public void adminDashboardStats_gettersAndSetters_work() {
        AdminDashboardStats s = new AdminDashboardStats();
        s.setTodayReservations(5); s.setTodayCheckedIn(2); s.setTodayGuestsInHotel(9); s.setTodayFullPaymentsTotal(new BigDecimal("500.00"));
        s.setTodayOutstandingBalanceTotal(new BigDecimal("120.00")); s.setTodayArrivalsNotCheckedIn(1); s.setRangeReservations(20); s.setRangeCheckedIn(11); s.setRangeCancelled(3); s.setRangeTotalPaid(new BigDecimal("2500.00"));
        assertEquals(5, s.getTodayReservations()); assertEquals(new BigDecimal("120.00"), s.getTodayOutstandingBalanceTotal()); assertEquals(3, s.getRangeCancelled());
    }

    @Test
    public void payment_canBeInstantiated() {
        assertNotNull(new Payment());
    }
}
