package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.notify.NotificationManager;
import com.oceanview.notify.event.ReservationCheckedInEvent;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/checkin")
public class CheckInServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            Reservation r = reservationDAO.findById(id);
            if (r == null) {
                resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&error=Reservation%20not%20found");
                return;
            }

            if (!"CONFIRMED".equalsIgnoreCase(r.getStatus())) {
                resp.sendRedirect(req.getContextPath()
                        + "/reservations/view?id=" + id
                        + "&toast=confirmFirst");
                return;
            }

            reservationService.checkIn(id);

            Reservation updated = reservationDAO.findById(id);
            if (updated != null && updated.getRoomId() != null) {
                roomDAO.updateStatus(updated.getRoomId(), "OCCUPIED");
            }

            Guest g = guestDAO.findById(r.getGuestId());
            if (updated != null && g != null && g.getEmail() != null && !g.getEmail().isBlank()) {
                NotificationManager.getInstance().publish(
                        new ReservationCheckedInEvent(updated, g.getEmail())
                );
            }

            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=checkedin");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}