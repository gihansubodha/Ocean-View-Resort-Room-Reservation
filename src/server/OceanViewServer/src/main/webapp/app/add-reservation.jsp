<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 02/28/26
  Time: 20:04
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="com.oceanview.model.Room" %>
<%@ page import="com.oceanview.model.Bill" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add Reservation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- NEW UI -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <style>
        /* Page-specific (no logic changes) */
        .actions{
            margin-top: 14px;
            display:flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        /* Keep your JS-driven preserve behavior but make fields look consistent */
        .field label{
            display:block;
            margin-bottom: 6px;
            font-weight: 700;
            color: var(--muted);
            font-size: 13px;
        }

        .note {
            color: var(--muted);
            font-size: 13px;
            margin-top: 6px;
            line-height: 1.5;
        }

        .bill-box {
            margin-top: 16px;
        }
        .bill-row { margin: 8px 0; color: var(--text); font-size: 13px; }
    </style>
</head>

<body>
<div class="app-shell">
    <div class="container">

        <!-- Top bar (consistent UI) -->
        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Create Reservation</div>
                </div>
            </div>

            <div class="nav-actions">
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp">Home</a>
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Add Reservation</h2>
            <div class="page-subtitle">Create a new reservation for a guest.</div>

            <%
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                Bill generatedBill = (Bill) request.getAttribute("generatedBill");

                String firstName = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
                String lastName = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";
                String nicPassport = request.getParameter("nicPassport") != null ? request.getParameter("nicPassport") : "";
                String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
                String email = request.getParameter("email") != null ? request.getParameter("email") : "";
                String address = request.getParameter("address") != null ? request.getParameter("address") : "";
                String checkIn = request.getParameter("checkIn") != null ? request.getParameter("checkIn") : "";
                String checkOut = request.getParameter("checkOut") != null ? request.getParameter("checkOut") : "";
                String numGuests = request.getParameter("numGuests") != null ? request.getParameter("numGuests") : "";
                String specialRequests = request.getParameter("specialRequests") != null ? request.getParameter("specialRequests") : "";
                String selectedRoomIdParam = request.getParameter("roomId") != null ? request.getParameter("roomId") : "";
            %>

            <% if (error != null) { %>
            <div class="alert alert-err" style="margin-top:12px;"><%= error %></div>
            <% } %>

            <% if (success != null) { %>
            <div class="alert alert-ok" style="margin-top:12px;"><%= success %></div>
            <% } %>

            <form method="post" action="<%= request.getContextPath() %>/reservations/add">

                <div class="section">
                    <div class="form-grid">

                        <div class="field">
                            <label>First Name</label>
                            <input type="text" name="firstName" value="<%= firstName %>" required>
                        </div>

                        <div class="field">
                            <label>Last Name</label>
                            <input type="text" name="lastName" value="<%= lastName %>" required>
                        </div>

                        <div class="field">
                            <label>NIC / Passport</label>
                            <input type="text" name="nicPassport" value="<%= nicPassport %>">
                        </div>

                        <div class="field">
                            <label>Phone</label>
                            <input type="text" name="phone" value="<%= phone %>" required>
                        </div>

                        <div class="field" style="grid-column: 1 / -1;">
                            <label>Email</label>
                            <input type="email" name="email" value="<%= email %>">
                        </div>

                        <div class="field" style="grid-column: 1 / -1;">
                            <label>Address</label>
                            <textarea name="address"><%= address %></textarea>
                        </div>

                        <div class="field">
                            <label>Room Type</label>
                            <select name="roomTypeId" id="roomTypeId" onchange="onRoomTypeChange()" required>
                                <option value="">-- Select Room Type --</option>
                                <%
                                    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
                                    Object selectedRoomTypeId = request.getAttribute("selectedRoomTypeId");

                                    if (selectedRoomTypeId == null && request.getParameter("roomTypeId") != null && !request.getParameter("roomTypeId").isBlank()) {
                                        selectedRoomTypeId = request.getParameter("roomTypeId");
                                    }

                                    if (roomTypes != null) {
                                        for (RoomType rt : roomTypes) {
                                            boolean selected = selectedRoomTypeId != null &&
                                                    Integer.parseInt(String.valueOf(selectedRoomTypeId)) == rt.getRoomTypeId();
                                %>
                                <option value="<%= rt.getRoomTypeId() %>" <%= selected ? "selected" : "" %>>
                                    <%= rt.getTypeName() %> - LKR <%= rt.getNightlyRate() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="field">
                            <label>Available Room (Optional)</label>
                            <select name="roomId" id="roomId">
                                <option value="">-- Optional: Select Available Room --</option>
                                <%
                                    List<Room> availableRooms = (List<Room>) request.getAttribute("availableRooms");
                                    if (availableRooms != null) {
                                        for (Room room : availableRooms) {
                                            boolean selectedRoom = selectedRoomIdParam != null &&
                                                    !selectedRoomIdParam.isBlank() &&
                                                    Integer.parseInt(selectedRoomIdParam) == room.getRoomId();
                                %>
                                <option value="<%= room.getRoomId() %>" <%= selectedRoom ? "selected" : "" %>>
                                    <%= room.getRoomNumber() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <div class="note">Room list changes when you select a room type.</div>
                        </div>

                        <div class="field">
                            <label>Check-In Date</label>
                            <input type="date" name="checkIn" value="<%= checkIn %>" required>
                        </div>

                        <div class="field">
                            <label>Check-Out Date</label>
                            <input type="date" name="checkOut" value="<%= checkOut %>" required>
                        </div>

                        <div class="field">
                            <label>Number of Guests</label>
                            <input type="number" name="numGuests" min="1" value="<%= numGuests %>" required>
                        </div>

                        <div class="field" style="grid-column: 1 / -1;">
                            <label>Special Requests</label>
                            <textarea name="specialRequests"><%= specialRequests %></textarea>
                        </div>

                    </div>
                </div>

                <div class="actions nav-actions" style="justify-content:flex-start;">
                    <button class="btn btn-success" type="submit">Create Reservation</button>
                    <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
                </div>
            </form>

            <% if (generatedBill != null) { %>
            <div class="section bill-box">
                <h3 style="margin:0 0 10px; font-size:16px;">Generated Bill</h3>
                <div class="bill-row"><b>Reservation ID:</b> <%= generatedBill.getReservationId() %></div>
                <div class="bill-row"><b>Subtotal:</b> LKR <%= generatedBill.getSubtotal() %></div>
                <div class="bill-row"><b>Tax:</b> LKR <%= generatedBill.getTaxAmount() %></div>
                <div class="bill-row"><b>Discount:</b> LKR <%= generatedBill.getDiscountAmount() %></div>
                <div class="bill-row"><b>Total:</b> LKR <%= generatedBill.getTotal() %></div>
                <div class="bill-row"><b>Status:</b> UNPAID</div>
            </div>
            <% } %>

        </div>

    </div>
</div>

<script>
    function enc(v){ return encodeURIComponent(v || ''); }

    // Preserve typed form fields when changing room type (prevents wipe-out)
    function onRoomTypeChange() {
        const roomTypeId = document.getElementById('roomTypeId').value;

        const firstName = document.querySelector('[name="firstName"]').value;
        const lastName = document.querySelector('[name="lastName"]').value;
        const nicPassport = document.querySelector('[name="nicPassport"]').value;
        const phone = document.querySelector('[name="phone"]').value;
        const email = document.querySelector('[name="email"]').value;
        const address = document.querySelector('[name="address"]').value;
        const checkIn = document.querySelector('[name="checkIn"]').value;
        const checkOut = document.querySelector('[name="checkOut"]').value;
        const numGuests = document.querySelector('[name="numGuests"]').value;
        const specialRequests = document.querySelector('[name="specialRequests"]').value;
        const roomId = document.querySelector('[name="roomId"]').value;

        const base = '<%= request.getContextPath() %>/reservations/add';
        const qs =
            '?roomTypeId=' + enc(roomTypeId) +
            '&firstName=' + enc(firstName) +
            '&lastName=' + enc(lastName) +
            '&nicPassport=' + enc(nicPassport) +
            '&phone=' + enc(phone) +
            '&email=' + enc(email) +
            '&address=' + enc(address) +
            '&checkIn=' + enc(checkIn) +
            '&checkOut=' + enc(checkOut) +
            '&numGuests=' + enc(numGuests) +
            '&specialRequests=' + enc(specialRequests) +
            '&roomId=' + enc(roomId);

        window.location = base + qs;
    }
</script>

</body>
</html>