package com.oceanview.web;

import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.ReservationDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reservations/view")
public class ViewReservationServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                throw new Exception("Reservation ID is required.");
            }

            int reservationId = Integer.parseInt(idParam);

            ReservationDetails details = reservationDAO.findDetailsById(reservationId);
            if (details == null) {
                request.setAttribute("error", "Reservation not found (ID: " + reservationId + ").");
            } else {
                request.setAttribute("details", details);
            }

            request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
        }
    }
}