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
        :root{
            --bg: #f6f7fb;
            --card: #ffffff;
            --text: #1f2937;
            --muted: #6b7280;
            --border: #e5e7eb;
            --shadow: rgba(0,0,0,0.08);

            /* Change these two to match your home page colors if needed */
            --primary: #111827;
            --primaryHover: #000000;
        }

        * { box-sizing: border-box; }

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background: var(--bg);
            color: var(--text);
        }

        .page-wrap{
            min-height: 100vh;
            padding: 20px 14px;
        }

        .card {
            background: var(--card);
            padding: 22px;
            border-radius: 14px;
            border: 1px solid var(--border);
            max-width: 1100px;
            margin: 0 auto;
            box-shadow: 0 10px 30px var(--shadow);
        }

        .header{
            display:flex;
            align-items:flex-start;
            justify-content:space-between;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 14px;
        }

        h1 { margin: 0; font-size: 26px; }
        .subtitle { margin: 6px 0 0; color: var(--muted); }

        .msg {
            margin: 12px 0 16px;
            padding: 10px 12px;
            border-radius: 10px;
            font-weight: 700;
        }
        .error { background: #ffe5e7; color: #8a0010; border: 1px solid #ffb3bb; }
        .success { background: #e8fff1; color: #006b2d; border: 1px solid #a7f3c4; }

        .form-grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px 20px;
            margin-top: 10px;
        }

        .field label{
            display:block;
            margin-bottom: 6px;
            font-weight: 700;
        }

        input, select, textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid var(--border);
            border-radius: 10px;
            outline: none;
            background: #fff;
        }

        input:focus, select:focus, textarea:focus{
            border-color: #c7cdd8;
            box-shadow: 0 0 0 3px rgba(17,24,39,0.08);
        }

        textarea { height: 92px; resize: vertical; }

        .span-2 { grid-column: 1 / -1; }

        .note {
            color: var(--muted);
            font-size: 13px;
            margin-top: 6px;
        }

        .actions{
            margin-top: 16px;
            display:flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 10px 14px;
            border: 1px solid var(--primary);
            background: #fff;
            cursor: pointer;
            border-radius: 10px;
            text-decoration: none;
            color: var(--primary);
            display: inline-block;
            font-weight: 700;
        }

        .btn-primary{
            background: var(--primary);
            color: #fff;
        }

        .btn:hover { opacity: 0.92; }
        .btn-primary:hover { background: var(--primaryHover); }

        .bill-box {
            margin-top: 18px;
            padding: 16px;
            border: 1px solid var(--border);
            border-radius: 12px;
            background: #fafafa;
        }

        .bill-row { margin-bottom: 8px; }

        @media (max-width: 900px){
            .form-grid{ grid-template-columns: 1fr; }
            .span-2{ grid-column: auto; }
        }
    </style>
</head>

<body>
<div class="page-wrap">
    <div class="card">
        <div class="header">
            <div>
                <h1>Add Reservation</h1>
                <div class="subtitle">Create a new reservation for a guest.</div>
            </div>
        </div>

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
        <div class="msg error"><%= error %></div>
        <% } %>

        <% if (success != null) { %>
        <div class="msg success"><%= success %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/reservations/add">

            <div class="form-grid">

                <div class="field">
                    <label>First Name</label>
                    <input type="text" name="firstName" value="<%= firstName %>" required>
                </div>

                <div class="field">
                    <label>Last Name</label>
                    <input type="text" name="lastName" value="<%= lastName %>" required>
                </div>

                <div class="field">
                    <label>NIC / Passport</label>
                    <input type="text" name="nicPassport" value="<%= nicPassport %>">
                </div>

                <div class="field">
                    <label>Phone</label>
                    <input type="text" name="phone" value="<%= phone %>" required>
                </div>

                <div class="field">
                    <label>Email</label>
                    <input type="email" name="email" value="<%= email %>">
                </div>

                <div class="field span-2">
                    <label>Address</label>
                    <textarea name="address"><%= address %></textarea>
                </div>

                <div class="field">
                    <label>Room Type</label>
                    <select name="roomTypeId" id="roomTypeId" onchange="onRoomTypeChange()" required>
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

                <div class="field">
                    <label>Available Room (Optional)</label>
                    <select name="roomId" id="roomId">
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
                            <%= room.getRoomNumber() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <div class="note">Room list changes when you select a room type.</div>
                </div>

                <div class="field">
                    <label>Check-In Date</label>
                    <input type="date" name="checkIn" value="<%= checkIn %>" required>
                </div>

                <div class="field">
                    <label>Check-Out Date</label>
                    <input type="date" name="checkOut" value="<%= checkOut %>" required>
                </div>

                <div class="field">
                    <label>Number of Guests</label>
                    <input type="number" name="numGuests" min="1" value="<%= numGuests %>" required>
                </div>

                <div class="field span-2">
                    <label>Special Requests</label>
                    <textarea name="specialRequests"><%= specialRequests %></textarea>
                </div>

            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">Create Reservation</button>
                <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
                <a class="btn" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </form>

        <% if (generatedBill != null) { %>
        <div class="bill-box">
            <h2 style="margin-top:0;">Generated Bill</h2>
            <div class="bill-row"><strong>Reservation ID:</strong> <%= generatedBill.getReservationId() %></div>
            <div class="bill-row"><strong>Subtotal:</strong> LKR <%= generatedBill.getSubtotal() %></div>
            <div class="bill-row"><strong>Tax:</strong> LKR <%= generatedBill.getTaxAmount() %></div>
            <div class="bill-row"><strong>Discount:</strong> LKR <%= generatedBill.getDiscountAmount() %></div>
            <div class="bill-row"><strong>Total:</strong> LKR <%= generatedBill.getTotal() %></div>
            <div class="bill-row"><strong>Status:</strong> UNPAID</div>
        </div>
        <% } %>

    </div>
</div>

<script>
    function enc(v){ return encodeURIComponent(v || ''); }

    // Preserve typed form fields when changing room type (prevents wipe-out)
    function onRoomTypeChange() {
        const roomTypeId = document.getElementById('roomTypeId').value;

        const firstName = document.querySelector('[name="firstName"]').value;
        const lastName = document.querySelector('[name="lastName"]').value;
        const nicPassport = document.querySelector('[name="nicPassport"]').value;
        const phone = document.querySelector('[name="phone"]').value;
        const email = document.querySelector('[name="email"]').value;
        const address = document.querySelector('[name="address"]').value;
        const checkIn = document.querySelector('[name="checkIn"]').value;
        const checkOut = document.querySelector('[name="checkOut"]').value;
        const numGuests = document.querySelector('[name="numGuests"]').value;
        const specialRequests = document.querySelector('[name="specialRequests"]').value;
        const roomId = document.querySelector('[name="roomId"]').value;

        const base = '<%= request.getContextPath() %>/reservations/add';
        const qs =
            '?roomTypeId=' + enc(roomTypeId) +
            '&firstName=' + enc(firstName) +
            '&lastName=' + enc(lastName) +
            '&nicPassport=' + enc(nicPassport) +
            '&phone=' + enc(phone) +
            '&email=' + enc(email) +
            '&address=' + enc(address) +
            '&checkIn=' + enc(checkIn) +
            '&checkOut=' + enc(checkOut) +
            '&numGuests=' + enc(numGuests) +
            '&specialRequests=' + enc(specialRequests) +
            '&roomId=' + enc(roomId);

        window.location = base + qs;
    }
</script>

</body>
</html>