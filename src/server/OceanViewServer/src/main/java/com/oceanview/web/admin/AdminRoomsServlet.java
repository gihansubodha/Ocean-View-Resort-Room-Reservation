package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomDAO;
import com.oceanview.dao.AdminRoomDAOImpl;
import com.oceanview.model.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/rooms")
public class AdminRoomsServlet extends HttpServlet {

    private final AdminRoomDAO dao = new AdminRoomDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Room> rooms = dao.findAll();
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/WEB-INF/admin/rooms_list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Load rooms failed", e);
        }
    }
}