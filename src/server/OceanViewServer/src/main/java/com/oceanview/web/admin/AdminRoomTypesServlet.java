package com.oceanview.web.admin;

import com.oceanview.dao.AdminRoomTypeDAO;
import com.oceanview.dao.AdminRoomTypeDAOImpl;
import com.oceanview.model.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/room-types")
public class AdminRoomTypesServlet extends HttpServlet {

    private final AdminRoomTypeDAO dao = new AdminRoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<RoomType> list = dao.findAll();
            req.setAttribute("types", list);
            req.getRequestDispatcher("/WEB-INF/admin/room_types_list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Load room types failed", e);
        }
    }
}