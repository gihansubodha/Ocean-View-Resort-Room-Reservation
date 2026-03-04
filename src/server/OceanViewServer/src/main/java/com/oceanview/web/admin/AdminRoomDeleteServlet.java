package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomDAO;
import com.oceanview.dao.AdminRoomDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/rooms/delete")
public class AdminRoomDeleteServlet extends HttpServlet {

    private final AdminRoomDAO dao = new AdminRoomDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = 0;
        try { id = Integer.parseInt(req.getParameter("id")); } catch (Exception ignored) {}

        try {
            dao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/rooms");
        } catch (Exception e) {
            throw new ServletException("Delete room failed", e);
        }
    }
}