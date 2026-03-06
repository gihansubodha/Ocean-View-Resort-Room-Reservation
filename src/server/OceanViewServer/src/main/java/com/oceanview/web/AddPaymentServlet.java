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
import com.oceanview.notify.NotificationManager;
import com.oceanview.notify.event.ReservationConfirmedEvent;
import com.oceanview.notify.event.ReservationPaymentEvent;
import com.oceanview.service.ReservationService;

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

            if (r != null) {
                Guest g = guestDAO.findById(r.getGuestId());

                String paidTotal = (bill != null && bill.getPaidTotal() != null)
                        ? bill.getPaidTotal().toPlainString()
                        : "0.00";

                String balance = (bill != null && bill.getBalance() != null)
                        ? bill.getBalance().toPlainString()
                        : "0.00";

                if (g != null && g.getEmail() != null && !g.getEmail().isBlank()) {
                    NotificationManager.getInstance().publish(
                            new ReservationPaymentEvent(
                                    r,
                                    g.getEmail(),
                                    amount,
                                    method,
                                    paidTotal,
                                    balance
                            )
                    );
                }

                if ("PENDING".equalsIgnoreCase(r.getStatus()) && bill != null && bill.getTotal() != null) {
                    BigDecimal requiredAdvance = bill.getTotal().multiply(new BigDecimal("0.20"));

                    if (bill.getPaidTotal() != null && bill.getPaidTotal().compareTo(requiredAdvance) >= 0) {
                        reservationService.updateStatus(reservationId, "CONFIRMED");

                        if (r.getRoomId() != null) {
                            roomDAO.updateStatus(r.getRoomId(), "RESERVED");
                        }

                        if (g != null && g.getEmail() != null && !g.getEmail().isBlank()) {
                            NotificationManager.getInstance().publish(
                                    new ReservationConfirmedEvent(r, g.getEmail())
                            );
                        }

                        resp.sendRedirect(req.getContextPath()
                                + "/reservations/view?id=" + reservationId
                                + "&toast=confirmed");
                        return;
                    }
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
}