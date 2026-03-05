package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import com.oceanview.notify.NotificationService;
import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.sender.GmailSmtpEmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservations/checkout")
public class CheckOutServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    private NotificationService notificationService;

    @Override
    public void init() {
        this.notificationService = buildNotificationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // DEBUG (temporary) - confirms Tomcat can read app password env var
        String p = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");
        System.out.println("PASS LEN=" + (p == null ? 0 : p.length()));

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            Reservation before = reservationDAO.findById(id);
            Guest g = (before == null ? null : guestDAO.findById(before.getGuestId()));

            reservationService.checkOut(id);

            // Checked-out email (non-blocking)
            try {
                Reservation after = reservationDAO.findById(id);

                boolean canSend =
                        notificationService != null
                                && after != null
                                && g != null
                                && g.getEmail() != null
                                && !g.getEmail().trim().isEmpty();

                if (canSend) {
                    ReservationEmailTemplate template = new ReservationEmailTemplate() {
                        @Override
                        public String subject(Reservation rr) {
                            return "OceanView - Checked Out [" + rr.getReservationCode() + "]";
                        }

                        @Override
                        public String bodyHtml(Reservation rr) {
                            return String.format(
                                    "<div style='font-family:Arial;line-height:1.5'>"
                                            + "<h2>Check-out Completed</h2>"
                                            + "<p>Status updated to <b>CHECKED_OUT</b>.</p>"
                                            + "<p><b>Reservation Code:</b> %s</p>"
                                            + "<p>Thank you for staying with OceanView.</p>"
                                            + "<hr/>"
                                            + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                            + "</div>",
                                    rr.getReservationCode()
                            );
                        }
                    };

                    getServletContext().log("EMAIL SEND ATTEMPT: to=" + g.getEmail());
                    notificationService.sendReservationEmail(g.getEmail(), after, template);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                getServletContext().log("EMAIL SEND FAILED: " + ex.getMessage(), ex);
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

    private NotificationService buildNotificationService() {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            return null;
        }

        // IMPORTANT: do NOT strip here if your sender already normalizes.
        // But it's safe to keep pass raw and let GmailSmtpEmailSender normalize.

        GmailConfig cfg = new GmailConfig(user, pass);
        EmailSender sender = new GmailSmtpEmailSender(cfg);
        return new NotificationService(sender);
    }
}