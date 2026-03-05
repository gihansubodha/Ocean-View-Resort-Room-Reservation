package com.oceanview.dao;

import com.oceanview.model.Bill;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class BillDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void create_returnsGeneratedId_andWritesAmounts() throws Exception {
        String sql = "INSERT INTO bills (reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.generatedKeys = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row("id", 41)));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Bill bill = new Bill();
        bill.setReservationId(5);
        bill.setNights(2);
        bill.setNightlyRate(new BigDecimal("120.00"));
        bill.setSubtotal(new BigDecimal("240.00"));
        bill.setDiscountAmount(BigDecimal.ZERO);
        bill.setTaxAmount(BigDecimal.ZERO);
        bill.setTotal(new BigDecimal("240.00"));

        int id = new BillDAOImpl().create(bill);

        assertEquals(41, id);
        assertEquals(5, ps.params.get(1));
        assertEquals(new BigDecimal("240.00"), ps.params.get(7));
    }

    @Test
    public void addPayment_throwsWhenMethodInvalid() throws Exception {
        try {
            new BillDAOImpl().addPayment(1, 100.0, "CHEQUE");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Invalid payment method"));
        }
    }

    @Test
    public void addPayment_callsStoredProcedure_whenInputsValid() throws Exception {
        String sql = "{CALL sp_add_payment(?,?,?)}";
        DbTestSupport.StatementStub cs = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> callable = new HashMap<>();
        callable.put(sql, cs);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(Collections.emptyMap(), callable)));

        new BillDAOImpl().addPayment(9, 150.0, "card");

        assertEquals(9, cs.params.get(1));
        assertEquals(new BigDecimal("150.0"), cs.params.get(2));
        assertEquals("CARD", cs.params.get(3));
        assertTrue(cs.executed);
    }

    @Test
    public void findByReservationId_returnsMappedBillWithPaymentSummary() throws Exception {
        String sql = "SELECT bill_id, reservation_id, nights, rate_per_night, subtotal, discount_amount, tax_amount, total_amount, paid_total, balance, bill_status FROM bills WHERE reservation_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("bill_id", 4, "reservation_id", 8, "nights", 3, "rate_per_night", new BigDecimal("100.00"), "subtotal", new BigDecimal("300.00"), "discount_amount", BigDecimal.ZERO, "tax_amount", BigDecimal.ZERO, "total_amount", new BigDecimal("300.00"), "paid_total", new BigDecimal("100.00"), "balance", new BigDecimal("200.00"), "bill_status", "PARTIAL")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Bill result = new BillDAOImpl().findByReservationId(8);

        assertNotNull(result);
        assertEquals(4, result.getBillId());
        assertEquals(new BigDecimal("200.00"), result.getBalance());
        assertEquals("PARTIAL", result.getBillStatus());
    }
}
