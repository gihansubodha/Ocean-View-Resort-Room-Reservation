package com.oceanview.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/confirm")
public class ConfirmReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        resp.sendRedirect(req.getContextPath()
                + "/reservations/advance?id=" + id
                + "&toast=advanceRequired");
    }
}