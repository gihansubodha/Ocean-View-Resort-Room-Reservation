package com.oceanview.web;

import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/checkout")
public class CheckOutServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            reservationService.checkOut(id);
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=checkedout");
        } catch (Exception e) {
            if ("PAY_FIRST".equals(e.getMessage())) {
                resp.sendRedirect(req.getContextPath()
                        + "/bills/view?reservationId=" + id
                        + "&toast=paymentRequired");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}