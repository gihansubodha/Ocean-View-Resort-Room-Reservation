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
import com.oceanview.notify.sender.GmailSmtpEmailSender;
import com.oceanview.service.ReservationService;

import com.oceanview.notify.NotificationService;
import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/payments/add")
public class AddPaymentServlet extends HttpServlet {

    private final BillDAO billDAO = new BillDAOImpl();
    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    private NotificationService notificationService;

    @Override
    public void init() {
        this.notificationService = buildNotificationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));
            int billId = Integer.parseInt(req.getParameter("billId"));
            double amount = Double.parseDouble(req.getParameter("amount"));
            String method = req.getParameter("method");

            billDAO.addPayment(billId, amount, method);

            billDAO.generateBill(reservationId);
            Bill bill = billDAO.findByReservationId(reservationId);
            Reservation r = reservationDAO.findById(reservationId);

            // Payment receipt email (non-blocking)
            try {
                if (notificationService != null && r != null) {
                    Guest g = guestDAO.findById(r.getGuestId());
                    if (g != null && g.getEmail() != null && !g.getEmail().isBlank()) {

                        final String paidTotal = (bill != null && bill.getPaidTotal() != null)
                                ? bill.getPaidTotal().toPlainString()
                                : "0.00";
                        final String balance = (bill != null && bill.getBalance() != null)
                                ? bill.getBalance().toPlainString()
                                : "0.00";
                        final String safeMethod = (method == null ? "" : method);

                        ReservationEmailTemplate payTemplate = new ReservationEmailTemplate() {
                            @Override
                            public String subject(Reservation rr) {
                                return "OceanView - Payment Received [" + rr.getReservationCode() + "]";
                            }

                            @Override
                            public String bodyHtml(Reservation rr) {
                                return String.format(
                                        "<div style='font-family:Arial;line-height:1.5'>"
                                                + "<h2>Payment Received</h2>"
                                                + "<p>We received your payment.</p>"
                                                + "<p><b>Reservation Code:</b> %s</p>"
                                                + "<p><b>Payment Amount:</b> %.2f<br/>"
                                                + "<b>Method:</b> %s</p>"
                                                + "<p><b>Total Paid:</b> %s<br/>"
                                                + "<b>Balance:</b> %s</p>"
                                                + "<hr/>"
                                                + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                                + "</div>",
                                        rr.getReservationCode(),
                                        amount,
                                        safeMethod,
                                        paidTotal,
                                        balance
                                );
                            }
                        };

                        notificationService.sendReservationEmail(g.getEmail(), r, payTemplate);
                    }
                }
            } catch (Exception ignored) {
            }

            // Auto-confirm when advance >= 20%
            if (r != null && "PENDING".equalsIgnoreCase(r.getStatus())) {
                BigDecimal requiredAdvance = bill.getTotal().multiply(new BigDecimal("0.20"));
                if (bill.getPaidTotal() != null && bill.getPaidTotal().compareTo(requiredAdvance) >= 0) {

                    reservationService.updateStatus(reservationId, "CONFIRMED");
                    if (r.getRoomId() != null) {
                        roomDAO.updateStatus(r.getRoomId(), "RESERVED");
                    }

                    // Confirmed email (non-blocking)
                    try {
                        if (notificationService != null) {
                            Guest g = guestDAO.findById(r.getGuestId());
                            if (g != null && g.getEmail() != null && !g.getEmail().isBlank()) {

                                ReservationEmailTemplate confirmedTemplate = new ReservationEmailTemplate() {
                                    @Override
                                    public String subject(Reservation rr) {
                                        return "OceanView - Reservation Confirmed [" + rr.getReservationCode() + "]";
                                    }

                                    @Override
                                    public String bodyHtml(Reservation rr) {
                                        return String.format(
                                                "<div style='font-family:Arial;line-height:1.5'>"
                                                        + "<h2>Reservation Confirmed</h2>"
                                                        + "<p>Your reservation is now <b>CONFIRMED</b> (advance payment received).</p>"
                                                        + "<p><b>Reservation Code:</b> %s</p>"
                                                        + "<p><b>Check-in:</b> %s<br/>"
                                                        + "<b>Check-out:</b> %s</p>"
                                                        + "<hr/>"
                                                        + "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                                        + "</div>",
                                                rr.getReservationCode(),
                                                String.valueOf(rr.getCheckIn()),
                                                String.valueOf(rr.getCheckOut())
                                        );
                                    }
                                };

                                notificationService.sendReservationEmail(g.getEmail(), r, confirmedTemplate);
                            }
                        }
                    } catch (Exception ignored) {
                    }

                    resp.sendRedirect(req.getContextPath()
                            + "/reservations/view?id=" + reservationId
                            + "&toast=confirmed");
                    return;
                }
            }

            resp.sendRedirect(req.getContextPath()
                    + "/bills/view?reservationId=" + reservationId
                    + "&paid=1");

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath()
                    + "/bills/view?reservationId=" + req.getParameter("reservationId")
                    + "&error=" + url(e.getMessage()));
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