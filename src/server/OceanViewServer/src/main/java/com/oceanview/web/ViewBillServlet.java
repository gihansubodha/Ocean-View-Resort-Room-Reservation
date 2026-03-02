package com.oceanview.web;

import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.ReservationDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;

@WebServlet("/bills/view")
public class ViewBillServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();

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

            long nights = ChronoUnit.DAYS.between(d.getCheckIn(), d.getCheckOut());
            if (nights < 1) nights = 1; // safety

            double nightlyRate = d.getNightlyRate();
            double total = nightlyRate * nights;

            request.setAttribute("details", d);
            request.setAttribute("nights", nights);
            request.setAttribute("total", total);

            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/bill.jsp").forward(request, response);
        }
    }
}