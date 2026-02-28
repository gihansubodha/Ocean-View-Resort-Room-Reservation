package com.oceanview.dao;

import com.oceanview.model.User;

public interface UserDAO {
    User findByUsername(String username) throws Exception;
}
