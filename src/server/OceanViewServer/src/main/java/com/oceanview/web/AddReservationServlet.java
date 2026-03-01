package com.oceanview.web;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.GuestDAOImpl;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.dao.RoomTypeDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@WebServlet("/reservations/add")
public class AddReservationServlet extends HttpServlet {

    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final GuestDAO guestDAO = new GuestDAOImpl();

    // matches your ReservationService constructor
    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        loadFormData(request);

        String roomTypeIdParam = request.getParameter("roomTypeId");
        if (roomTypeIdParam != null && !roomTypeIdParam.isBlank()) {
            try {
                int roomTypeId = Integer.parseInt(roomTypeIdParam);
                List<Room> availableRooms = roomDAO.findAvailableByRoomType(roomTypeId);
                request.setAttribute("availableRooms", availableRooms);
                request.setAttribute("selectedRoomTypeId", roomTypeId);
            } catch (Exception e) {
                request.setAttribute("error", "Unable to load available rooms.");
            }
        }

        request.getRequestDispatcher("/app/add-reservation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Build guest from form
            Guest guest = new Guest();
            guest.setFirstName(request.getParameter("firstName"));
            guest.setLastName(request.getParameter("lastName"));
            guest.setNicPassport(request.getParameter("nicPassport"));
            guest.setPhone(request.getParameter("phone"));
            guest.setEmail(request.getParameter("email"));
            guest.setAddress(request.getParameter("address"));

            // 2. Find existing guest or create new guest
            int guestId = resolveGuestId(guest);

            // 3. Build reservation from form
            Reservation reservation = new Reservation();
            reservation.setGuestId(guestId);
            reservation.setRoomTypeId(Integer.parseInt(request.getParameter("roomTypeId")));

            String roomIdParam = request.getParameter("roomId");
            if (roomIdParam != null && !roomIdParam.isBlank()) {
                reservation.setRoomId(Integer.parseInt(roomIdParam));
            }

            reservation.setCheckIn(LocalDate.parse(request.getParameter("checkIn")));
            reservation.setCheckOut(LocalDate.parse(request.getParameter("checkOut")));
            reservation.setNumGuests(Integer.parseInt(request.getParameter("numGuests")));
            reservation.setSpecialRequests(request.getParameter("specialRequests"));

            // Since your ReservationService does not generate these, set them here
            reservation.setReservationCode(generateReservationCode());
            reservation.setStatus("CONFIRMED");

            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                Object userIdObj = session.getAttribute("userId");
                if (userIdObj instanceof Integer) {
                    reservation.setCreatedBy((Integer) userIdObj);
                } else {
                    reservation.setCreatedBy(Integer.parseInt(String.valueOf(userIdObj)));
                }
            }

            // 4. Create reservation
            int reservationId = reservationService.createReservation(reservation);

            request.setAttribute("success",
                    "Reservation created successfully. Reservation ID: " + reservationId +
                            " | Code: " + reservation.getReservationCode());

            // 5. Update room status if a specific room was selected
            if (reservation.getRoomId() != null) {
                roomDAO.updateStatus(reservation.getRoomId(), "RESERVED"); // must match ENUM exactly
            }

            loadFormData(request);
            request.getRequestDispatcher("/app/add-reservation.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());

            try {
                loadFormData(request);

                String roomTypeIdParam = request.getParameter("roomTypeId");
                if (roomTypeIdParam != null && !roomTypeIdParam.isBlank()) {
                    int roomTypeId = Integer.parseInt(roomTypeIdParam);
                    List<Room> availableRooms = roomDAO.findAvailableByRoomType(roomTypeId);
                    request.setAttribute("availableRooms", availableRooms);
                    request.setAttribute("selectedRoomTypeId", roomTypeId);
                }
            } catch (Exception ignored) {
            }

            request.getRequestDispatcher("/app/add-reservation.jsp").forward(request, response);
        }
    }

    private void loadFormData(HttpServletRequest request) throws ServletException {
        try {
            List<RoomType> roomTypes = roomTypeDAO.findActiveRoomTypes();
            request.setAttribute("roomTypes", roomTypes);
        } catch (Exception e) {
            throw new ServletException("Failed to load reservation form data.", e);
        }
    }

    private int resolveGuestId(Guest guest) throws Exception {
        if (guest.getFirstName() == null || guest.getFirstName().isBlank()) {
            throw new Exception("First name is required.");
        }
        if (guest.getLastName() == null || guest.getLastName().isBlank()) {
            throw new Exception("Last name is required.");
        }
        if (guest.getPhone() == null || guest.getPhone().isBlank()) {
            throw new Exception("Phone is required.");
        }

        if (guest.getNicPassport() != null && !guest.getNicPassport().isBlank()) {
            Guest existingGuest = guestDAO.findByNicPassport(guest.getNicPassport());
            if (existingGuest != null) {
                return existingGuest.getGuestId();
            }
        }

        return guestDAO.insert(guest);
    }

    private String generateReservationCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "RES-" + datePart + "-" + randomPart;
    }
}