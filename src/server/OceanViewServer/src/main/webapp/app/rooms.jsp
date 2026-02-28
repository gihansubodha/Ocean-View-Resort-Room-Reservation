<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 02/28/26
  Time: 19:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Room" %>
<!DOCTYPE html>
<html>
<head>
    <title>Rooms</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f7f7f7;
            color: #222;
        }
        .card {
            background: #fff;
            padding: 24px;
            border-radius: 8px;
            border: 1px solid #ddd;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        h1 {
            margin-top: 0;
        }
        .filters a, .actions a {
            display: inline-block;
            margin-right: 10px;
            margin-bottom: 10px;
            padding: 8px 14px;
            text-decoration: none;
            border: 1px solid #333;
            border-radius: 6px;
            color: #333;
            background: #fff;
        }
        .filters a:hover, .actions a:hover {
            background: #333;
            color: #fff;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: #fff;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        th {
            background: #efefef;
        }
        .meta {
            margin-top: 10px;
            color: #666;
        }
        .status-available {
            color: green;
            font-weight: bold;
        }
        .status-maintenance {
            color: darkorange;
            font-weight: bold;
        }
        .status-reserved {
            color: crimson;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="card">
    <h1>Room Management View</h1>
    <p>Use the filters below to view available, reserved, or maintenance rooms.</p>

    <div class="filters">
        <a href="<%= request.getContextPath() %>/rooms">All Rooms</a>
        <a href="<%= request.getContextPath() %>/rooms?status=AVAILABLE">Available Rooms</a>
        <a href="<%= request.getContextPath() %>/rooms?status=RESERVED">Reserved Rooms</a>
        <a href="<%= request.getContextPath() %>/rooms?status=MAINTENANCE">Maintenance Rooms</a>
    </div>

    <div class="actions">
        <a href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>

    <div class="meta">
        Selected status:
        <strong>
            <%= request.getAttribute("selectedStatus") == null || String.valueOf(request.getAttribute("selectedStatus")).isBlank()
                    ? "ALL"
                    : request.getAttribute("selectedStatus") %>
        </strong>
    </div>

    <%
        List<Room> rooms = (List<Room>) request.getAttribute("rooms");
        if (rooms == null || rooms.isEmpty()) {
    %>
    <p>No rooms found for this filter.</p>
    <%
    } else {
    %>

    <table>
        <thead>
        <tr>
            <th>Room ID</th>
            <th>Room Number</th>
            <th>Room Type</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Room room : rooms) {
                String cssClass = "";
                if ("AVAILABLE".equalsIgnoreCase(room.getStatus())) {
                    cssClass = "status-available";
                } else if ("MAINTENANCE".equalsIgnoreCase(room.getStatus())) {
                    cssClass = "status-maintenance";
                } else if ("RESERVED".equalsIgnoreCase(room.getStatus())) {
                    cssClass = "status-reserved";
                }
        %>
        <tr>
            <td><%= room.getRoomId() %></td>
            <td><%= room.getRoomNumber() %></td>
            <td><%= room.getRoomTypeName() %></td>
            <td class="<%= cssClass %>"><%= room.getStatus() %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <%
        }
    %>
</div>
</body>
</html>