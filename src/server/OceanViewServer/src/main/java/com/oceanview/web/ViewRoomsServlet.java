package com.oceanview.web;

import com.oceanview.model.Room;
import com.oceanview.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class ViewRoomsServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = request.getParameter("status");

        try {
            List<Room> rooms = roomService.getRoomsByStatus(status);
            request.setAttribute("rooms", rooms);
            request.setAttribute("selectedStatus", status);
            request.getRequestDispatcher("/app/rooms.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error loading rooms", e);
        }
    }
}