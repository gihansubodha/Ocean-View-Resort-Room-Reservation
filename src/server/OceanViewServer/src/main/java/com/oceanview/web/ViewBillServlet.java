package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.ReservationDetails;
import com.oceanview.util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/bills/view")
public class ViewBillServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reservationIdParam = request.getParameter("reservationId");

            // ✅ Dashboard mode
            if (reservationIdParam == null || reservationIdParam.isBlank()) {
                loadTodayDashboard(request);
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            int reservationId = Integer.parseInt(reservationIdParam);
            ReservationDetails d = reservationDAO.findDetailsById(reservationId);

            if (d == null) {
                request.setAttribute("error", "No reservation found for ID: " + reservationId);
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            billDAO.generateBill(reservationId);

            Bill bill = billDAO.findByReservationId(reservationId);
            if (bill == null) {
                request.setAttribute("error", "Bill not found for reservation ID: " + reservationId);
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            request.setAttribute("details", d);
            request.setAttribute("bill", bill);

            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
        }
    }

    private void loadTodayDashboard(HttpServletRequest request) throws Exception {
        List<String[]> todaysPayments = new ArrayList<>();
        List<String[]> todaysUnpaid = new ArrayList<>();

        try (Connection con = DbUtil.getConnection()) {

            String psql = "SELECT p.payment_id, p.bill_id, p.paid_amount, p.paid_method, p.paid_at " +
                    "FROM payments p WHERE DATE(p.paid_at) = CURDATE() ORDER BY p.paid_at DESC";
            try (PreparedStatement ps = con.prepareStatement(psql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    todaysPayments.add(new String[]{
                            String.valueOf(rs.getInt("payment_id")),
                            String.valueOf(rs.getInt("bill_id")),
                            rs.getBigDecimal("paid_amount").toPlainString(),
                            rs.getString("paid_method"),
                            String.valueOf(rs.getTimestamp("paid_at"))
                    });
                }
            }

            String usql = "SELECT r.reservation_id, r.reservation_code, r.status, rm.room_number, b.bill_status, b.balance " +
                    "FROM reservations r " +
                    "LEFT JOIN rooms rm ON r.room_id = rm.room_id " +
                    "LEFT JOIN bills b ON b.reservation_id = r.reservation_id " +
                    "WHERE (b.bill_status IN ('UNPAID','PARTIAL') OR b.bill_status IS NULL) " +
                    "AND r.status IN ('PENDING','CONFIRMED','CHECKED_IN') " +
                    "ORDER BY r.reservation_id DESC";
            try (PreparedStatement ps = con.prepareStatement(usql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    todaysUnpaid.add(new String[]{
                            String.valueOf(rs.getInt("reservation_id")),
                            rs.getString("reservation_code"),
                            rs.getString("status"),
                            rs.getString("room_number"),
                            rs.getString("bill_status"),
                            (rs.getBigDecimal("balance") == null ? "0.00" : rs.getBigDecimal("balance").toPlainString())
                    });
                }
            }
        }

        request.setAttribute("dashboardMode", true);
        request.setAttribute("todaysPayments", todaysPayments);
        request.setAttribute("todaysUnpaid", todaysUnpaid);
    }
}