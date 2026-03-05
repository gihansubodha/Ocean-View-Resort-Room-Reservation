package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.ReservationDetails;
import com.oceanview.notify.sender.GmailSmtpEmailSender;
import com.oceanview.service.ReservationService;

// NEW (notification)
import com.oceanview.notify.NotificationService;
import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/reservations/advance")
public class AdvanceConfirmServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    // NEW
    private NotificationService notificationService;

    @Override
    public void init() {
        this.notificationService = buildNotificationService();
            }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));

            ReservationDetails details = reservationDAO.findDetailsById(id);
            if (details == null) throw new Exception("Reservation not found.");

            billDAO.generateBill(id);
            Bill bill = billDAO.findByReservationId(id);
            if (bill == null) throw new Exception("Bill not found.");

            BigDecimal advanceRequired = bill.getTotal().multiply(new BigDecimal("0.20"));

            req.setAttribute("details", details);
            req.setAttribute("bill", bill);
            req.setAttribute("advanceRequired", advanceRequired);

            req.getRequestDispatcher("/app/advance-confirm.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/app/reservation-details.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int reservationId = Integer.parseInt(req.getParameter("id"));
            String docNumber = req.getParameter("docNumber");

            Reservation r = reservationDAO.findById(reservationId);
            if (r == null) throw new Exception("Reservation not found.");

            Guest g = guestDAO.findById(r.getGuestId());
            String stored = (g == null ? null : g.getNicPassport());

            boolean idValid =
                    stored != null && !stored.isBlank()
                            && docNumber != null && !docNumber.isBlank()
                            && stored.trim().equalsIgnoreCase(docNumber.trim());

            if (!idValid) {
                resp.sendRedirect(req.getContextPath()
                        + "/reservations/view?id=" + reservationId
                        + "&toast=invalidDocument");
                return;
            }

            // confirm + reserve room
            reservationService.updateStatus(reservationId, "CONFIRMED");
            if (r.getRoomId() != null) {
                roomDAO.updateStatus(r.getRoomId(), "RESERVED");
            }

            // NEW: send confirmed email (non-blocking)
            try {
                if (notificationService != null && g != null && g.getEmail() != null && !g.getEmail().isBlank()) {
                    r.setReservationId(reservationId);

                    ReservationEmailTemplate template = new ReservationEmailTemplate() {
                        @Override
                        public String subject(Reservation rr) {
                            return "OceanView - Reservation Confirmed [" + rr.getReservationCode() + "]";
                        }

                        @Override
                        public String bodyHtml(Reservation r) {
                            return String.format(
                                    "<div style='font-family:Arial;line-height:1.5'>"
                                            +   "<h2>Reservation Created (Pending Confirmation)</h2>"
                                            +   "<p>Your reservation request has been created and is currently <b>PENDING</b>.</p>"
                                            +   "<p><b>Reservation Code:</b> %s</p>"
                                            +   "<p><b>Check-in:</b> %s<br/>"
                                            +      "<b>Check-out:</b> %s<br/>"
                                            +      "<b>Guests:</b> %d</p>"
                                            +   "<p>To confirm, please provide the required document details at the front desk / staff portal.</p>"
                                            +   "<hr/>"
                                            +   "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                            + "</div>",
                                    r.getReservationCode(),
                                    String.valueOf(r.getCheckIn()),
                                    String.valueOf(r.getCheckOut()),
                                    r.getNumGuests()
                            );
                        }
                    };

                    notificationService.sendReservationEmail(g.getEmail(), r, template);
                }
            } catch (Exception ignored) {}

            resp.sendRedirect(req.getContextPath()
                    + "/reservations/view?id=" + reservationId
                    + "&toast=confirmed");

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath()
                    + "/reservations/advance?id=" + req.getParameter("id")
                    + "&error=" + safe(e.getMessage()));
        }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace(" ", "%20");
    }

    // NEW
    private NotificationService buildNotificationService() {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) return null;

        GmailConfig cfg = new GmailConfig(user, pass);
        EmailSender sender = new GmailSmtpEmailSender(cfg);
        return new NotificationService(sender);
    }
}