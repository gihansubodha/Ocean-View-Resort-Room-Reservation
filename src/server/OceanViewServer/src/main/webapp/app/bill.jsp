<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>
<%@ page import="com.oceanview.model.Bill" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bill</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }
        .card { background: #fff; padding: 24px; border-radius: 10px; border: 1px solid #ddd; max-width: 1050px;
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
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #fafafa; }
    </style>
</head>
<body>

<div class="card">
    <h1>Bill</h1>

    <%
        Object errObj = request.getAttribute("error");
        String error = errObj == null ? null : String.valueOf(errObj);

        ReservationDetails d = (ReservationDetails) request.getAttribute("details");
        Bill bill = (Bill) request.getAttribute("bill");

        boolean dashboardMode = false;
        Object dm = request.getAttribute("dashboardMode");
        if (dm instanceof Boolean) dashboardMode = (Boolean) dm;

        List todaysPayments = (List) request.getAttribute("todaysPayments");
        List todaysUnpaid = (List) request.getAttribute("todaysUnpaid");

        String paid = request.getParameter("paid");
        String toast = request.getParameter("toast");
        String errorParam = request.getParameter("error");
    %>

    <% if ("1".equals(paid)) { %>
    <div class="ok">Payment recorded successfully.</div>
    <% } %>

    <% if ("paymentRequired".equals(toast)) { %>
    <div class="error">Payment required before Check-out</div>
    <% } %>

    <% if ("paidSuccess".equals(toast)) { %>
    <div class="ok">Paid successfully.</div>
    <% } %>

    <% if ("advancePayRequired".equals(toast)) { %>
    <div class="error">Advance payment required: please pay at least 20% to confirm reservation.</div>
    <% } %>

    <% if (errorParam != null && !errorParam.isBlank()) { %>
    <div class="error"><%= errorParam %></div>
    <% } %>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (dashboardMode) { %>

    <h2>Today’s Payments</h2>
    <table>
        <thead>
        <tr>
            <th>Payment ID</th>
            <th>Bill ID</th>
            <th>Amount</th>
            <th>Method</th>
            <th>Time</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (todaysPayments != null && !todaysPayments.isEmpty()) {
                for (Object o : todaysPayments) {
                    String[] p = (String[]) o;
        %>
        <tr>
            <td><%= p[0] %></td>
            <td><%= p[1] %></td>
            <td><%= p[2] %></td>
            <td><%= p[3] %></td>
            <td><%= p[4] %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr><td colspan="5">No payments today.</td></tr>
        <% } %>
        </tbody>
    </table>

    <h2 style="margin-top:18px;">Unpaid / Partial Reservations</h2>
    <table>
        <thead>
        <tr>
            <th>Reservation ID</th>
            <th>Code</th>
            <th>Status</th>
            <th>Room</th>
            <th>Bill Status</th>
            <th>Balance</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (todaysUnpaid != null && !todaysUnpaid.isEmpty()) {
                for (Object o : todaysUnpaid) {
                    String[] u = (String[]) o;
        %>
        <tr>
            <td><%= u[0] %></td>
            <td><%= u[1] %></td>
            <td><%= u[2] %></td>
            <td><%= (u[3] == null ? "-" : u[3]) %></td>
            <td><%= u[4] %></td>
            <td><%= u[5] %></td>
            <td>
                <a class="btn" style="padding:6px 10px;" href="<%= request.getContextPath() %>/bills/view?reservationId=<%= u[0] %>">Open Bill</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr><td colspan="7">No unpaid reservations.</td></tr>
        <% } %>
        </tbody>
    </table>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    </div>

    <% } else if (d != null && bill != null) { %>

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
        <a class="btn" href="<%= request.getContextPath() %>/bills/view">Billing Dashboard</a>
        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    </div>

    <% } else { %>
    <div class="error">Open a bill using a Reservation ID, or view the dashboard.</div>
    <a class="btn" href="<%= request.getContextPath() %>/bills/view">Billing Dashboard</a>
    <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    <% } %>

</div>

</body>
</html>