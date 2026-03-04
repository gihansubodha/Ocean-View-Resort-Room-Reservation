package com.oceanview.web;

import com.oceanview.dao.AdminDashboardDAO;
import com.oceanview.dao.AdminDashboardDAOImpl;
import com.oceanview.model.AdminDashboardStats;
import com.oceanview.model.User;
import com.oceanview.security.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final AdminDashboardDAO dao = new AdminDashboardDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User authUser = (session == null) ? null : (User) session.getAttribute("authUser");

        if (authUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Please login");
            return;
        }
        if (!Roles.ADMIN.equalsIgnoreCase(authUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/app/home.jsp?error=Access denied");
            return;
        }

        LocalDate start = parseDate(req.getParameter("start"));
        LocalDate end = parseDate(req.getParameter("end"));

        try {
            AdminDashboardStats stats = dao.getStats(start, end);
            List<Map<String, Object>> busyDays = dao.getBusyDays(start, end);
            List<Map<String, Object>> popularRoomTypes = dao.getPopularRoomTypes(start, end);

            req.setAttribute("stats", stats);
            req.setAttribute("start", (start != null) ? start.toString() : "");
            req.setAttribute("end", (end != null) ? end.toString() : "");
            req.setAttribute("busyDays", busyDays);
            req.setAttribute("popularRoomTypes", popularRoomTypes);

            // IMPORTANT: your screenshot shows admin_dashboard.jsp is under /webapp/app/
            req.getRequestDispatcher("/app/admin_dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Admin dashboard load failed", e);
        }
    }

    private LocalDate parseDate(String s) {
        try {
            if (s == null || s.trim().isEmpty()) return null;
            return LocalDate.parse(s.trim());
        } catch (Exception ex) {
            return null;
        }
    }
}