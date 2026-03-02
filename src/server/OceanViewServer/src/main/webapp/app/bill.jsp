<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bill</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }
        .card { background: #fff; padding: 24px; border-radius: 10px; border: 1px solid #ddd; max-width: 950px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        .row { margin: 10px 0; }
        .label { display:inline-block; width: 220px; font-weight: bold; }
        .btn { padding: 10px 16px; border: 1px solid #333; background: #fff; cursor: pointer; border-radius: 8px;
            text-decoration: none; color: #333; display: inline-block; margin-right: 10px; }
        .btn:hover { background: #333; color: #fff; }
        .error { color: #b00020; font-weight: bold; margin: 14px 0; }
        .total { font-size: 18px; font-weight: bold; margin-top: 16px; }
    </style>
</head>
<body>

<div class="card">
    <h1>Bill</h1>

    <%
        String error = (String) request.getAttribute("error");
        ReservationDetails d = (ReservationDetails) request.getAttribute("details");
        Long nights = (Long) request.getAttribute("nights");
        Double total = (Double) request.getAttribute("total");
    %>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    <% } %>

    <% if (d != null) { %>
    <h2>Reservation</h2>
    <div class="row"><span class="label">Reservation ID:</span> <%= d.getReservationId() %></div>
    <div class="row"><span class="label">Code:</span> <%= d.getReservationCode() %></div>
    <div class="row"><span class="label">Check-in:</span> <%= d.getCheckIn() %></div>
    <div class="row"><span class="label">Check-out:</span> <%= d.getCheckOut() %></div>

    <h2>Guest</h2>
    <div class="row"><span class="label">Name:</span> <%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></div>
    <div class="row"><span class="label">Phone:</span> <%= d.getGuestPhone() %></div>

    <h2>Charges</h2>
    <div class="row"><span class="label">Room Type:</span> <%= d.getRoomTypeName() %></div>
    <div class="row"><span class="label">Nightly Rate:</span> LKR <%= d.getNightlyRate() %></div>
    <div class="row"><span class="label">Nights:</span> <%= nights %></div>

    <div class="total">Total: LKR <%= total %></div>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back to Reservation</a>
        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    </div>
    <% } %>
</div>

</body>
</html>