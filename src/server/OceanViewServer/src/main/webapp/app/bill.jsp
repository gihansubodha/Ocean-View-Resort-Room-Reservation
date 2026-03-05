<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>
<%@ page import="com.oceanview.model.Bill" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bill</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <style>
        /* Bill page small extras */
        .split {
            display: grid;
            grid-template-columns: 1.2fr 0.8fr;
            gap: 14px;
            margin-top: 14px;
        }
        @media (max-width: 980px) {
            .split { grid-template-columns: 1fr; }
        }
        .kv {
            display: grid;
            grid-template-columns: 220px 1fr;
            gap: 10px;
            align-items: center;
            margin: 8px 0;
        }
        @media (max-width: 760px) {
            .kv { grid-template-columns: 1fr; }
        }
        .pill2{
            display:inline-flex;
            align-items:center;
            padding:6px 10px;
            border-radius:999px;
            border:1px solid var(--border);
            background:#F9FAFB;
            font-size:12px;
            font-weight:800;
            color:#0F172A;
        }
        .money { font-weight:800; }
        .totalBox{
            display:flex;
            align-items:center;
            justify-content:space-between;
            gap:10px;
            padding:14px;
            border-radius:14px;
            border:1px solid var(--border);
            background:#F9FAFB;
            margin-top: 14px;
        }
        .totalBox .t1{ font-size:13px; color:var(--muted); margin:0; }
        .totalBox .t2{ font-size:20px; font-weight:900; margin:0; }
        .actionsRow{
            display:flex;
            flex-wrap:wrap;
            gap:10px;
            margin-top: 14px;
            justify-content:flex-start;
        }
    </style>
</head>

<body>

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

<div class="app-shell">
    <div class="container">

        <!-- Top Bar -->
        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub"><%= dashboardMode ? "Billing Dashboard" : "Bill & Payments" %></div>
                </div>
            </div>

            <div class="nav-actions">
                <!-- Always available -->
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp">Home</a>

                <!-- Context-aware actions -->
                <% if (!dashboardMode) { %>
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view">Billing Dashboard</a>
                <% if (d != null) { %>
                <a class="btn btn-primary" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back to Reservation</a>
                <% } %>
                <% } else { %>
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <div class="pill">💳 Today’s overview</div>
                <% } %>

                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Bill</h2>
            <div class="page-subtitle">
                <%= dashboardMode ? "Track today’s payments and unpaid/partial reservations." : "Review charges and accept payments securely." %>
            </div>

            <!-- Alerts (kept your exact parameter logic) -->
            <% if ("1".equals(paid)) { %>
            <div class="alert alert-ok">Payment recorded successfully.</div>
            <% } %>

            <% if ("paymentRequired".equals(toast)) { %>
            <div class="alert alert-err">Payment required before Check-out.</div>
            <% } %>

            <% if ("paidSuccess".equals(toast)) { %>
            <div class="alert alert-ok">Paid successfully.</div>
            <% } %>

            <% if ("advancePayRequired".equals(toast)) { %>
            <div class="alert alert-err">Advance payment required: please pay at least 20% to confirm reservation.</div>
            <% } %>

            <% if (errorParam != null && !errorParam.isBlank()) { %>
            <div class="alert alert-err"><%= errorParam %></div>
            <% } %>

            <% if (error != null && !error.isBlank()) { %>
            <div class="alert alert-err"><%= error %></div>
            <% } %>

            <% if (dashboardMode) { %>

            <!-- Dashboard Mode -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Today’s Payments</h3>

                <table class="table">
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
            </div>

            <div class="section">
                <h3 style="margin:0; font-size:16px;">Unpaid / Partial Reservations</h3>

                <table class="table">
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
                            <a class="btn btn-primary" style="padding:8px 12px;"
                               href="<%= request.getContextPath() %>/bills/view?reservationId=<%= u[0] %>">Open Bill</a>
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
            </div>

            <% } else if (d != null && bill != null) { %>

            <!-- Bill Detail Mode -->
            <div class="split">

                <!-- LEFT: Reservation + Guest + Charges -->
                <div class="section">
                    <h3 style="margin:0 0 10px; font-size:16px;">Reservation</h3>

                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Reservation ID</div><div><b><%= d.getReservationId() %></b></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Code</div><div><b><%= d.getReservationCode() %></b></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Check-in</div><div><b><%= d.getCheckIn() %></b></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Check-out</div><div><b><%= d.getCheckOut() %></b></div></div>

                    <hr class="sep">

                    <h3 style="margin:0 0 10px; font-size:16px;">Guest</h3>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Name</div><div><b><%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></b></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Phone</div><div><b><%= d.getGuestPhone() %></b></div></div>

                    <hr class="sep">

                    <h3 style="margin:0 0 10px; font-size:16px;">Charges</h3>

                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Room Type</div><div><b><%= d.getRoomTypeName() %></b></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Nightly Rate</div><div class="money">LKR <%= bill.getNightlyRate() %></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Nights</div><div><b><%= bill.getNights() %></b></div></div>

                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Subtotal</div><div class="money">LKR <%= bill.getSubtotal() %></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Discount</div><div class="money">LKR <%= bill.getDiscountAmount() %></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Tax</div><div class="money">LKR <%= bill.getTaxAmount() %></div></div>

                    <div class="totalBox">
                        <div>
                            <p class="t1">Total</p>
                            <p class="t2">LKR <%= bill.getTotal() %></p>
                        </div>
                        <div class="pill2"><%= bill.getBillStatus() %></div>
                    </div>
                </div>

                <!-- RIGHT: Payment -->
                <div class="section">
                    <h3 style="margin:0 0 10px; font-size:16px;">Payment</h3>

                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Status</div><div><span class="pill2"><%= bill.getBillStatus() %></span></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Paid Total</div><div class="money">LKR <%= bill.getPaidTotal() %></div></div>
                    <div class="kv"><div style="color:var(--muted);font-size:13px;">Balance</div><div class="money">LKR <%= bill.getBalance() %></div></div>

                    <%
                        java.math.BigDecimal bal = bill.getBalance();
                        boolean balancePositive = (bal != null && bal.compareTo(java.math.BigDecimal.ZERO) > 0);
                        boolean isPaid = "PAID".equalsIgnoreCase(bill.getBillStatus());
                    %>

                    <% if (balancePositive && !isPaid) { %>
                    <form method="post" action="<%= request.getContextPath() %>/payments/add" style="margin-top:14px;">
                        <input type="hidden" name="reservationId" value="<%= d.getReservationId() %>"/>
                        <input type="hidden" name="billId" value="<%= bill.getBillId() %>"/>

                        <div style="display:grid; gap:12px; margin-top: 12px;">
                            <div>
                                <label>Payment Method</label>
                                <select name="method" required>
                                    <option value="CASH">CASH</option>
                                    <option value="CARD">CARD</option>
                                    <option value="TRANSFER">TRANSFER</option>
                                </select>
                            </div>

                            <div>
                                <label>Amount</label>
                                <input type="number" name="amount" min="1" step="0.01" required>
                            </div>

                            <button class="btn btn-success" type="submit">Accept Payment</button>
                        </div>
                    </form>
                    <% } else { %>
                    <div class="alert alert-ok" style="margin-top:14px;">
                        No payment required (Bill is fully paid).
                    </div>
                    <% } %>
                </div>
            </div>

            <% } else { %>

            <!-- No Data -->
            <div class="alert alert-err" style="margin-top:14px;">
                Open a bill using a Reservation ID, or view the dashboard.
            </div>

            <div class="actionsRow">
                <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view">Billing Dashboard</a>
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
            </div>

            <% } %>

        </div>
    </div>
</div>

</body>
</html>