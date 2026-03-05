package com.oceanview.dao;

import com.oceanview.model.Guest;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GuestDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void findByNicPassport_returnsMappedGuest_whenRowExists() throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address FROM guests WHERE nic_passport = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("guest_id", 7, "first_name", "Gihan", "last_name", "Silva", "nic_passport", "NIC123", "phone", "071", "email", "g@test.com", "address", "Colombo")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Guest result = new GuestDAOImpl().findByNicPassport("NIC123");

        assertNotNull(result);
        assertEquals(7, result.getGuestId());
        assertEquals("Gihan", result.getFirstName());
        assertEquals("NIC123", ps.params.get(1));
    }

    @Test
    public void findById_returnsNull_whenNoRowExists() throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address FROM guests WHERE guest_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Guest result = new GuestDAOImpl().findById(99);

        assertNull(result);
        assertEquals(99, ps.params.get(1));
    }

    @Test
    public void insert_returnsGeneratedKey_andWritesAllFields() throws Exception {
        String sql = "INSERT INTO guests (first_name, last_name, nic_passport, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.generatedKeys = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row("id", 15)));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Guest guest = new Guest();
        guest.setFirstName("Ana");
        guest.setLastName("Perera");
        guest.setNicPassport("P123");
        guest.setPhone("0771234567");
        guest.setEmail("ana@test.com");
        guest.setAddress("Kandy");

        int id = new GuestDAOImpl().insert(guest);

        assertEquals(15, id);
        assertEquals("Ana", ps.params.get(1));
        assertEquals("Kandy", ps.params.get(6));
        assertTrue(ps.executed);
    }
}
