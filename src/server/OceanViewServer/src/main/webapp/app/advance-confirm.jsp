<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 09:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>
<%@ page import="com.oceanview.model.Bill" %>
<%@ page import="java.math.BigDecimal" %>

<%
    String error = request.getParameter("error");
    ReservationDetails d = (ReservationDetails) request.getAttribute("details");
    Bill bill = (Bill) request.getAttribute("bill");
    BigDecimal advanceRequired = (BigDecimal) request.getAttribute("advanceRequired");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Advance Confirmation</title>
    <style>
        body{font-family:Arial;margin:40px;background:#f7f7f7;}
        .card{background:#fff;padding:24px;border:1px solid #ddd;border-radius:10px;max-width:900px}
        .row{margin:10px 0;}
        .label{display:inline-block;width:220px;font-weight:bold;}
        .btn{padding:10px 16px;border:1px solid #333;background:#fff;border-radius:8px;cursor:pointer;margin-right:10px;}
        .btn:hover{background:#333;color:#fff;}
        .error{color:#b00020;font-weight:bold;margin:12px 0;}
        select,input{padding:8px;border-radius:8px;border:1px solid #ccc;}
        .box{border:1px solid #eee;border-radius:10px;padding:14px;margin-top:16px;background:#fafafa;}
    </style>
</head>
<body>

<div class="card">
    <h1>Advance Payment / ID Confirmation</h1>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (d != null && bill != null) { %>

    <div class="box">
        <h3>Reservation</h3>
        <div class="row"><span class="label">Reservation ID:</span> <%= d.getReservationId() %></div>
        <div class="row"><span class="label">Code:</span> <%= d.getReservationCode() %></div>
        <div class="row"><span class="label">Status:</span> <%= d.getReservationStatus() %></div>
    </div>

    <div class="box">
        <h3>Option 1: Confirm using Identification</h3>
        <div class="row">Enter the same document number you used during reservation creation.</div>

        <form method="post" action="<%= request.getContextPath() %>/reservations/advance">
            <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>

            <div class="row">
                <span class="label">Document Type:</span>
                <select name="docType" required>
                    <option value="NIC">NIC</option>
                    <option value="DRIVING">Driving Licence</option>
                    <option value="PASSPORT">Passport</option>
                </select>
            </div>

            <div class="row">
                <span class="label">Document Number:</span>
                <input type="text" name="docNumber" required />
            </div>

            <button class="btn" type="submit">Verify & Confirm</button>
            <a class="btn" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back</a>
        </form>
    </div>

    <div class="box">
        <h3>Option 2: Pay Advance (20%)</h3>
        <div class="row"><span class="label">Bill Total:</span> LKR <%= bill.getTotal() %></div>
        <div class="row"><span class="label">Advance Required:</span> <b>LKR <%= advanceRequired %></b></div>
        <div class="row">Pay at least 20% on the Bill page to confirm automatically.</div>

        <a class="btn" href="<%= request.getContextPath() %>/bills/view?reservationId=<%= d.getReservationId() %>&toast=advancePayRequired">
            Go to Bill & Pay
        </a>
    </div>

    <% } %>
</div>

</body>
</html>