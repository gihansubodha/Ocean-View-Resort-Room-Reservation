<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 02/28/26
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="com.oceanview.model.Room" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Reservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f7f7f7;
        }
        .card {
            background: #fff;
            padding: 24px;
            border-radius: 8px;
            border: 1px solid #ddd;
            max-width: 900px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        h1 {
            margin-top: 0;
        }
        .row {
            margin-bottom: 14px;
        }
        label {
            display: inline-block;
            width: 180px;
            vertical-align: top;
        }
        input, select, textarea {
            width: 260px;
            padding: 8px;
        }
        textarea {
            height: 80px;
        }
        .btn {
            padding: 10px 16px;
            border: 1px solid #333;
            background: #fff;
            cursor: pointer;
            border-radius: 6px;
            text-decoration: none;
            color: #333;
            display: inline-block;
            margin-right: 10px;
        }
        .btn:hover {
            background: #333;
            color: #fff;
        }
        .error {
            color: #b00020;
            font-weight: bold;
            margin-bottom: 14px;
        }
        .success {
            color: green;
            font-weight: bold;
            margin-bottom: 14px;
        }
    </style>
</head>
<body>
<div class="card">
    <h1>Add Reservation</h1>
    <p>Create a new reservation for a guest.</p>

    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
        if (error != null) {
    %>
    <div class="error"><%= error %></div>
    <%
        }
        if (success != null) {
    %>
    <div class="success"><%= success %></div>
    <%
        }
    %>

    <form method="post" action="<%= request.getContextPath() %>/reservations/add">
        <div class="row">
            <label>First Name:</label>
            <input type="text" name="firstName" required>
        </div>

        <div class="row">
            <label>Last Name:</label>
            <input type="text" name="lastName" required>
        </div>

        <div class="row">
            <label>NIC / Passport:</label>
            <input type="text" name="nicPassport">
        </div>

        <div class="row">
            <label>Phone:</label>
            <input type="text" name="phone" required>
        </div>

        <div class="row">
            <label>Email:</label>
            <input type="email" name="email">
        </div>

        <div class="row">
            <label>Address:</label>
            <textarea name="address"></textarea>
        </div>

        <div class="row">
            <label>Room Type:</label>
            <select name="roomTypeId" onchange="window.location='<%= request.getContextPath() %>/reservations/add?roomTypeId=' + this.value" required>
                <option value="">-- Select Room Type --</option>
                <%
                    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
                    Object selectedRoomTypeId = request.getAttribute("selectedRoomTypeId");
                    if (roomTypes != null) {
                        for (RoomType rt : roomTypes) {
                            boolean selected = selectedRoomTypeId != null &&
                                    Integer.parseInt(String.valueOf(selectedRoomTypeId)) == rt.getRoomTypeId();
                %>
                <option value="<%= rt.getRoomTypeId() %>" <%= selected ? "selected" : "" %>>
                    <%= rt.getTypeName() %> - LKR <%= rt.getNightlyRate() %>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="row">
            <label>Available Room:</label>
            <select name="roomId">
                <option value="">-- Optional: Select Available Room --</option>
                <%
                    List<Room> availableRooms = (List<Room>) request.getAttribute("availableRooms");
                    if (availableRooms != null) {
                        for (Room room : availableRooms) {
                %>
                <option value="<%= room.getRoomId() %>">
                    <%= room.getRoomNumber() %> - <%= room.getRoomTypeName() %>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="row">
            <label>Check-In Date:</label>
            <input type="date" name="checkIn" required>
        </div>

        <div class="row">
            <label>Check-Out Date:</label>
            <input type="date" name="checkOut" required>
        </div>

        <div class="row">
            <label>Number of Guests:</label>
            <input type="number" name="numGuests" min="1" required>
        </div>

        <div class="row">
            <label>Special Requests:</label>
            <textarea name="specialRequests"></textarea>
        </div>

        <div class="row">
            <button class="btn" type="submit">Create Reservation</button>
            <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
            <a class="btn" href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
    </form>
</div>
</body>
</html>
