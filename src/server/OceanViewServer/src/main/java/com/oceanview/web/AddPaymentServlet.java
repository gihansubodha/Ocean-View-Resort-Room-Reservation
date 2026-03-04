package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
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

            if (r != null && "PENDING".equalsIgnoreCase(r.getStatus())) {
                BigDecimal requiredAdvance = bill.getTotal().multiply(new BigDecimal("0.20"));
                if (bill.getPaidTotal() != null && bill.getPaidTotal().compareTo(requiredAdvance) >= 0) {
                    reservationService.updateStatus(reservationId, "CONFIRMED");
                    if (r.getRoomId() != null) {
                        roomDAO.updateStatus(r.getRoomId(), "RESERVED");
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
}