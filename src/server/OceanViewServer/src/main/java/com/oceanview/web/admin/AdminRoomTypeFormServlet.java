package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomTypeDAO;
import com.oceanview.dao.AdminRoomTypeDAOImpl;
import com.oceanview.model.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet({"/admin/room-types/add", "/admin/room-types/edit"})
public class AdminRoomTypeFormServlet extends HttpServlet {

    private final AdminRoomTypeDAO dao = new AdminRoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getServletPath().endsWith("/edit")) {
                int id = parseInt(req.getParameter("id"));
                RoomType rt = dao.findById(id);
                req.setAttribute("rt", rt);
            }
            req.getRequestDispatcher("/WEB-INF/admin/room_types_form.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Room type form load failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean edit = req.getServletPath().endsWith("/edit");

        int id = parseInt(req.getParameter("roomTypeId"));
        String typeName = req.getParameter("typeName");
        String description = req.getParameter("description");
        BigDecimal nightlyRate = parseBig(req.getParameter("nightlyRate"));
        int maxGuests = parseInt(req.getParameter("maxGuests"));
        boolean active = "1".equals(req.getParameter("isActive"));

        try {
            RoomType rt = edit ? dao.findById(id) : new RoomType();
            if (rt == null) throw new IllegalArgumentException("Room type not found");

            rt.setTypeName(typeName);
            rt.setDescription(description);
            rt.setNightlyRate(nightlyRate);
            rt.setMaxGuests(maxGuests);
            rt.setActive(active);

            if (edit) dao.update(rt);
            else dao.create(rt);

            resp.sendRedirect(req.getContextPath() + "/admin/room-types");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private BigDecimal parseBig(String s) { try { return new BigDecimal(s); } catch (Exception e) { return BigDecimal.ZERO; } }
}