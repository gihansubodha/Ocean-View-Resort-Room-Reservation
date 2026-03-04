package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomDAO;
import com.oceanview.dao.AdminRoomDAOImpl;
import com.oceanview.dao.AdminRoomTypeDAO;
import com.oceanview.dao.AdminRoomTypeDAOImpl;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet({"/admin/rooms/add", "/admin/rooms/edit"})
public class AdminRoomFormServlet extends HttpServlet {

    private final AdminRoomDAO roomDAO = new AdminRoomDAOImpl();
    private final AdminRoomTypeDAO roomTypeDAO = new AdminRoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            loadTypes(req);

            if (req.getServletPath().endsWith("/edit")) {
                int id = parseInt(req.getParameter("id"));
                Room r = roomDAO.findById(id);
                req.setAttribute("r", r);
            }

            req.getRequestDispatcher("/WEB-INF/admin/rooms_form.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Room form load failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean edit = req.getServletPath().endsWith("/edit");

        int roomId = parseInt(req.getParameter("roomId"));
        String roomNumber = safe(req.getParameter("roomNumber")); // ✅ keep String
        int roomTypeId = parseInt(req.getParameter("roomTypeId"));
        String status = safe(req.getParameter("status"));

        try {
            // basic validation (prevents blank room numbers)
            if (roomNumber.isEmpty()) throw new IllegalArgumentException("Room number is required");
            if (roomTypeId <= 0) throw new IllegalArgumentException("Room type is required");
            if (status.isEmpty()) throw new IllegalArgumentException("Status is required");

            Room r = edit ? roomDAO.findById(roomId) : new Room();
            if (r == null) throw new IllegalArgumentException("Room not found");

            r.setRoomNumber(roomNumber);
            r.setRoomTypeId(roomTypeId);
            r.setStatus(status);

            if (edit) roomDAO.update(r);
            else roomDAO.create(r);

            resp.sendRedirect(req.getContextPath() + "/admin/rooms");
        } catch (Exception e) {
            try {
                loadTypes(req);
                // keep values on error
                Room tmp = new Room();
                tmp.setRoomId(roomId);
                tmp.setRoomNumber(roomNumber);
                tmp.setRoomTypeId(roomTypeId);
                tmp.setStatus(status);
                req.setAttribute("r", tmp);
            } catch (Exception ignored) { }

            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/admin/rooms_form.jsp").forward(req, resp);
        }
    }

    private void loadTypes(HttpServletRequest req) throws Exception {
        List<RoomType> types = roomTypeDAO.findAll();
        req.setAttribute("types", types);
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}