package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.notify.sender.GmailSmtpEmailSender;
import com.oceanview.service.ReservationService;

import com.oceanview.notify.NotificationService;
import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

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

    private NotificationService notificationService;

    @Override
    public void init() {
        this.notificationService = buildNotificationService();
    }

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

            // Checked-in email (non-blocking)
            try {
                Guest g = guestDAO.findById(r.getGuestId());
                if (notificationService != null && updated != null && g != null
                        && g.getEmail() != null && !g.getEmail().isBlank()) {

                    ReservationEmailTemplate template = new ReservationEmailTemplate() {
                        @Override
                        public String subject(Reservation rr) {
                            return "OceanView - Checked In [" + rr.getReservationCode() + "]";
                        }

                        @Override
                        public String bodyHtml(Reservation rr) {
                            return String.format(
                                    "<div style='font-family:Arial;line-height:1.5'>"
                                            + "<h2>Check-in Successful</h2>"
                                            + "<p>Status updated to <b>CHECKED_IN</b>.</p>"
                                            + "<p><b>Reservation Code:</b> %s</p>"
                                            + "<p>Enjoy your stay at OceanView!</p>"
                                            + "<hr/>"
                                            + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                            + "</div>",
                                    rr.getReservationCode()
                            );
                        }
                    };

                    notificationService.sendReservationEmail(g.getEmail(), updated, template);
                }
            } catch (Exception ignored) { }

            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=checkedin");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }

    private NotificationService buildNotificationService() {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) return null;

        GmailConfig cfg = new GmailConfig(user, pass);
        EmailSender sender = new GmailSmtpEmailSender(cfg);
        return new NotificationService(sender);
    }
}