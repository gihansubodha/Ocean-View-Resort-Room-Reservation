package com.oceanview.web;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.ReservationDetails;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reservationIdParam = request.getParameter("reservationId");
            if (reservationIdParam == null || reservationIdParam.isBlank()) {
                request.setAttribute("error", "Reservation ID is required to view a bill.");
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            int reservationId = Integer.parseInt(reservationIdParam);
            ReservationDetails d = reservationDAO.findDetailsById(reservationId);

            if (d == null) {
                request.setAttribute("error", "No reservation found for ID: " + reservationId);
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            //  Generate / re-generate bill using SP (includes tax + discount rules)
            billDAO.generateBill(reservationId);

            Bill bill = billDAO.findByReservationId(reservationId);
            if (bill == null) {
                request.setAttribute("error", "Bill not found for reservation ID: " + reservationId);
                request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
                return;
            }

            request.setAttribute("details", d);
            request.setAttribute("bill", bill);

            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
        }
    }
}