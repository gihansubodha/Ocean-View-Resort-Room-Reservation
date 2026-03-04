<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.ReservationDetails" %>

<!DOCTYPE html>
<html>
<head>
    <title>Reservation Details</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- NEW UI -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <style>
        /* Only page-specific layout tweaks (logic untouched) */
        .grid { display:flex; flex-wrap:wrap; gap:12px; }
        .grid .field { display:flex; flex-direction:column; }
        .grid input { width: 220px; }

        .row { margin: 10px 0; }
        .label { display:inline-block; width: 180px; font-weight: 800; color: var(--muted); }

        /* Use shared toast style from ui.css */
        /* but keep your IDs and extra toast box */
        #actionToast { display:none; }

        .section-title { margin-top: 18px; margin-bottom: 8px; font-size:16px; }

        /* Buttons: map your .btn usage to new UI buttons */
        .btn {
            display:inline-flex;
            align-items:center;
            justify-content:center;
            padding: 10px 14px;
            border-radius: 12px;
            border: 1px solid transparent;
            font-size: 14px;
            text-decoration:none;
            cursor:pointer;
            user-select:none;
            white-space:nowrap;

            background: var(--soft);
            color:#0F172A;
            border: 1px solid #BAE6FD;
        }
        .btn:hover{ background: var(--soft2); }

        /* tables use ui.css .table */
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }

        /* preserve your "small open button" look */
        .btn-mini{ padding:6px 10px; border-radius:10px; font-size: 13px; }

        /* inline form spacing */
        .inline-form{ display:inline; }
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

<!-- NEW SHELL -->
<div class="app-shell">
    <div class="container">

        <!-- Top bar (new UI) -->
        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Reservation Details</div>
                </div>
            </div>

            <div class="nav-actions">
                <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Home</a>
                <a class="btn" style="background: var(--danger); border-color: var(--danger); color:#fff;"
                   href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <!-- Toast 1: created (IDs kept) -->
        <div class="toast" id="toast">
            <button class="close" onclick="hideToast()">×</button>
            <h3>Reservation created successfully</h3>
            <div class="line" id="tCode"></div>
            <div class="line" id="tRoom"></div>
            <div class="line" id="tDates"></div>
        </div>

        <!-- Toast 2: action toast (IDs kept) -->
        <div class="toast" id="actionToast">
            <button class="close" onclick="hideActionToast()">×</button>
            <h3 id="actionTitle">Done</h3>
            <div class="line" id="actionMsg"></div>
        </div>

        <!-- Main card -->
        <div class="card card-pad">
            <h2 class="page-title">Reservation Details</h2>
            <div class="page-subtitle">
                Search by Reservation ID, Code, NIC/Passport, Phone, Email, Room number, check-in date range — or use Today’s lists.
            </div>

            <!-- Search section -->
            <div class="section" style="margin-top:14px;">
                <form method="get" action="<%= request.getContextPath() %>/reservations/view">
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

                    <div class="nav-actions" style="margin-top:12px; justify-content:flex-start;">
                        <button class="btn" type="submit"
                                style="background: var(--primary); border-color: var(--primary); color:#fff;">Search</button>

                        <a class="btn" href="<%= request.getContextPath() %>/reservations/view?quick=arrivals">Today’s Arrivals</a>
                        <a class="btn" href="<%= request.getContextPath() %>/reservations/view?quick=departures">Today’s Departures</a>
                        <a class="btn" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
                    </div>
                </form>
            </div>

            <% if (error != null && !error.trim().isEmpty()) { %>
            <div class="alert alert-err" style="margin-top:12px;"><%= error %></div>
            <% } %>

            <% if (results != null && !results.isEmpty()) { %>
            <h3 class="section-title"><%= (resultsTitle != null ? resultsTitle : "Search Results") %></h3>

            <table class="table">
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
                        <a class="btn btn-mini" href="<%= request.getContextPath() %>/reservations/view?id=<%= r.getReservationId() %>">Open</a>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>

            <% if (d != null) { %>
            <hr class="sep">

            <h3 class="section-title">Booking</h3>

            <div class="section">
                <div class="row"><span class="label">Code:</span> <span id="vCode"><%= d.getReservationCode() %></span></div>
                <div class="row"><span class="label">Check-in:</span> <span id="vIn"><%= d.getCheckIn() %></span></div>
                <div class="row"><span class="label">Check-out:</span> <span id="vOut"><%= d.getCheckOut() %></span></div>
                <div class="row"><span class="label">Guests:</span> <%= d.getNumGuests() %></div>
                <div class="row"><span class="label">Status:</span> <%= d.getReservationStatus() %></div>
                <div class="row"><span class="label">Special requests:</span> <%= d.getSpecialRequests() %></div>
            </div>

            <h3 class="section-title">Guest</h3>

            <div class="section">
                <div class="row"><span class="label">Name:</span> <%= d.getGuestFirstName() %> <%= d.getGuestLastName() %></div>
                <div class="row"><span class="label">Phone:</span> <%= d.getGuestPhone() %></div>
                <div class="row"><span class="label">Email:</span> <%= d.getGuestEmail() %></div>
                <div class="row"><span class="label">NIC/Passport:</span> <%= d.getGuestNicPassport() %></div>
            </div>

            <h3 class="section-title">Room</h3>

            <div class="section">
                <div class="row"><span class="label">Room Type:</span> <%= d.getRoomTypeName() %> (ID: <%= d.getRoomTypeId() %>)</div>
                <div class="row"><span class="label">Nightly Rate:</span> LKR <%= d.getNightlyRate() %></div>
                <div class="row"><span class="label">Room Assigned:</span>
                    <span id="vRoom"><%= (d.getRoomId() == null ? "Not assigned" : (d.getRoomNumber() + " (Room ID: " + d.getRoomId() + ")")) %></span>
                </div>

                <div class="nav-actions" style="margin-top:14px; justify-content:flex-start;">
                    <a class="btn" style="background: var(--warning); border-color: var(--warning); color:#111827;"
                       href="<%= request.getContextPath() %>/bills/view?reservationId=<%= d.getReservationId() %>">View Bill</a>

                    <% if ("PENDING".equalsIgnoreCase(d.getReservationStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/reservations/confirm" class="inline-form">
                        <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
                        <button class="btn" type="submit"
                                style="background: var(--primary); border-color: var(--primary); color:#fff;">Confirm</button>
                    </form>
                    <% } %>

                    <% if (!"CHECKED_OUT".equalsIgnoreCase(d.getReservationStatus()) && !"CANCELLED".equalsIgnoreCase(d.getReservationStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/reservations/cancel"
                          class="inline-form" onsubmit="return confirmCancelReservation();">
                        <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
                        <button class="btn" type="submit"
                                style="background: var(--danger); border-color: var(--danger); color:#fff;">Cancel</button>
                    </form>
                    <% } %>

                    <% if ("CONFIRMED".equalsIgnoreCase(d.getReservationStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/reservations/checkin" class="inline-form">
                        <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
                        <button class="btn" type="submit"
                                style="background: var(--success); border-color: var(--success); color:#fff;">Check-in</button>
                    </form>
                    <% } %>

                    <% if ("CHECKED_IN".equalsIgnoreCase(d.getReservationStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/reservations/checkout"
                          class="inline-form" onsubmit="return confirmCheckOut();">
                        <input type="hidden" name="id" value="<%= d.getReservationId() %>"/>
                        <button class="btn" type="submit"
                                style="background: var(--slate); border-color: var(--slate); color:#fff;">Check-out</button>
                    </form>
                    <% } %>
                </div>
            </div>
            <% } %>

        </div>
    </div>
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