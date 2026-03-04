package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/payments/add")
public class AddPaymentServlet extends HttpServlet {

    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));
            int billId = Integer.parseInt(req.getParameter("billId"));
            double amount = Double.parseDouble(req.getParameter("amount"));
            String method = req.getParameter("method"); // CASH/CARD/TRANSFER

            billDAO.addPayment(billId, amount, method);

            resp.sendRedirect(req.getContextPath() + "/bills/view?reservationId=" + reservationId + "&paid=1");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/bills/view?reservationId=" + req.getParameter("reservationId") + "&error=" + url(e.getMessage()));
        }
    }

    private String url(String s) { return s == null ? "" : s.replace(" ", "%20"); }
}