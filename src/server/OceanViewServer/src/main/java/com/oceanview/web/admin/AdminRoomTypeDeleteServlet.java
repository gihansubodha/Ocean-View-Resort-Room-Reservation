package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomTypeDAO;
import com.oceanview.dao.AdminRoomTypeDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/room-types/delete")
public class AdminRoomTypeDeleteServlet extends HttpServlet {

    private final AdminRoomTypeDAO dao = new AdminRoomTypeDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = 0;
        try { id = Integer.parseInt(req.getParameter("id")); } catch (Exception ignored) {}

        try {
            dao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/room-types");
        } catch (Exception e) {
            throw new ServletException("Delete room type failed", e);
        }
    }
}