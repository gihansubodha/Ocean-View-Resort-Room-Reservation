package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String username, String plainPassword) throws Exception {
        if (username == null || username.trim().isEmpty()) return null;
        if (plainPassword == null || plainPassword.isEmpty()) return null;

        User user = userDAO.findByUsername(username.trim());
        if (user == null) return null;
        if (!user.isActive()) return null;

        boolean ok = PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash());
        return ok ? user : null;
    }
}
