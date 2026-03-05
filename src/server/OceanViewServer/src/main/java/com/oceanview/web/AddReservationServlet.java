package com.oceanview.web;

import com.oceanview.dao.*;
import com.oceanview.model.*;
import com.oceanview.notify.sender.GmailSmtpEmailSender;
import com.oceanview.service.ReservationService;

// NEW imports (notification system)
import com.oceanview.notify.NotificationService;
import com.oceanview.notify.config.GmailConfig;
import com.oceanview.notify.sender.EmailSender;
import com.oceanview.notify.template.ReservationEmailTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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

    private final ReservationService reservationService =
            new ReservationService(new ReservationDAOImpl());

    // NEW: notification service
    private NotificationService notificationService;

    @Override
    public void init() throws ServletException {
        // Create the NotificationService once for this servlet (clean + efficient)
        this.notificationService = buildNotificationService();
    }

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
            Guest guest = new Guest();
            guest.setFirstName(request.getParameter("firstName"));
            guest.setLastName(request.getParameter("lastName"));
            guest.setNicPassport(request.getParameter("nicPassport"));
            guest.setPhone(request.getParameter("phone"));
            guest.setEmail(request.getParameter("email"));
            guest.setAddress(request.getParameter("address"));

            int guestId = resolveGuestId(guest);

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
            reservation.setReservationCode(generateReservationCode());

            // Unconfirmed state
            reservation.setStatus("PENDING");

            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                Object userIdObj = session.getAttribute("userId");
                if (userIdObj instanceof Integer) reservation.setCreatedBy((Integer) userIdObj);
                else reservation.setCreatedBy(Integer.parseInt(String.valueOf(userIdObj)));
            }

            int reservationId = reservationService.createReservation(reservation);

            // Bill is generated on create (even if unconfirmed)
            reservationService.generateBill(reservationId);

            // NEW: Gmail notification (safe + non-blocking)
            try {
                // Only send if guest email exists
                String guestEmail = guest.getEmail();
                if (guestEmail != null && !guestEmail.isBlank() && notificationService != null) {

                    // Minimal template for "PENDING created"
                    ReservationEmailTemplate template = new ReservationEmailTemplate() {
                        @Override
                        public String subject(Reservation r) {
                            return "OceanView - Reservation Created (Pending) [" + r.getReservationCode() + "]";
                        }

                        @Override
                        public String bodyHtml(Reservation r) {
                            return String.format(
                                    "<div style='font-family:Arial;line-height:1.5'>"
                                            +   "<h2>Reservation Created (Pending Confirmation)</h2>"
                                            +   "<p>Your reservation request has been created and is currently <b>PENDING</b>.</p>"
                                            +   "<p><b>Reservation Code:</b> %s</p>"
                                            +   "<p><b>Check-in:</b> %s<br/>"
                                            +      "<b>Check-out:</b> %s<br/>"
                                            +      "<b>Guests:</b> %d</p>"
                                            +   "<p>To confirm, please provide the required document details at the front desk / staff portal.</p>"
                                            +   "<hr/>"
                                            +   "<p style='font-size:12px;color:#666'>OceanView Resort Reservation System</p>"
                                            + "</div>",
                                    r.getReservationCode(),
                                    String.valueOf(r.getCheckIn()),
                                    String.valueOf(r.getCheckOut()),
                                    r.getNumGuests()
                            );
                        }
                    };

                    // Ensure reservation object has ID for email content
                    reservation.setReservationId(reservationId);

                    notificationService.sendReservationEmail(guestEmail, reservation, template);
                }
            } catch (Exception mailEx) {
                // DO NOT break reservation creation if email fails
                // (Optional) you can log this to server logs:
                // mailEx.printStackTrace();
            }

            // where NOT to reserve room (keep your existing behavior)
            response.sendRedirect(request.getContextPath()
                    + "/reservations/view?id=" + reservationId
                    + "&created=1");

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
            } catch (Exception ignored) {}

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
        if (guest.getFirstName() == null || guest.getFirstName().isBlank())
            throw new Exception("First name is required.");
        if (guest.getLastName() == null || guest.getLastName().isBlank())
            throw new Exception("Last name is required.");
        if (guest.getPhone() == null || guest.getPhone().isBlank())
            throw new Exception("Phone is required.");

        if (guest.getNicPassport() != null && !guest.getNicPassport().isBlank()) {
            Guest existingGuest = guestDAO.findByNicPassport(guest.getNicPassport());
            if (existingGuest != null) return existingGuest.getGuestId();
        }
        return guestDAO.insert(guest);
    }

    private String generateReservationCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "RES-" + datePart + "-" + randomPart;
    }


    // NEW: Build NotificationService (DIP-friendly)
    private NotificationService buildNotificationService() {
        String user = System.getenv("OCEANVIEW_GMAIL_USER");
        String pass = System.getenv("OCEANVIEW_GMAIL_APP_PASSWORD");

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) return null;

        GmailConfig cfg = new GmailConfig(user, pass);
        EmailSender sender = new GmailSmtpEmailSender(cfg);
        return new NotificationService(sender);
    }
}