package com.oceanview.web;

import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.model.ReservationDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/reservations/search")
public class SearchReservationServlet extends HttpServlet {

    private final ReservationDAOImpl reservationDAO = new ReservationDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = trim(request.getParameter("id"));
            String code = trim(request.getParameter("code"));
            String nic = trim(request.getParameter("nic"));
            String phone = trim(request.getParameter("phone"));
            String email = trim(request.getParameter("email"));
            String roomNumber = trim(request.getParameter("roomNumber"));
            String fromParam = trim(request.getParameter("from"));
            String toParam = trim(request.getParameter("to"));
            String quick = trim(request.getParameter("quick")); // "arrivals" or "departures"

            // If nothing provided, just show page
            if (isBlank(idParam) && isBlank(code) && isBlank(nic) && isBlank(phone)
                    && isBlank(email) && isBlank(roomNumber) && isBlank(fromParam)
                    && isBlank(toParam) && isBlank(quick)) {
                request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
                return;
            }

            // 1) Exact lookup: reservation ID
            if (!isBlank(idParam)) {
                int id = Integer.parseInt(idParam);
                ReservationDetails d = reservationDAO.findDetailsById(id);
                if (d == null) request.setAttribute("error", "No reservation found for Reservation ID: " + id);
                request.setAttribute("details", d);
                request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
                return;
            }

            // 2) Exact lookup: reservation CODE
            if (!isBlank(code)) {
                ReservationDetails d = reservationDAO.findDetailsByCode(code);
                if (d == null) request.setAttribute("error", "No reservation found for Code: " + code);
                request.setAttribute("details", d);
                request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
                return;
            }

            // 3) Quick lists: today arrivals/departures
            if ("arrivals".equalsIgnoreCase(quick)) {
                List<ReservationDetails> results = reservationDAO.findTodayArrivals(LocalDate.now());
                request.setAttribute("results", results);
                request.setAttribute("resultsTitle", "Today’s Arrivals");
                request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
                return;
            }

            if ("departures".equalsIgnoreCase(quick)) {
                List<ReservationDetails> results = reservationDAO.findTodayDepartures(LocalDate.now());
                request.setAttribute("results", results);
                request.setAttribute("resultsTitle", "Today’s Departures");
                request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
                return;
            }

            // 4) Search options advanced in to use: NIC/Phone/Email/Room/Date range
            LocalDate from = null;
            LocalDate to = null;
            if (!isBlank(fromParam)) from = LocalDate.parse(fromParam);
            if (!isBlank(toParam)) to = LocalDate.parse(toParam);

            List<ReservationDetails> results = reservationDAO.searchDetails(
                    nic, phone, email, roomNumber, from, to
            );

            if (results == null || results.isEmpty()) {
                request.setAttribute("error", "No reservations matched your search.");
            } else if (results.size() == 1) {
                // practical behaviour: if one match, open it directly
                request.setAttribute("details", results.get(0));
            } else {
                request.setAttribute("results", results);
                request.setAttribute("resultsTitle", "Search Results (" + results.size() + ")");
            }

            request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/app/reservation-details.jsp").forward(request, response);
        }
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}