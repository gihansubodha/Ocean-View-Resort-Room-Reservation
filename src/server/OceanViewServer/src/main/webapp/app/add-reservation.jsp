<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 02/28/26
  Time: 20:04
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="com.oceanview.model.Room" %>
<%@ page import="com.oceanview.model.Bill" %>

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
            max-width: 950px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        h1, h2 {
            margin-top: 0;
        }
        .row {
            margin-bottom: 14px;
        }
        label {
            display: inline-block;
            width: 180px;
            vertical-align: top;
            font-weight: bold;
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
        .bill-box {
            margin-top: 24px;
            padding: 16px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background: #fafafa;
        }
        .bill-row {
            margin-bottom: 10px;
        }
        .note {
            color: #666;
            font-size: 13px;
            margin-top: 6px;
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
        Bill generatedBill = (Bill) request.getAttribute("generatedBill");

        String firstName = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
        String lastName = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";
        String nicPassport = request.getParameter("nicPassport") != null ? request.getParameter("nicPassport") : "";
        String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
        String email = request.getParameter("email") != null ? request.getParameter("email") : "";
        String address = request.getParameter("address") != null ? request.getParameter("address") : "";
        String checkIn = request.getParameter("checkIn") != null ? request.getParameter("checkIn") : "";
        String checkOut = request.getParameter("checkOut") != null ? request.getParameter("checkOut") : "";
        String numGuests = request.getParameter("numGuests") != null ? request.getParameter("numGuests") : "";
        String specialRequests = request.getParameter("specialRequests") != null ? request.getParameter("specialRequests") : "";
        String selectedRoomIdParam = request.getParameter("roomId") != null ? request.getParameter("roomId") : "";
    %>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (success != null) { %>
    <div class="success"><%= success %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/reservations/add">
        <div class="row">
            <label>Room Type:</label>
            <select name="roomTypeId"
                    onchange="window.location='<%= request.getContextPath() %>/reservations/add?roomTypeId=' + this.value"
                    required>
                <option value="">-- Select Room Type --</option>
                <%
                    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
                    Object selectedRoomTypeId = request.getAttribute("selectedRoomTypeId");

                    if (selectedRoomTypeId == null && request.getParameter("roomTypeId") != null && !request.getParameter("roomTypeId").isBlank()) {
                        selectedRoomTypeId = request.getParameter("roomTypeId");
                    }

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
                            boolean selectedRoom = selectedRoomIdParam != null &&
                                    !selectedRoomIdParam.isBlank() &&
                                    Integer.parseInt(selectedRoomIdParam) == room.getRoomId();
                %>
                <option value="<%= room.getRoomId() %>" <%= selectedRoom ? "selected" : "" %>>
                    <%= room.getRoomNumber() %> - <%= room.getRoomTypeName() %>
                </option>
                <%
                        }
                    }
                %>
            </select>
            <div class="note">Room list changes when you select a room type.</div>
        </div>

        <div class="row">
            <label>Check-In Date:</label>
            <input type="date" name="checkIn" value="<%= checkIn %>" required>
        </div>

        <div class="row">
            <label>Check-Out Date:</label>
            <input type="date" name="checkOut" value="<%= checkOut %>" required>
        </div>

        <div class="row">
            <label>First Name:</label>
            <input type="text" name="firstName" value="<%= firstName %>" required>
        </div>

        <div class="row">
            <label>Last Name:</label>
            <input type="text" name="lastName" value="<%= lastName %>" required>
        </div>

        <div class="row">
            <label>NIC / Passport:</label>
            <input type="text" name="nicPassport" value="<%= nicPassport %>">
        </div>

        <div class="row">
            <label>Phone:</label>
            <input type="text" name="phone" value="<%= phone %>" required>
        </div>

        <div class="row">
            <label>Email:</label>
            <input type="email" name="email" value="<%= email %>">
        </div>

        <div class="row">
            <label>Address:</label>
            <textarea name="address"><%= address %></textarea>
        </div>

        <div class="row">
            <label>Number of Guests:</label>
            <input type="number" name="numGuests" min="1" value="<%= numGuests %>" required>
        </div>

        <div class="row">
            <label>Special Requests:</label>
            <textarea name="specialRequests"><%= specialRequests %></textarea>
        </div>

        <div class="row">
            <button class="btn" type="submit">Create Reservation</button>
            <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
            <a class="btn" href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
    </form>

    <% if (generatedBill != null) { %>
    <div class="bill-box">
        <h2>Generated Bill</h2>

        <div class="bill-row"><strong>Reservation ID:</strong> <%= generatedBill.getReservationId() %></div>
        <div class="bill-row"><strong>Subtotal:</strong> LKR <%= generatedBill.getSubtotal() %></div>
        <div class="bill-row"><strong>Tax:</strong> LKR <%= generatedBill.getTaxAmount() %></div>
        <div class="bill-row"><strong>Discount:</strong> LKR <%= generatedBill.getDiscountAmount() %></div>
        <div class="bill-row"><strong>Total:</strong> LKR <%= generatedBill.getTotal() %></div>
        <div class="bill-row"><strong>Status:</strong> UNPAID</div>
    </div>
    <% } %>
</div>
</body>
</html>