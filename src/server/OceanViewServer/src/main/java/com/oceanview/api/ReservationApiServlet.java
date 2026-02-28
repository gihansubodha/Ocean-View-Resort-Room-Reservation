package com.oceanview.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/reservations")
public class ReservationApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Example: /api/reservations?id=123
        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\":\"TODO: implement GET reservation\"}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\":\"TODO: implement POST reservation\"}");
    }
}
