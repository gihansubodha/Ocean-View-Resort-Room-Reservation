package com.oceanview.dao;

import com.oceanview.model.AdminDashboardStats;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AdminDashboardDAO {
    AdminDashboardStats getStats(LocalDate start, LocalDate end) throws Exception;

    // Graph 1: Highest occupied days in range (date -> checked-in count / occupancy proxy)
    List<Map<String, Object>> getBusyDays(LocalDate start, LocalDate end) throws Exception;

    // Graph 2: Most chosen room types (room type name -> count)
    List<Map<String, Object>> getPopularRoomTypes(LocalDate start, LocalDate end) throws Exception;
}