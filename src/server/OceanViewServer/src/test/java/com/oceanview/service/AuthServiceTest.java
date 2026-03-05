package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AuthServiceTest {

    @Test
    public void authenticate_returnsNull_whenUsernameIsNull() throws Exception {
        AuthService service = new AuthService(new StubUserDAO(null));
        assertNull(service.authenticate(null, "secret"));
    }

    @Test
    public void authenticate_returnsNull_whenUsernameIsBlank() throws Exception {
        AuthService service = new AuthService(new StubUserDAO(null));
        assertNull(service.authenticate("   ", "secret"));
    }

    @Test
    public void authenticate_returnsNull_whenPasswordIsNull() throws Exception {
        AuthService service = new AuthService(new StubUserDAO(null));
        assertNull(service.authenticate("admin", null));
    }

    @Test
    public void authenticate_returnsNull_whenUserDoesNotExist() throws Exception {
        AuthService service = new AuthService(new StubUserDAO(null));
        assertNull(service.authenticate("admin", "secret"));
    }

    @Test
    public void authenticate_returnsNull_whenUserIsInactive() throws Exception {
        User user = buildUser("admin", false, "secret");
        AuthService service = new AuthService(new StubUserDAO(user));
        assertNull(service.authenticate("admin", "secret"));
    }

    @Test
    public void authenticate_returnsNull_whenPasswordIsWrong() throws Exception {
        User user = buildUser("admin", true, "secret");
        AuthService service = new AuthService(new StubUserDAO(user));
        assertNull(service.authenticate("admin", "wrong"));
    }

    @Test
    public void authenticate_returnsUser_whenCredentialsAreCorrectAndUsernameTrimmed() throws Exception {
        User user = buildUser("admin", true, "secret");
        StubUserDAO dao = new StubUserDAO(user);
        AuthService service = new AuthService(dao);

        User result = service.authenticate("  admin  ", "secret");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("admin", dao.lastLookupUsername);
    }

    private User buildUser(String username, boolean active, String plainPassword) {
        User user = new User();
        user.setUsername(username);
        user.setActive(active);
        user.setPasswordHash(PasswordUtil.createStoredPassword(plainPassword));
        return user;
    }

    private static class StubUserDAO implements UserDAO {
        private final User user;
        private String lastLookupUsername;

        private StubUserDAO(User user) {
            this.user = user;
        }

        @Override
        public User findByUsername(String username) {
            this.lastLookupUsername = username;
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
            return null;
        }

        @Override public List<User> findAll() { return Collections.emptyList(); }
        @Override public User findById(int userId) { return null; }
        @Override public int create(User u) { return 0; }
        @Override public void update(User u) { }
        @Override public void delete(int userId) { }
    }
}
