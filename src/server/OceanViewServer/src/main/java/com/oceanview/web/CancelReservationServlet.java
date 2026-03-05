package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
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

@WebServlet("/reservations/cancel")
public class CancelReservationServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    private NotificationService notificationService;

    @Override
    public void init() {
        this.notificationService = buildNotificationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));

            Reservation r = reservationDAO.findById(id);
            Guest g = (r == null ? null : guestDAO.findById(r.getGuestId()));

            reservationService.cancel(id);

            // Cancelled email (non-blocking)
            try {
                if (notificationService != null && r != null && g != null
                        && g.getEmail() != null && !g.getEmail().isBlank()) {

                    ReservationEmailTemplate template = new ReservationEmailTemplate() {
                        @Override
                        public String subject(Reservation rr) {
                            return "OceanView - Reservation Cancelled [" + rr.getReservationCode() + "]";
                        }

                        @Override
                        public String bodyHtml(Reservation rr) {
                            return String.format(
                                    "<div style='font-family:Arial;line-height:1.5'>"
                                            + "<h2>Reservation Cancelled</h2>"
                                            + "<p>Your reservation has been <b>CANCELLED</b>.</p>"
                                            + "<p><b>Reservation Code:</b> %s</p>"
                                            + "<p>If this was a mistake, please contact reception.</p>"
                                            + "<hr/>"
                                            + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                            + "</div>",
                                    rr.getReservationCode()
                            );
                        }
                    };

                    notificationService.sendReservationEmail(g.getEmail(), r, template);
                }
            } catch (Exception ignored) { }

            resp.sendRedirect(req.getContextPath() + "/reservations/view?id=" + id + "&toast=cancelled");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath()
                    + "/reservations/view?id=" + req.getParameter("id")
                    + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) { return s == null ? "" : s.replace(" ", "%20"); }

    private NotificationService buildNotificationService() {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) return null;

        GmailConfig cfg = new GmailConfig(user, pass);
        EmailSender sender = new GmailSmtpEmailSender(cfg);
        return new NotificationService(sender);
    }
}