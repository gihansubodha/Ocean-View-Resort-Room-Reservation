package com.oceanview.dao;

import com.oceanview.model.Guest;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GuestDAOImplAdditionalTest {
    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void findByNicPassport_returnsNull_whenMissing() throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address FROM guests WHERE nic_passport = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        assertNull(new GuestDAOImpl().findByNicPassport("MISS"));
        assertEquals("MISS", ps.params.get(1));
    }

    @Test
    public void findById_returnsMappedGuest_whenFound() throws Exception {
        String sql = "SELECT guest_id, first_name, last_name, nic_passport, phone, email, address FROM guests WHERE guest_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("guest_id", 3, "first_name", "Nimal", "last_name", "Perera", "nic_passport", "NIC9", "phone", "070", "email", "n@test.com", "address", "Galle")
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Guest result = new GuestDAOImpl().findById(3);

        assertNotNull(result);
        assertEquals("Nimal", result.getFirstName());
        assertEquals("Galle", result.getAddress());
    }

    @Test
    public void insert_throws_whenGeneratedKeyMissing() throws Exception {
        String sql = "INSERT INTO guests (first_name, last_name, nic_passport, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        Guest guest = new Guest();
        guest.setFirstName("No");
        guest.setLastName("Key");

        try {
            new GuestDAOImpl().insert(guest);
            fail("Expected Exception");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("Failed to insert guest"));
        }
    }
}
