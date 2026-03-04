package com.oceanview.web;

import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/cancel")
public class CancelReservationServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            reservationService.cancel(id);
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=cancelled");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + req.getParameter("id") + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) { return s == null ? "" : s.replace(" ", "%20"); }
}