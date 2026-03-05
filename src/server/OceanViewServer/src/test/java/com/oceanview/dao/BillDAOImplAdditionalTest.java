package com.oceanview.dao;

import com.oceanview.model.Bill;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BillDAOImplAdditionalTest {
    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void create_throws_whenGeneratedKeyMissing() throws Exception {
        String sql = "INSERT INTO bills (reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Bill bill = new Bill();
        bill.setReservationId(1);
        bill.setNights(1);
        bill.setNightlyRate(new BigDecimal("100.00"));
        bill.setSubtotal(new BigDecimal("100.00"));
        bill.setDiscountAmount(BigDecimal.ZERO);
        bill.setTaxAmount(BigDecimal.ZERO);
        bill.setTotal(new BigDecimal("100.00"));

        try {
            new BillDAOImpl().create(bill);
            fail("Expected Exception");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("Failed to create bill"));
        }
    }

    @Test
    public void generateBill_throws_whenReservationIdInvalid() throws Exception {
        try {
            new BillDAOImpl().generateBill(0);
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

        new BillDAOImpl().generateBill(77);

        assertEquals(77, cs.params.get(1));
        assertTrue(cs.executed);
    }

    @Test
    public void addPayment_throws_whenBillIdInvalid() throws Exception {
        try {
            new BillDAOImpl().addPayment(0, 10.0, "CASH");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid bill ID", ex.getMessage());
        }
    }

    @Test
    public void addPayment_throws_whenAmountInvalid() throws Exception {
        try {
            new BillDAOImpl().addPayment(1, 0.0, "CASH");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Amount must be > 0", ex.getMessage());
        }
    }

    @Test
    public void addPayment_throws_whenMethodBlank() throws Exception {
        try {
            new BillDAOImpl().addPayment(1, 10.0, "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Payment method required", ex.getMessage());
        }
    }

    @Test
    public void findByReservationId_returnsNull_whenNoRowExists() throws Exception {
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Bill result = new BillDAOImpl().findByReservationId(42);

        assertNull(result);
        assertEquals(42, ps.params.get(1));
    }

    @Test
    public void findByReservationId_propagatesSqlException_whenQueryFails() throws Exception {
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.onExecuteQuery = new SQLException("bill query failed");
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        try {
            new BillDAOImpl().findByReservationId(42);
            fail("Expected SQLException");
        } catch (SQLException ex) {
            assertEquals("bill query failed", ex.getMessage());
        }
    }
}
