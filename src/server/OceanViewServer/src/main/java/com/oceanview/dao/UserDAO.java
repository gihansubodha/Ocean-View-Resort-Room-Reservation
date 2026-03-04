package com.oceanview.dao;

import com.oceanview.model.User;
import java.util.List;

public interface UserDAO {
    User findByUsername(String username) throws Exception;

    // ADMIN CRUD
    List<User> findAll() throws Exception;
    User findById(int userId) throws Exception;
    int create(User u) throws Exception;
    void update(User u) throws Exception;
    void delete(int userId) throws Exception;
}