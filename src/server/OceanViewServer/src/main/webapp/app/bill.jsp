<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/01/26
  Time: 08:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>
<%@ page import="com.oceanview.model.Bill" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bill</title>
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
        hr { margin: 18px 0; }
    </style>
</head>
<body>
<div class="card">
    <h1>Bill</h1>

    <%
        String error = (String) request.getAttribute("error");
        ReservationDetails d = (ReservationDetails) request.getAttribute("details");
        Bill b = (Bill) request.getAttribute("bill");
        Long nights = (Long) request.getAttribute("nights");
    %>

    <form method="get" action="<%= request.getContextPath() %>/bills/view">
        <div class="row">
            <span class="label">Reservation ID:</span>
            <input type="number" name="reservationId" min="1" required>
            <button class="btn" type="submit">Calculate</button>
            <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
        </div>
    </form>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (d != null && b != null) { %>
    <hr>

    <h2>Reservation</h2>
    <div class="row"><span class="label">Code:</span> <%= d.getReservationCode() %></div>
    <div class="row"><span class="label">Guest:</span> <%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></div>
    <div class="row"><span class="label">Room Type:</span> <%= d.getRoomTypeName() %></div>
    <div class="row"><span class="label">Nightly Rate:</span> LKR <%= d.getNightlyRate() %></div>
    <div class="row"><span class="label">Nights:</span> <%= nights %></div>

    <hr>

    <h2>Charges</h2>
    <div class="row"><span class="label">Subtotal:</span> LKR <%= b.getSubtotal() %></div>
    <div class="row"><span class="label">Tax:</span> LKR <%= b.getTaxAmount() %></div>
    <div class="row"><span class="label">Discount:</span> LKR <%= b.getDiscountAmount() %></div>
    <div class="row"><span class="label">Total:</span> <strong>LKR <%= b.getTotal() %></strong></div>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back to Reservation</a>
    </div>
    <% } %>
</div>
</body>
</html>
