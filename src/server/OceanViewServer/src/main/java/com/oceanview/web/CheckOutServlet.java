package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.notify.NotificationManager;
import com.oceanview.notify.event.ReservationCheckedOutEvent;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/checkout")
public class CheckOutServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            Reservation before = reservationDAO.findById(id);
            Guest g = (before == null ? null : guestDAO.findById(before.getGuestId()));

            reservationService.checkOut(id);

            Reservation after = reservationDAO.findById(id);

            if (after != null && g != null && g.getEmail() != null && !g.getEmail().trim().isEmpty()) {
                NotificationManager.getInstance().publish(
                        new ReservationCheckedOutEvent(after, g.getEmail())
                );
            }

            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=checkedout");

        } catch (Exception e) {
            if ("PAY_FIRST".equals(e.getMessage())) {
                resp.sendRedirect(req.getContextPath()
                        + "/bills/view?reservationId=" + id
                        + "&toast=paymentRequired");
                return;
            }
            resp.sendRedirect(req.getContextPath()
                    + "/reservations/view?id=" + id
                    + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}