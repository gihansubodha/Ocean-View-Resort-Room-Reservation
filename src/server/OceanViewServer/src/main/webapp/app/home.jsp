<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.oceanview.model.User" %>

<%
    User authUser = (User) session.getAttribute("authUser");
    if (authUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login");
        return;
    }

    String username = authUser.getUsername();

    String toastSuccess = (String) session.getAttribute("toastSuccess");
    if (toastSuccess != null) {
        session.removeAttribute("toastSuccess");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Ocean View Staff Panel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <style>
        /* Home tiles only (keeps global CSS clean) */
        .grid{ display:grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 14px; margin-top: 14px; }
        @media (max-width: 860px){ .grid{ grid-template-columns: 1fr; } }
        .tile{
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 16px;
            background: #fff;
            box-shadow: 0 10px 26px rgba(17,24,39,0.05);
            transition: transform .12s ease, box-shadow .12s ease;
        }
        .tile:hover{ transform: translateY(-2px); box-shadow: 0 16px 32px rgba(17,24,39,0.08); }
        .tile h3{ margin:0 0 6px; font-size: 16px; }
        .tile p{ margin:0 0 12px; color: var(--muted); font-size: 13px; line-height:1.45; }
        .btnrow{ display:flex; flex-wrap:wrap; gap:10px; }
        .footer{ margin-top: 14px; color: var(--muted); font-size: 12px; text-align:center; }

        .toast-success{
            border-left: 6px solid var(--success);
        }
    </style>
</head>

<body>
<div class="app-shell">
    <div class="container">

        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Staff Panel</div>
                </div>
            </div>

            <div class="nav-actions">
                <div class="pill">
                    ✅ Logged in as <strong><%= username %></strong>
                </div>
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Dashboard</h2>
            <div class="page-subtitle">Manage rooms, reservations, billing, and staff guidance using the shortcuts below.</div>

            <div class="grid">

                <div class="tile">
                    <h3>Rooms</h3>
                    <p>View all rooms and monitor availability status.</p>
                    <div class="btnrow">
                        <a class="btn btn-primary" href="<%= request.getContextPath() %>/rooms">View Rooms</a>
                    </div>
                </div>

                <div class="tile">
                    <h3>Create Reservation</h3>
                    <p>Create a new booking for a guest and assign a room (optional).</p>
                    <div class="btnrow">
                        <a class="btn btn-success" href="<%= request.getContextPath() %>/reservations/add">Add Reservation</a>
                    </div>
                </div>

                <div class="tile">
                    <h3>Reservations & Bills</h3>
                    <p>Search reservations by ID, code, NIC/Passport, phone, email, room number, date range, or today’s arrivals/departures.</p>
                    <p>Navigate to bills.</p>
                    <div class="btnrow">
                        <a class="btn btn-primary" href="<%= request.getContextPath() %>/reservations/view">Search Reservations</a>
                    </div>
                </div>

                <div class="tile">
                    <h3>Payments</h3>
                    <p>View payment information.</p>
                    <div class="btnrow">
                        <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view">View Bills</a>
                    </div>
                </div>

                <div class="tile">
                    <h3>Help & Guidelines</h3>
                    <p>Step-by-step guidance for new staff members on using the reservation system.</p>
                    <div class="btnrow">
                        <a class="btn btn-soft" href="<%= request.getContextPath() %>/help">Open Help</a>
                    </div>
                </div>

                <div class="tile">
                    <h3>Quick Actions</h3>
                    <p>Use common daily views to speed up front-desk work.</p>
                    <div class="btnrow">
                        <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view?quick=arrivals">Today’s Arrivals</a>
                        <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view?quick=departures">Today’s Departures</a>
                    </div>
                </div>

            </div>

            <div class="footer">Ocean View Resort System • Java EE • Tomcat • WAMP/MySQL</div>
        </div>

    </div>
</div>

<% if (toastSuccess != null) { %>
<div id="toastSuccess" class="toast toast-success">
    <button class="close" onclick="document.getElementById('toastSuccess').style.display='none'">&times;</button>
    <h3>Success</h3>
    <div class="line"><%= toastSuccess %></div>
</div>

<script>
    (function () {
        var toast = document.getElementById("toastSuccess");
        if (toast) {
            toast.style.display = "block";
            setTimeout(function () {
                if (toast) {
                    toast.style.display = "none";
                }
            }, 3000);
        }
    })();
</script>
<% } %>

</body>
</html>