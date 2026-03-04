package com.oceanview.dao;

import com.oceanview.model.User;
import com.oceanview.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                return map(rs);
            }
        }
    }

    @Override
    public List<User> findAll() throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at " +
                "FROM users ORDER BY user_id DESC";
        List<User> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public User findById(int userId) throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, is_active, created_at " +
                "FROM users WHERE user_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    @Override
    public int create(User u) throws Exception {
        String sql = "INSERT INTO users (username, password_hash, role, is_active) VALUES (?,?,?,?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.isActive() ? 1 : 0);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void update(User u) throws Exception {
        String sql = "UPDATE users SET username=?, password_hash=?, role=?, is_active=? WHERE user_id=?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.isActive() ? 1 : 0);
            ps.setInt(5, u.getUserId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int userId) throws Exception {
        String sql = "DELETE FROM users WHERE user_id=?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private User map(ResultSet rs) throws Exception {
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