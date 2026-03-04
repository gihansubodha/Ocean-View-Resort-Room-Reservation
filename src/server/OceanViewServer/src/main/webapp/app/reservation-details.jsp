<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>

<!DOCTYPE html>
<html>
<head>
    <title>Reservation Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }
        .card {
            background: #fff;
            padding: 24px;
            border-radius: 10px;
            border: 1px solid #ddd;
            max-width: 1100px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        .row { margin: 10px 0; }
        .label { display:inline-block; width: 180px; font-weight: bold; }
        .btn {
            padding: 10px 16px;
            border: 1px solid #333;
            background: #fff;
            border-radius: 8px;
            text-decoration: none;
            color: #333;
            display: inline-block;
            margin-right: 10px;
            cursor: pointer;
        }
        .btn:hover { background: #333; color: #fff; }
        .error { color: #b00020; font-weight: bold; margin: 14px 0; }
        .muted { color:#666; margin-top:6px; }
        .section-title { margin-top: 22px; }
        input, select { padding: 8px; border: 1px solid #ccc; border-radius: 8px; }
        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            min-width: 320px;
            background: #fff;
            color: #111;
            border-radius: 12px;
            padding: 18px 20px;
            box-shadow: 0 10px 24px rgba(0,0,0,0.18);
            display: none;
            z-index: 9999;
            border: 1px solid #ddd;
        }
        .toast h3 { margin: 0 0 8px 0; font-size: 18px; }
        .toast .line { margin: 4px 0; font-size: 14px; }
        .toast .close {
            float: right;
            background: transparent;
            border: none;
            color: #111;
            font-size: 18px;
            cursor: pointer;
        }
        .grid { display:flex; flex-wrap:wrap; gap:10px; }
        .grid .field { display:flex; flex-direction:column; }
        .grid input { width: 220px; }
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #fafafa; }
    </style>
</head>
<body>

<%
    String error = (String) request.getAttribute("error");
    if (error == null || error.trim().isEmpty()) {
        error = request.getParameter("error");
    }

    ReservationDetails d = (ReservationDetails) request.getAttribute("details");
    List<ReservationDetails> results = (List<ReservationDetails>) request.getAttribute("results");
    String resultsTitle = (String) request.getAttribute("resultsTitle");

    boolean created = "1".equals(request.getParameter("created"));
    String toastParam = request.getParameter("toast");
%>

<div class="toast" id="toast">
    <button class="close" onclick="hideToast()">×</button>
    <h3>Reservation created successfully</h3>
    <div class="line" id="tCode"></div>
    <div class="line" id="tRoom"></div>
    <div class="line" id="tDates"></div>
</div>

<div class="toast" id="actionToast" style="display:none;">
    <button class="close" onclick="hideActionToast()">×</button>
    <h3 id="actionTitle">Done</h3>
    <div class="line" id="actionMsg"></div>
</div>

<div class="card">
    <h1>Reservation Details</h1>
    <div class="muted">Search by Reservation ID, Code, NIC/Passport, Phone, Email, Room number, check-in date range — or use Today’s lists.</div>

    <form method="get" action="<%= request.getContextPath() %>/reservations/view" style="margin-top:14px;">
        <div class="grid">
            <div class="field">
                <label>Reservation ID</label>
                <input type="number" name="id" min="1" placeholder="e.g., 1">
            </div>

            <div class="field">
                <label>Reservation Code</label>
                <input type="text" name="code" placeholder="e.g., RES-20260301-ABC123">
            </div>

            <div class="field">
                <label>NIC/Passport</label>
                <input type="text" name="nic" placeholder="e.g., 9527xxxxxxV">
            </div>

            <div class="field">
                <label>Phone</label>
                <input type="text" name="phone" placeholder="e.g., 077xxxxxxx">
            </div>

            <div class="field">
                <label>Email</label>
                <input type="text" name="email" placeholder="e.g., user@email.com">
            </div>

            <div class="field">
                <label>Room Number</label>
                <input type="text" name="roomNumber" placeholder="e.g., 201">
            </div>

            <div class="field">
                <label>Check-in From</label>
                <input type="date" name="from">
            </div>

            <div class="field">
                <label>Check-in To</label>
                <input type="date" name="to">
            </div>
        </div>

        <div style="margin-top:12px;">
            <button class="btn" type="submit">Search</button>
            <a class="btn" href="<%= request.getContextPath() %>/reservations/view?quick=arrivals">Today’s Arrivals</a>
            <a class="btn" href="<%= request.getContextPath() %>/reservations/view?quick=departures">Today’s Departures</a>
            <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
        </div>
    </form>

    <% if (error != null && !error.trim().isEmpty()) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% if (results != null && !results.isEmpty()) { %>
    <h2 class="section-title"><%= (resultsTitle != null ? resultsTitle : "Search Results") %></h2>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Guest</th>
            <th>Phone</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Room</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (ReservationDetails r : results) { %>
        <tr>
            <td><%= r.getReservationId() %></td>
            <td><%= r.getReservationCode() %></td>
            <td><%= r.getGuestFirstName() %> <%= r.getGuestLastName() %></td>
            <td><%= r.getGuestPhone() %></td>
            <td><%= r.getCheckIn() %></td>
            <td><%= r.getCheckOut() %></td>
            <td><%= (r.getRoomNumber() == null ? "-" : r.getRoomNumber()) %></td>
            <td>
                <a class="btn" style="padding:6px 10px;" href="<%= request.getContextPath() %>/reservations/view?id=<%= r.getReservationId() %>">Open</a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>

    <% if (d != null) { %>
    <h2 class="section-title">Booking</h2>

    <div class="row"><span class="label">Code:</span> <span id="vCode"><%= d.getReservationCode() %></span></div>
    <div class="row"><span class="label">Check-in:</span> <span id="vIn"><%= d.getCheckIn() %></span></div>
    <div class="row"><span class="label">Check-out:</span> <span id="vOut"><%= d.getCheckOut() %></span></div>
    <div class="row"><span class="label">Guests:</span> <%= d.getNumGuests() %></div>
    <div class="row"><span class="label">Status:</span> <%= d.getReservationStatus() %></div>
    <div class="row"><span class="label">Special requests:</span> <%= d.getSpecialRequests() %></div>

    <h2 class="section-title">Guest</h2>
    <div class="row"><span class="label">Name:</span> <%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></div>
    <div class="row"><span class="label">Phone:</span> <%= d.getGuestPhone() %></div>
    <div class="row"><span class="label">Email:</span> <%= d.getGuestEmail() %></div>
    <div class="row"><span class="label">NIC/Passport:</span> <%= d.getGuestNicPassport() %></div>

    <h2 class="section-title">Room</h2>
    <div class="row"><span class="label">Room Type:</span> <%= d.getRoomTypeName() %> (ID: <%= d.getRoomTypeId() %>)</div>
    <div class="row"><span class="label">Nightly Rate:</span> LKR <%= d.getNightlyRate() %></div>
    <div class="row"><span class="label">Room Assigned:</span>
        <span id="vRoom"><%= (d.getRoomId() == null ? "Not assigned" : (d.getRoomNumber() + " (Room ID: " + d.getRoomId() + ")")) %></span>
    </div>

    <div style="margin-top:16px;">
        <a class="btn" href="<%= request.getContextPath() %>/bills/view?reservationId=<%= d.getReservationId() %>">View Bill</a>

        <% if ("PENDING".equalsIgnoreCase(d.getReservationStatus())) { %>
        <form method="post" action="<%= request.getContextPath() %>/reservations/confirm" style="display:inline;">
            <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
            <button class="btn" type="submit">Confirm</button>
        </form>
        <% } %>

        <% if (!"CHECKED_OUT".equalsIgnoreCase(d.getReservationStatus()) && !"CANCELLED".equalsIgnoreCase(d.getReservationStatus())) { %>
        <form method="post" action="<%= request.getContextPath() %>/reservations/cancel" style="display:inline;" onsubmit="return confirmCancelReservation();">
            <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
            <button class="btn" type="submit">Cancel</button>
        </form>
        <% } %>

        <% if ("CONFIRMED".equalsIgnoreCase(d.getReservationStatus())) { %>
        <form method="post" action="<%= request.getContextPath() %>/reservations/checkin" style="display:inline;">
            <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
            <button class="btn" type="submit">Check-in</button>
        </form>
        <% } %>

        <% if ("CHECKED_IN".equalsIgnoreCase(d.getReservationStatus())) { %>
        <form method="post" action="<%= request.getContextPath() %>/reservations/checkout" style="display:inline;" onsubmit="return confirmCheckOut();">
            <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
            <button class="btn" type="submit">Check-out</button>
        </form>
        <% } %>
    </div>
    <% } %>

</div>

<script>
    function showToast() {
        const toast = document.getElementById("toast");
        toast.style.display = "block";
        setTimeout(hideToast, 7000);
    }

    function hideToast() {
        document.getElementById("toast").style.display = "none";
    }

    function showActionToast(title, msg) {
        const t = document.getElementById("actionToast");
        document.getElementById("actionTitle").innerText = title;
        document.getElementById("actionMsg").innerText = msg;
        t.style.display = "block";
        setTimeout(hideActionToast, 6000);
    }

    function hideActionToast() {
        document.getElementById("actionToast").style.display = "none";
    }

    function confirmCancelReservation() {
        return confirm("Do you want to cancel the reservation?");
    }

    function confirmCheckOut() {
        return confirm("Do you want to check out?");
    }

    (function() {
        const created = <%= created ? "true" : "false" %>;
        const toastParam = "<%= toastParam == null ? "" : toastParam %>";

        if (toastParam === "confirmed") {
            showActionToast("Reservation Confirmed", "Reservation confirmed successfully.");
        }
        if (toastParam === "cancelled") {
            showActionToast("Reservation Cancelled", "Reservation cancelled successfully.");
        }
        if (toastParam === "checkedin") {
            showActionToast("Checked-in", "Guest checked in successfully.");
        }
        if (toastParam === "checkedout") {
            showActionToast("Checked-out", "Guest checked out successfully.");
        }
        if (toastParam === "confirmFirst") {
            showActionToast("Check-in Blocked", "Confirm the reservation first");
        }
        if (toastParam === "invalidDocument") {
            showActionToast("Verification Failed", "Enter correct document number!");
        }
        if (toastParam === "advanceRequired") {
            showActionToast("Advance payment is required", "Please verify ID or pay 20% advance.");
        }

        if (!created) return;

        const code = document.getElementById("vCode") ? document.getElementById("vCode").innerText : "";
        const room = document.getElementById("vRoom") ? document.getElementById("vRoom").innerText : "";
        const inD = document.getElementById("vIn") ? document.getElementById("vIn").innerText : "";
        const outD = document.getElementById("vOut") ? document.getElementById("vOut").innerText : "";

        document.getElementById("tCode").innerText = "Code: " + code;
        document.getElementById("tRoom").innerText = "Room: " + room;
        document.getElementById("tDates").innerText = "Dates: " + inD + " → " + outD;

        showToast();
    })();
</script>

</body>
</html>