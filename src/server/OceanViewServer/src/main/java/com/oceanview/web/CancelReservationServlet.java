package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.notify.NotificationManager;
import com.oceanview.notify.event.ReservationCancelledEvent;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/cancel")
public class CancelReservationServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));

            Reservation r = reservationDAO.findById(id);
            Guest g = (r == null ? null : guestDAO.findById(r.getGuestId()));

            reservationService.cancel(id);

            if (r != null && g != null && g.getEmail() != null && !g.getEmail().isBlank()) {
                NotificationManager.getInstance().publish(
                        new ReservationCancelledEvent(r, g.getEmail())
                );
            }

            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=cancelled");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath()
                    + "/reservations/view?id=" + req.getParameter("id")
                    + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}