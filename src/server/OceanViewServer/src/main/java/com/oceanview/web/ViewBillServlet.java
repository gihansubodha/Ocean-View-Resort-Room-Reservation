package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.ReservationDetails;
import com.oceanview.service.BillingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bills/view")
public class ViewBillServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();

    private final BillDAO billDAO = new BillDAOImpl();
    private final BillingService billingService = new BillingService(billDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String ridParam = request.getParameter("reservationId");
            if (ridParam == null || ridParam.isBlank()) {
                throw new Exception("reservationId is required.");
            }

            int reservationId = Integer.parseInt(ridParam);

            ReservationDetails details = reservationDAO.findDetailsById(reservationId);
            if (details == null) {
                throw new Exception("Reservation not found (ID: " + reservationId + ").");
            }

            Bill bill = billingService.calculateBill(details);
            long nights = billingService.calculateNights(details);

            // Optional: if you want to persist bill records, uncomment:
            // int billId = billingService.createBill(bill);
            // request.setAttribute("billId", billId);

            request.setAttribute("details", details);
            request.setAttribute("bill", bill);
            request.setAttribute("nights", nights);

            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
        }
    }
}