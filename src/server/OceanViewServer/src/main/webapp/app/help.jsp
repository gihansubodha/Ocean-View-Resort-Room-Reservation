<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/02/26
  Time: 02:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Help & Guidelines</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root{
            --bg: #F5F7FB;
            --card: #FFFFFF;
            --text: #111827;
            --muted: #6B7280;
            --border: #E5E7EB;
            --primary: #2563EB;
            --primary2: #1D4ED8;
        }
        *{ box-sizing:border-box; }
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
        .card{
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 22px;
            box-shadow: 0 10px 30px rgba(17,24,39,0.06);
        }
        h1{ margin:0 0 6px; font-size: 22px; }
        .sub{ color: var(--muted); font-size: 14px; margin-bottom: 16px; }
        .section{
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 16px;
            margin-top: 14px;
            background: #fff;
        }
        .section h2{
            margin: 0 0 8px;
            font-size: 16px;
        }
        .section p, .section li{
            color: var(--muted);
            font-size: 13px;
            line-height: 1.5;
        }
        .btnrow{ display:flex; flex-wrap:wrap; gap:10px; margin-top: 16px; }
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
        }
        .btn-primary{
            background: var(--primary);
            color:#fff;
            border-color: transparent;
        }
        .btn-primary:hover{ background: var(--primary2); }
        code{
            background:#F3F4F6;
            padding:2px 6px;
            border-radius:6px;
            font-size: 12px;
            color:#111827;
        }
    </style>
</head>
<body>
<div class="wrap">
    <div class="card">
        <h1>Help & Guidelines</h1>
        <div class="sub">
            Quick guidance for staff on using the Ocean View reservation system.
        </div>

        <div class="section">
            <h2>1) Create a Reservation</h2>
            <ul>
                <li>Go to <code>Add Reservation</code>.</li>
                <li>Select a Room Type, then (optional) pick an available room.</li>
                <li>Enter Check-in / Check-out dates and guest details.</li>
                <li>Click <code>Create Reservation</code>. You can then view the reservation details.</li>
            </ul>
        </div>

        <div class="section">
            <h2>2) Find Reservation Details</h2>
            <ul>
                <li>Open <code>Reservation Details</code> from Home.</li>
                <li>Search by Reservation ID or Code for exact match.</li>
                <li>Or search by NIC/Passport, Phone, Email, Room Number, and check-in date range.</li>
                <li>Use <code>Today’s Arrivals</code> / <code>Today’s Departures</code> for daily operations.</li>
            </ul>
        </div>

        <div class="section">
            <h2>3) Billing</h2>
            <ul>
                <li>Open a reservation details page.</li>
                <li>Click <code>View Bill</code> to calculate cost based on nights × nightly rate.</li>
                <li>If the bill page asks for Reservation ID, use the ID shown in reservation details.</li>
            </ul>
        </div>

        <div class="section">
            <h2>4) Common Troubleshooting</h2>
            <ul>
                <li>If a page shows 404, verify the link exists and Tomcat is deployed with the latest build.</li>
                <li>If rooms don’t appear, ensure room type is selected and dates are valid.</li>
                <li>If login fails, confirm database connection / JNDI settings.</li>
            </ul>
        </div>

        <div class="btnrow">
            <a class="btn btn-primary" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
            <a class="btn" href="<%= request.getContextPath() %>/reservations/view">Go to Reservation Details</a>
        </div>
    </div>
</div>
</body>
</html>