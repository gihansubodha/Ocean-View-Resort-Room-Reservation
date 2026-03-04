package com.oceanview.dao;

import com.oceanview.model.Guest;

public interface GuestDAO {
    Guest findByNicPassport(String nicPassport) throws Exception;
    Guest findById(int guestId) throws Exception;
    int insert(Guest guest) throws Exception;
}