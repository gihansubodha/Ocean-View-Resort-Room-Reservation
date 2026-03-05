package com.oceanview.dao;

import com.oceanview.model.User;
import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UserDAOImplAdditionalTest {
    @After
    public void tearDown() throws Exception { DbTestSupport.clearDataSource(); }

    @Test
    public void findByUsername_returnsNull_whenMissing() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users WHERE username = ? LIMIT 1";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        assertNull(new UserDAOImpl().findByUsername("ghost"));
        assertEquals("ghost", ps.params.get(1));
    }

    @Test
    public void findById_returnsMappedUser_whenFound() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users WHERE user_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Timestamp ts = Timestamp.valueOf("2026-03-06 09:00:00");
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("user_id", 4, "username", "staff2", "password_hash", "s:h", "role", "STAFF", "is_active", 1, "created_at", ts)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        User result = new UserDAOImpl().findById(4);

        assertNotNull(result);
        assertEquals("staff2", result.getUsername());
        assertTrue(result.isActive());
        assertEquals(ts, result.getCreatedAt());
    }

    @Test
    public void findById_returnsNull_whenMissing() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users WHERE user_id = ?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        assertNull(new UserDAOImpl().findById(404));
    }

    @Test
    public void update_executesUpdateWithAllFields() throws Exception {
        String sql = "UPDATE users SET username=?, password_hash=?, role=?, is_active=? WHERE user_id=?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        User user = new User();
        user.setUserId(8);
        user.setUsername("manager");
        user.setPasswordHash("salt:hash");
        user.setRole("ADMIN");
        user.setActive(false);

        new UserDAOImpl().update(user);

        assertEquals("manager", ps.params.get(1));
        assertEquals("salt:hash", ps.params.get(2));
        assertEquals("ADMIN", ps.params.get(3));
        assertEquals(0, ps.params.get(4));
        assertEquals(8, ps.params.get(5));
        assertTrue(ps.executed);
    }

    @Test
    public void create_returnsZero_whenGeneratedKeyMissing() throws Exception {
        String sql = "INSERT INTO users (username, password_hash, role, is_active) VALUES (?,?,?,?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        User user = new User();
        user.setUsername("nokey");
        user.setPasswordHash("h");
        user.setRole("STAFF");
        user.setActive(false);

        assertEquals(0, new UserDAOImpl().create(user));
    }

    @Test
    public void findAll_propagatesSqlException_whenQueryFails() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users ORDER BY user_id DESC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.onExecuteQuery = new SQLException("user list failed");
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        try {
            new UserDAOImpl().findAll();
            fail("Expected SQLException");
        } catch (SQLException ex) {
            assertEquals("user list failed", ex.getMessage());
        }
    }
}
