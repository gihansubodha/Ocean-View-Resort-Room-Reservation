package com.oceanview.dao;

import com.oceanview.model.AdminDashboardStats;
import com.oceanview.util.DbUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

public class AdminDashboardDAOImpl implements AdminDashboardDAO {

    @Override
    public AdminDashboardStats getStats(LocalDate start, LocalDate end) throws Exception {
        AdminDashboardStats s = new AdminDashboardStats();

        LocalDate sDate = (start != null) ? start : LocalDate.now().minusDays(30);
        LocalDate eDate = (end != null) ? end : LocalDate.now();

        try (Connection con = DbUtil.getConnection()) {

            // TODAY: total reservations where check_in = today
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE check_in = CURDATE()"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayReservations(rs.getInt(1));
                }
            }

            // TODAY: checked-in today
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE status='CHECKED_IN' AND check_in = CURDATE()"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayCheckedIn(rs.getInt(1));
                }
            }

            // TODAY: guests in-house today (sum num_guests for active stays)
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COALESCE(SUM(num_guests),0) " +
                            "FROM reservations " +
                            "WHERE status='CHECKED_IN' " +
                            "AND CURDATE() >= check_in AND CURDATE() < check_out"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayGuestsInHotel(rs.getInt(1));
                }
            }

            // TODAY: full payments paid today (sum total_amount for bills marked PAID today)
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COALESCE(SUM(total_amount),0) " +
                            "FROM bills " +
                            "WHERE bill_status='PAID' AND DATE(created_at)=CURDATE()"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayFullPaymentsTotal(rs.getBigDecimal(1));
                }
            }

            // TODAY: outstanding balances of CHECK-OUT is today
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COALESCE(SUM(b.balance),0) " +
                            "FROM bills b " +
                            "JOIN reservations r ON r.reservation_id = b.reservation_id " +
                            "WHERE r.check_out = CURDATE() AND b.balance > 0"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayOutstandingBalanceTotal(rs.getBigDecimal(1));
                }
            }

            // TODAY: arrivals today not checked-in
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations " +
                            "WHERE check_in = CURDATE() AND status <> 'CHECKED_IN'"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setTodayArrivalsNotCheckedIn(rs.getInt(1));
                }
            }

            // RANGE: total reservations by check_in date
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE check_in BETWEEN ? AND ?"
            )) {
                ps.setDate(1, java.sql.Date.valueOf(sDate));
                ps.setDate(2, java.sql.Date.valueOf(eDate));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setRangeReservations(rs.getInt(1));
                }
            }

            // RANGE: checked-in reservations by check_in date
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE status='CHECKED_IN' AND check_in BETWEEN ? AND ?"
            )) {
                ps.setDate(1, java.sql.Date.valueOf(sDate));
                ps.setDate(2, java.sql.Date.valueOf(eDate));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setRangeCheckedIn(rs.getInt(1));
                }
            }

            // RANGE: cancelled reservations by check_in date
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE status='CANCELLED' AND check_in BETWEEN ? AND ?"
            )) {
                ps.setDate(1, java.sql.Date.valueOf(sDate));
                ps.setDate(2, java.sql.Date.valueOf(eDate));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setRangeCancelled(rs.getInt(1));
                }
            }

            // RANGE: total paid in range (payments table)
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COALESCE(SUM(p.paid_amount),0) " +
                            "FROM payments p " +
                            "WHERE DATE(p.paid_at) BETWEEN ? AND ?"
            )) {
                ps.setDate(1, java.sql.Date.valueOf(sDate));
                ps.setDate(2, java.sql.Date.valueOf(eDate));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) s.setRangeTotalPaid(rs.getBigDecimal(1));
                }
            }

            if (s.getTodayFullPaymentsTotal() == null) s.setTodayFullPaymentsTotal(BigDecimal.ZERO);
            if (s.getTodayOutstandingBalanceTotal() == null) s.setTodayOutstandingBalanceTotal(BigDecimal.ZERO);
            if (s.getRangeTotalPaid() == null) s.setRangeTotalPaid(BigDecimal.ZERO);
        }

        return s;
    }

    // Busy days (last 30 days)
    @Override
    public List<Map<String, Object>> getBusyDays(LocalDate ignoredStart, LocalDate ignoredEnd) throws Exception {
        LocalDate sDate = LocalDate.now().minusDays(29);
        LocalDate eDate = LocalDate.now();

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT check_in AS day, COUNT(*) AS cnt " +
                             "FROM reservations " +
                             "WHERE status='CHECKED_IN' AND check_in BETWEEN ? AND ? " +
                             "GROUP BY check_in ORDER BY check_in"
             )) {

            ps.setDate(1, java.sql.Date.valueOf(sDate));
            ps.setDate(2, java.sql.Date.valueOf(eDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("day", rs.getDate("day").toString());
                    row.put("value", rs.getInt("cnt"));
                    list.add(row);
                }
            }
        }
        return list;
    }

    // Most chosen room types (last 30 days)
    @Override
    public List<Map<String, Object>> getPopularRoomTypes(LocalDate ignoredStart, LocalDate ignoredEnd) throws Exception {
        LocalDate sDate = LocalDate.now().minusDays(29);
        LocalDate eDate = LocalDate.now();

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT rt.type_name AS label, COUNT(*) AS cnt " +
                             "FROM reservations r " +
                             "JOIN room_types rt ON rt.room_type_id = r.room_type_id " +
                             "WHERE r.check_in BETWEEN ? AND ? " +
                             "GROUP BY rt.type_name " +
                             "ORDER BY cnt DESC " +
                             "LIMIT 10"
             )) {

            ps.setDate(1, java.sql.Date.valueOf(sDate));
            ps.setDate(2, java.sql.Date.valueOf(eDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getInt("cnt"));
                    list.add(row);
                }
            }
        }
        return list;
    }
}