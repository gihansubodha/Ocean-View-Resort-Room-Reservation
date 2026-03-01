<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/01/26
  Time: 09:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Help - Reservation System</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }
        .card { background: #fff; padding: 24px; border-radius: 8px; border: 1px solid #ddd; max-width: 950px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        .btn { padding: 10px 16px; border: 1px solid #333; background: #fff; cursor: pointer; border-radius: 6px;
            text-decoration: none; color: #333; display: inline-block; margin-right: 10px; }
        .btn:hover { background: #333; color: #fff; }
        h2 { margin-top: 18px; }
        ul { line-height: 1.6; }
        code { background: #f2f2f2; padding: 2px 6px; border-radius: 4px; }
    </style>
</head>
<body>
<div class="card">
    <h1>Help - OceanView Reservation System</h1>
    <p>This page provides quick guidance for new staff members.</p>

    <h2>1) Create a Reservation</h2>
    <ul>
        <li>Go to <code>Add Reservation</code> page.</li>
        <li>Enter guest details. If NIC/Passport matches an existing guest, the system reuses that record.</li>
        <li>Select a <code>Room Type</code> to load available rooms.</li>
        <li>Optionally assign a specific room. If a room is assigned, its status will become <code>RESERVED</code>.</li>
        <li>Enter check-in/check-out dates (check-out must be after check-in).</li>
        <li>Click <code>Create Reservation</code>.</li>
    </ul>

    <h2>2) View Reservation Details</h2>
    <ul>
        <li>Open the Reservation Details page.</li>
        <li>Enter the reservation ID and click <code>Search</code>.</li>
        <li>The system displays booking, guest, and room information in one page.</li>
    </ul>

    <h2>3) View and Calculate Bills</h2>
    <ul>
        <li>Open the Bill page.</li>
        <li>Enter the reservation ID and click <code>Calculate</code>.</li>
        <li>The bill is calculated as: <code>nights × nightly rate</code> (based on room type rate).</li>
    </ul>

    <h2>4) Common Issues</h2>
    <ul>
        <li>If rooms do not appear: ensure the room type is selected and rooms have status <code>AVAILABLE</code>.</li>
        <li>If dates are invalid: ensure check-out is after check-in.</li>
    </ul>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
    </div>
</div>
</body>
</html>
