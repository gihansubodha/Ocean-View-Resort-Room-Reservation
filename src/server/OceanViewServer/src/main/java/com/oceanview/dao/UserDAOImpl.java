package com.oceanview.dao;

import com.oceanview.model.User;
import com.oceanview.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at " +
                     "FROM users WHERE username = ? LIMIT 1";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                u.setActive(rs.getInt("is_active") == 1);
                u.setCreatedAt(rs.getTimestamp("created_at"));
                return u;
            }
        }
    }
}
