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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- UI SYSTEM -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <style>
        /* page-only tweaks (no logic changes) */
        .kv{
            display:grid;
            grid-template-columns: 200px 1fr;
            gap: 10px 14px;
            margin-top: 10px;
            align-items:center;
        }
        @media (max-width: 720px){
            .kv{ grid-template-columns: 1fr; }
        }
        .k{ color: var(--muted); font-size: 13px; font-weight: 800; }
        .v{ color: var(--text); font-size: 14px; }
        .mutedline{ color: var(--muted); font-size: 13px; line-height: 1.55; margin-top: 6px; }
        .btnrow{ display:flex; flex-wrap:wrap; gap:10px; margin-top: 14px; }
    </style>
</head>
<body>

<div class="app-shell">
    <div class="container">

        <!-- Top bar (UI only) -->
        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Reservation Confirmation</div>
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
            <h2 class="page-title">Advance Payment / ID Confirmation</h2>
            <div class="page-subtitle">
                Confirm the reservation either by verifying the guest identification document or by paying the required advance.
            </div>

            <% if (error != null) { %>
            <div class="alert alert-err" style="margin-top:12px;"><%= error %></div>
            <% } %>

            <% if (d != null && bill != null) { %>

            <!-- Reservation -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Reservation</h3>
                <div class="kv">
                    <div class="k">Reservation ID</div>
                    <div class="v"><b><%= d.getReservationId() %></b></div>

                    <div class="k">Code</div>
                    <div class="v"><%= d.getReservationCode() %></div>

                    <div class="k">Status</div>
                    <div class="v"><b><%= d.getReservationStatus() %></b></div>
                </div>
            </div>

            <!-- Option 1 -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Option 1: Confirm using Identification</h3>
                <div class="mutedline">Enter the same document number you used during reservation creation.</div>

                <form method="post" action="<%= request.getContextPath() %>/reservations/advance" style="margin-top:12px;">
                    <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>

                    <div class="form-grid" style="margin-top: 10px;">
                        <div>
                            <label>Document Type</label>
                            <select name="docType" required>
                                <option value="NIC">NIC</option>
                                <option value="DRIVING">Driving Licence</option>
                                <option value="PASSPORT">Passport</option>
                            </select>
                        </div>

                        <div>
                            <label>Document Number</label>
                            <input type="text" name="docNumber" required />
                        </div>
                    </div>

                    <div class="btnrow">
                        <button class="btn btn-primary" type="submit">Verify & Confirm</button>
                        <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back</a>
                    </div>
                </form>
            </div>

            <!-- Option 2 -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Option 2: Pay Advance (20%)</h3>
                <div class="kv">
                    <div class="k">Bill Total</div>
                    <div class="v">LKR <%= bill.getTotal() %></div>

                    <div class="k">Advance Required</div>
                    <div class="v"><b>LKR <%= advanceRequired %></b></div>
                </div>

                <div class="mutedline">Pay at least 20% on the Bill page to confirm automatically.</div>

                <div class="btnrow">
                    <a class="btn btn-warning"
                       href="<%= request.getContextPath() %>/bills/view?reservationId=<%= d.getReservationId() %>&toast=advancePayRequired">
                        Go to Bill & Pay
                    </a>
                    <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view?id=<%= d.getReservationId() %>">Back</a>
                </div>
            </div>

            <% } %>
        </div>
    </div>
</div>

</body>
</html>