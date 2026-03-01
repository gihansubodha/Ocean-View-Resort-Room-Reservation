<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/01/26
  Time: 08:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>

<!DOCTYPE html>
<html>
<head>
    <title>Reservation Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }
        .card { background: #fff; padding: 24px; border-radius: 8px; border: 1px solid #ddd; max-width: 950px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        .error { color: #b00020; font-weight: bold; margin-bottom: 14px; }
        .row { margin-bottom: 10px; }
        .label { display:inline-block; width: 220px; font-weight: bold; }
        .btn { padding: 10px 16px; border: 1px solid #333; background: #fff; cursor: pointer; border-radius: 6px;
            text-decoration: none; color: #333; display: inline-block; margin-right: 10px; }
        .btn:hover { background: #333; color: #fff; }
        input { padding: 8px; width: 220px; }
        .section-title { margin-top: 18px; }
    </style>
</head>
<body>
<div class="card">
    <h1>Reservation Details</h1>

    <%
        String error = (String) request.getAttribute("error");
        ReservationDetails d = (ReservationDetails) request.getAttribute("details");
    %>

    <form method="get" action="<%= request.getContextPath() %>/reservations/view">
        <div class="row">
            <span class="label">Reservation ID:</span>
            <input type="number" name="id" min="1" required>
            <button class="btn" type="submit">Search</button>
            <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
        </div>
    </form>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (d != null) { %>
    <h2 class="section-title">Booking</h2>
    <div class="row"><span class="label">Code:</span> <%= d.getReservationCode() %></div>
    <div class="row"><span class="label">Check-in:</span> <%= d.getCheckIn() %></div>
    <div class="row"><span class="label">Check-out:</span> <%= d.getCheckOut() %></div>
    <div class="row"><span class="label">Guests:</span> <%= d.getNumGuests() %></div>
    <div class="row"><span class="label">Status:</span> <%= d.getReservationStatus() %></div>
    <div class="row"><span class="label">Special requests:</span> <%= d.getSpecialRequests() %></div>

    <h2 class="section-title">Guest</h2>
    <div class="row"><span class="label">Name:</span> <%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></div>
    <div class="row"><span class="label">Phone:</span> <%= d.getGuestPhone() %></div>
    <div class="row"><span class="label">Email:</span> <%= d.getGuestEmail() %></div>
    <div class="row"><span class="label">NIC/Passport:</span> <%= d.getGuestNicPassport() %></div>

    <h2 class="section-title">Room</h2>
    <div class="row"><span class="label">Room Type:</span> <%= d.getRoomTypeName() %> (ID: <%= d.getRoomTypeId() %>)</div>
    <div class="row"><span class="label">Nightly Rate:</span> LKR <%= d.getNightlyRate() %></div>
    <div class="row"><span class="label">Room Assigned:</span>
        <%= (d.getRoomId() == null ? "Not assigned" : (d.getRoomNumber() + " (Room ID: " + d.getRoomId() + ")")) %>
    </div>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/bills/view?reservationId=<%= d.getReservationId() %>">View Bill</a>
    </div>
    <% } %>
</div>
</body>
</html>