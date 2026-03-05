<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Room" %>
<!DOCTYPE html>
<html>
<head>
    <title>Rooms</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
</head>
<body>
<div class="app-shell">
    <div class="container">

        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Room Management</div>
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
            <h2 class="page-title">Room Management View</h2>
            <div class="page-subtitle">Use filters to view available, reserved, or maintenance rooms.</div>

            <div class="section">
                <div class="nav-actions" style="justify-content:flex-start;">
                    <a class="btn btn-slate" href="<%= request.getContextPath() %>/rooms">All Rooms</a>
                    <a class="btn btn-primary" href="<%= request.getContextPath() %>/rooms?status=AVAILABLE">Available Rooms</a>
                    <a class="btn btn-warning" href="<%= request.getContextPath() %>/rooms?status=RESERVED">Reserved Rooms</a>
                    <a class="btn btn-soft" href="<%= request.getContextPath() %>/rooms?status=MAINTENANCE">Maintenance Rooms</a>
                </div>
            </div>

            <%
                String selected = (String) request.getAttribute("selectedStatus");
                if (selected == null) selected = "ALL";
                List<Room> rooms = (List<Room>) request.getAttribute("rooms");
            %>

            <div class="page-subtitle" style="margin-top:12px;">
                Selected status: <b><%= selected %></b>
            </div>

            <table class="table">
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
                    if (rooms != null) {
                        for (Room r : rooms) {
                            String st = r.getStatus() == null ? "" : r.getStatus().toString();
                            String cls = "";
                            if ("AVAILABLE".equalsIgnoreCase(st)) cls = "status-available";
                            else if ("RESERVED".equalsIgnoreCase(st)) cls = "status-reserved";
                            else if ("MAINTENANCE".equalsIgnoreCase(st)) cls = "status-maintenance";
                %>
                <tr>
                    <td><%= r.getRoomId() %></td>
                    <td><%= r.getRoomNumber() %></td>
                    <td><%= r.getRoomTypeName() %></td>
                    <td class="<%= cls %>"><%= st %></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>

        </div>

    </div>
</div>
</body>
</html>