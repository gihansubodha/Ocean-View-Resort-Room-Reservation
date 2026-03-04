<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>
<%@ page import="com.oceanview.model.Bill" %>

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
        .ok { color: #0a7a2f; font-weight: bold; margin: 14px 0; }
        .total { font-size: 18px; font-weight: bold; margin-top: 16px; }
        .pill { display:inline-block; padding:4px 10px; border-radius:999px; border:1px solid #ddd; font-size:12px; }
        .box { border:1px solid #eee; border-radius:10px; padding:14px; margin-top:16px; background:#fafafa; }
        select, input { padding: 8px; border-radius: 8px; border: 1px solid #ccc; }
    </style>
</head>
<body>

<div class="card">
    <h1>Bill</h1>

    <%
        String error = (String) request.getAttribute("error");
        ReservationDetails d = (ReservationDetails) request.getAttribute("details");
        Bill bill = (Bill) request.getAttribute("bill");

        String paid = request.getParameter("paid");
        String errorParam = request.getParameter("error");

        boolean showOk = "1".equals(paid);
    %>

    <% if (showOk) { %>
    <div class="ok">Payment recorded successfully.</div>
    <% } %>

    <% if (errorParam != null && !errorParam.isBlank()) { %>
    <div class="error"><%= errorParam %></div>
    <% } %>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    <% } %>

    <% if (d != null && bill != null) { %>
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
    <div class="row"><span class="label">Nightly Rate:</span> LKR <%= bill.getNightlyRate() %></div>
    <div class="row"><span class="label">Nights:</span> <%= bill.getNights() %></div>

    <div class="row"><span class="label">Subtotal:</span> LKR <%= bill.getSubtotal() %></div>
    <div class="row"><span class="label">Discount:</span> LKR <%= bill.getDiscountAmount() %></div>
    <div class="row"><span class="label">Tax:</span> LKR <%= bill.getTaxAmount() %></div>

    <div class="total">Total: LKR <%= bill.getTotal() %></div>

    <div class="box">
        <h3 style="margin-top:0;">Payment</h3>

        <div class="row"><span class="label">Status:</span> <span class="pill"><%= bill.getBillStatus() %></span></div>
        <div class="row"><span class="label">Paid Total:</span> LKR <%= bill.getPaidTotal() %></div>
        <div class="row"><span class="label">Balance:</span> LKR <%= bill.getBalance() %></div>

        <%
            java.math.BigDecimal bal = bill.getBalance();
            boolean balancePositive = (bal != null && bal.compareTo(java.math.BigDecimal.ZERO) > 0);
            boolean isPaid = "PAID".equalsIgnoreCase(bill.getBillStatus());
        %>

        <% if (balancePositive && !isPaid) { %>
        <form method="post" action="<%= request.getContextPath() %>/payments/add" style="margin-top:14px;">
            <input type="hidden" name="reservationId" value="<%= d.getReservationId() %>"/>
            <input type="hidden" name="billId" value="<%= bill.getBillId() %>"/>

            <div class="row">
                <span class="label">Payment Method:</span>
                <select name="method" required>
                    <option value="CASH">CASH</option>
                    <option value="CARD">CARD</option>
                    <option value="TRANSFER">TRANSFER</option>
                </select>
            </div>

            <div class="row">
                <span class="label">Amount:</span>
                <input type="number" name="amount" min="1" step="0.01" required>
            </div>

            <button class="btn" type="submit">Accept Payment</button>
        </form>
        <% } else { %>
        <div class="row" style="color:#0a7a2f;font-weight:bold;margin-top:10px;">
            No payment required (Bill is fully paid).
        </div>
        <% } %>
    </div>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back to Reservation</a>
        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    </div>
    <% } %>
</div>

</body>
</html>