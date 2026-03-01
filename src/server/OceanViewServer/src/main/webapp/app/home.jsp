<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    // If you already store username or role in session, you can show it here safely:
    Object userId = session.getAttribute("userId");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Ocean View Admin Panel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        :root{
            --bg: #F5F7FB;
            --card: #FFFFFF;
            --text: #111827;
            --muted: #6B7280;
            --border: #E5E7EB;

            --primary: #2563EB;      /* Blue */
            --primary2: #1D4ED8;
            --success: #16A34A;      /* Green */
            --warning: #F59E0B;      /* Amber */
            --danger: #DC2626;       /* Red */
            --neutral: #111827;      /* Dark */
        }

        * { box-sizing: border-box; }
        body{
            margin:0;
            font-family: Arial, sans-serif;
            background: linear-gradient(180deg, #EEF2FF 0%, var(--bg) 35%, var(--bg) 100%);
            color: var(--text);
        }

        .wrap{
            max-width: 980px;
            margin: 48px auto;
            padding: 0 18px;
        }

        .topbar{
            display:flex;
            align-items:center;
            justify-content:space-between;
            margin-bottom: 18px;
        }

        .brand{
            display:flex;
            gap:12px;
            align-items:center;
        }

        .logo{
            width: 44px;
            height: 44px;
            border-radius: 12px;
            background: linear-gradient(135deg, var(--primary), #7C3AED);
            box-shadow: 0 10px 25px rgba(37,99,235,0.25);
        }

        .brand h1{
            font-size: 20px;
            margin: 0;
            line-height: 1.2;
        }

        .brand p{
            margin: 2px 0 0;
            color: var(--muted);
            font-size: 13px;
        }

        .pill{
            display:inline-flex;
            gap:8px;
            align-items:center;
            padding: 8px 12px;
            border: 1px solid var(--border);
            background: rgba(255,255,255,0.8);
            border-radius: 999px;
            color: var(--muted);
            font-size: 13px;
        }

        .card{
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 22px;
            box-shadow: 0 10px 30px rgba(17,24,39,0.06);
        }

        .header{
            display:flex;
            align-items:flex-start;
            justify-content:space-between;
            gap: 12px;
            margin-bottom: 16px;
        }

        .header h2{
            margin:0;
            font-size: 22px;
        }

        .header .sub{
            margin-top: 6px;
            color: var(--muted);
            font-size: 14px;
            max-width: 680px;
        }

        .grid{
            display:grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px;
            margin-top: 18px;
        }

        @media (max-width: 760px){
            .grid{ grid-template-columns: 1fr; }
            .topbar{ flex-direction:column; align-items:flex-start; gap: 10px; }
        }

        .tile{
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 16px;
            background: #FFFFFF;
            transition: transform .12s ease, box-shadow .12s ease;
        }
        .tile:hover{
            transform: translateY(-2px);
            box-shadow: 0 14px 30px rgba(17,24,39,0.08);
        }

        .tile h3{
            margin: 0 0 6px;
            font-size: 16px;
        }

        .tile p{
            margin: 0 0 12px;
            color: var(--muted);
            font-size: 13px;
            line-height: 1.4;
        }

        .btnrow{
            display:flex;
            flex-wrap:wrap;
            gap:10px;
        }

        .btn{
            display:inline-flex;
            align-items:center;
            justify-content:center;
            padding: 10px 12px;
            border-radius: 10px;
            border: 1px solid var(--border);
            text-decoration:none;
            font-size: 14px;
            color: var(--text);
            background: #fff;
            cursor:pointer;
        }
        .btn:hover{ border-color:#C7D2FE; }

        .btn-primary{
            background: var(--primary);
            color:#fff;
            border-color: transparent;
        }
        .btn-primary:hover{ background: var(--primary2); }

        .btn-success{
            background: var(--success);
            color:#fff;
            border-color: transparent;
        }

        .btn-warning{
            background: var(--warning);
            color:#111827;
            border-color: transparent;
        }

        .btn-danger{
            background: var(--danger);
            color:#fff;
            border-color: transparent;
        }

        .footer{
            margin-top: 14px;
            color: var(--muted);
            font-size: 12px;
            text-align: center;
        }

        .tiny{
            font-size: 12px;
            color: var(--muted);
        }
    </style>
</head>

<body>
<div class="wrap">

    <div class="topbar">
        <div class="brand">
            <div class="logo"></div>
            <div>
                <h1>Ocean View Resort</h1>
                <p>Staff Admin Panel</p>
            </div>
        </div>

        <div class="pill">
            ✅ Logged in
            <% if (userId != null) { %>
            <span class="tiny">| User ID: <%= userId %></span>
            <% } %>
        </div>
    </div>

    <div class="card">
        <div class="header">
            <div>
                <h2>Dashboard</h2>
                <div class="sub">
                    Use the shortcuts below to manage rooms, reservations, billing, and staff guidance.
                </div>
            </div>

            <div class="btnrow">
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <div class="grid">

            <!-- Rooms -->
            <div class="tile">
                <h3>Rooms</h3>
                <p>View all rooms and monitor availability status.</p>
                <div class="btnrow">
                    <a class="btn btn-primary" href="<%= request.getContextPath() %>/rooms">View Rooms</a>
                    <a class="btn" href="<%= request.getContextPath() %>/api/health">Tomcat Server Status</a>
                </div>
            </div>

            <!-- Add Reservation -->
            <div class="tile">
                <h3>Create Reservation</h3>
                <p>Create a new booking for a guest and assign a room (optional).</p>
                <div class="btnrow">
                    <a class="btn btn-success" href="<%= request.getContextPath() %>/reservations/add">Add Reservation</a>
                </div>
            </div>

            <!-- View Reservation / Search -->
            <div class="tile">
                <h3>Reservation Details</h3>
                <p>Search reservations by ID, code, NIC/Passport, phone, email, room number, date range, or today’s arrivals/departures.</p>
                <div class="btnrow">
                    <!-- If your search servlet is /reservations/search -->
                    <a class="btn btn-primary" href="<%= request.getContextPath() %>/reservations/search">Search Reservations</a>

                    <!-- If you want a direct open by ID example -->
                    <a class="btn" href="<%= request.getContextPath() %>/reservations/view?id=1">Open by ID</a>
                </div>
            </div>

            <!-- Billing -->
            <div class="tile">
                <h3>Billing</h3>
                <p>View and calculate bills based on nights and room rate for a reservation.</p>
                <div class="btnrow">
                    <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view">View Bills</a>
                </div>
            </div>

            <!-- Help -->
            <div class="tile">
                <h3>Help & Guidelines</h3>
                <p>Step-by-step guidance for new staff members on using the reservation system.</p>
                <div class="btnrow">
                    <a class="btn" href="<%= request.getContextPath() %>/help">Open Help</a>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="tile">
                <h3>Quick Actions</h3>
                <p>Use common daily views to speed up front-desk work.</p>
                <div class="btnrow">
                    <a class="btn" href="<%= request.getContextPath() %>/reservations/search?quick=arrivals">Today’s Arrivals</a>
                    <a class="btn" href="<%= request.getContextPath() %>/reservations/search?quick=departures">Today’s Departures</a>
                </div>
            </div>

        </div>

        <div class="footer">
            Ocean View Resort System • Java EE • Tomcat • WAMP/MySQL
        </div>
    </div>
</div>
</body>
</html>