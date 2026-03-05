package com.oceanview.dao;

import com.oceanview.model.User;
import org.junit.After;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

public class UserDAOImplTest {

    @After
    public void tearDown() throws Exception {
        DbTestSupport.clearDataSource();
    }

    @Test
    public void findByUsername_returnsMappedUser_whenFound() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users WHERE username = ? LIMIT 1";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Timestamp now = Timestamp.valueOf("2026-03-05 08:00:00");
        ps.queryResult = DbTestSupport.rows(Collections.singletonList(
                DbTestSupport.row("user_id", 9, "username", "admin", "password_hash", "x:y", "role", "ADMIN", "is_active", 1, "created_at", now)
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        User result = new UserDAOImpl().findByUsername("admin");

        assertNotNull(result);
        assertEquals(9, result.getUserId());
        assertEquals("ADMIN", result.getRole());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    public void findAll_returnsMappedList() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at FROM users ORDER BY user_id DESC";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.queryResult = DbTestSupport.rows(Arrays.asList(
                DbTestSupport.row("user_id", 2, "username", "staff", "password_hash", "a:b", "role", "STAFF", "is_active", 1, "created_at", Timestamp.valueOf("2026-03-05 08:00:00")),
                DbTestSupport.row("user_id", 1, "username", "admin", "password_hash", "c:d", "role", "ADMIN", "is_active", 0, "created_at", Timestamp.valueOf("2026-03-04 08:00:00"))
        ));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        List<User> result = new UserDAOImpl().findAll();

        assertEquals(2, result.size());
        assertEquals("staff", result.get(0).getUsername());
        assertFalse(result.get(1).isActive());
    }

    @Test
    public void create_returnsGeneratedId_andWritesParameters() throws Exception {
        String sql = "INSERT INTO users (username, password_hash, role, is_active) VALUES (?,?,?,?)";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        ps.generatedKeys = DbTestSupport.rows(Collections.singletonList(DbTestSupport.row("id", 6)));
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        User user = new User();
        user.setUsername("newuser");
        user.setPasswordHash("hash");
        user.setRole("STAFF");
        user.setActive(true);

        int id = new UserDAOImpl().create(user);

        assertEquals(6, id);
        assertEquals("newuser", ps.params.get(1));
        assertEquals(1, ps.params.get(4));
    }

    @Test
    public void delete_executesUpdateWithUserId() throws Exception {
        String sql = "DELETE FROM users WHERE user_id=?";
        DbTestSupport.StatementStub ps = DbTestSupport.statement();
        Map<String, DbTestSupport.StatementStub> prepared = new HashMap<>();
        prepared.put(sql, ps);
        DbTestSupport.installDataSource(DbTestSupport.dataSource(DbTestSupport.connection(prepared, Collections.emptyMap())));

        new UserDAOImpl().delete(12);

        assertEquals(12, ps.params.get(1));
        assertTrue(ps.executed);
    }
}
