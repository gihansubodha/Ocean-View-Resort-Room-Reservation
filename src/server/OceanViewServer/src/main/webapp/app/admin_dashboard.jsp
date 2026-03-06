<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.AdminDashboardStats" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
    AdminDashboardStats stats = (AdminDashboardStats) request.getAttribute("stats");
    String start = (String) request.getAttribute("start");
    String end = (String) request.getAttribute("end");

    List<Map<String,Object>> busyDays = (List<Map<String,Object>>) request.getAttribute("busyDays");
    List<Map<String,Object>> popularRoomTypes = (List<Map<String,Object>>) request.getAttribute("popularRoomTypes");

    String toastSuccess = (String) session.getAttribute("toastSuccess");
    if (toastSuccess != null) {
        session.removeAttribute("toastSuccess");
    }

    if (stats == null) stats = new AdminDashboardStats();
    if (start == null) start = "";
    if (end == null) end = "";
    if (busyDays == null) busyDays = new java.util.ArrayList<Map<String,Object>>();
    if (popularRoomTypes == null) popularRoomTypes = new java.util.ArrayList<Map<String,Object>>();
%>

<!DOCTYPE html>
<html>
<head>
     <title>Ocean View Resort - Admin Panel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/ui.css">

    <style>
        :root{
            --bg:#f6f7fb;
            --card:#ffffff;
            --text:#0f172a;
            --muted:#64748b;
            --border:#e6eaf2;
            --shadow: 0 10px 26px rgba(17,24,39,.06);
            --r:16px;
        }
        *{box-sizing:border-box}
        body{margin:0;font-family:Arial,Helvetica,sans-serif;background:var(--bg);color:var(--text);}
        a{text-decoration:none;color:inherit}

        .wrap{max-width:1220px;margin:0 auto;padding:18px 18px 28px;}

        .topbar{
            background:var(--card);
            border:1px solid var(--border);
            border-radius:18px;
            box-shadow:var(--shadow);
            padding:14px 16px;
            display:flex;
            align-items:center;
            justify-content:space-between;
            gap:12px;
        }
        .brand{display:flex;align-items:center;gap:12px;}
        .mark{width:38px;height:38px;border-radius:14px;background:linear-gradient(135deg,#2563eb,#8b5cf6);}
        .brand h1{margin:0;font-size:16px;font-weight:900}
        .brand .sub{font-size:12px;color:var(--muted);margin-top:2px}

        .actions{display:flex;gap:10px;align-items:center;flex-wrap:wrap;justify-content:flex-end;}
        .pill{
            padding:9px 12px;border-radius:999px;border:1px solid var(--border);
            background:#fbfcff;font-size:13px;color:var(--muted);
        }
        .btn{
            padding:9px 12px;border-radius:999px;border:1px solid var(--border);
            background:#0f172a;color:#fff;font-size:13px;font-weight:800;
        }
        .btnLight{
            padding:9px 12px;border-radius:999px;border:1px solid var(--border);
            background:#fff;color:#0f172a;font-size:13px;font-weight:800;
        }

        .titleRow{
            margin-top:14px;
            display:flex;
            justify-content:space-between;
            align-items:flex-end;
            gap:12px;
            flex-wrap:wrap;
        }
        .titleRow .title{font-size:24px;font-weight:900;margin:0;}
        .titleRow .hint{color:var(--muted);font-size:13px;margin-top:6px;line-height:1.4}

        .grid{display:grid;gap:12px;margin-top:12px;}
        .grid.kpi{grid-template-columns:repeat(6,minmax(170px,1fr));}
        .grid.range{grid-template-columns:repeat(4,minmax(170px,1fr));}
        .grid.two{grid-template-columns:1fr 1fr;}

        .card{
            background:var(--card);
            border:1px solid var(--border);
            border-radius:var(--r);
            box-shadow:var(--shadow);
            padding:14px;
        }

        .kpiCard .k{font-size:12px;color:var(--muted);}
        .kpiCard .v{font-size:26px;font-weight:900;margin-top:10px;}
        .kpiCard .s{font-size:12px;color:var(--muted);margin-top:6px;}

        .sectionHead{
            margin-top:14px;
            display:flex;
            justify-content:space-between;
            align-items:center;
            gap:10px;
            flex-wrap:wrap;
        }
        .sectionHead h2{margin:0;font-size:16px;font-weight:900;}
        .rangeForm{display:flex;gap:10px;flex-wrap:wrap;align-items:end}
        .field label{display:block;font-size:12px;color:var(--muted);margin-bottom:6px}
        .field input{
            padding:9px 12px;border-radius:12px;border:1px solid var(--border);background:#fff;min-width:170px
        }
        .apply{
            padding:10px 14px;border-radius:12px;border:1px solid var(--border);
            background:#0f172a;color:#fff;font-weight:900;cursor:pointer;
        }

        canvas{width:100% !important;height:320px !important;}

        .manageGrid{display:grid;grid-template-columns:repeat(3,minmax(220px,1fr));gap:12px;margin-top:12px;}
        .manageCard h3{margin:0;font-size:14px;font-weight:900;}
        .manageCard p{margin:6px 0 12px;color:var(--muted);font-size:13px;line-height:1.5}
        .manageBtns{display:flex;gap:10px;flex-wrap:wrap}
        .linkBtn{
            padding:9px 12px;border-radius:12px;border:1px solid var(--border);
            background:#fbfcff;font-size:13px;font-weight:800;
        }
        .linkBtn.primary{background:#2563eb;color:#fff;border-color:#2563eb}
        .linkBtn.warn{background:#f59e0b;color:#111827;border-color:#f59e0b}

        .toast-success{
            border-left: 6px solid #16A34A;
        }

        @media(max-width:1200px){
            .grid.kpi{grid-template-columns:repeat(3,minmax(170px,1fr));}
            .grid.two{grid-template-columns:1fr;}
            .manageGrid{grid-template-columns:1fr;}
        }
        @media(max-width:720px){
            .grid.kpi{grid-template-columns:repeat(2,minmax(170px,1fr));}
            .grid.range{grid-template-columns:repeat(2,minmax(170px,1fr));}
        }
    </style>

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>
<div class="wrap">

    <div class="topbar">
        <div class="brand">
            <div class="mark"></div>
            <div>
                <h1>Ocean View Resort</h1>
                <div class="sub">Admin Panel</div>
            </div>
        </div>

        <div class="actions">
            <button type="button" class="btn btn-soft" onclick="history.back()">
                <i class="fa-solid fa-arrow-left"></i> Back
            </button>
            <div class="pill">Today: <strong><%= java.time.LocalDate.now() %></strong></div>
            <a class="btnLight" href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a>
            <a class="btn" href="<%=request.getContextPath()%>/logout">Logout</a>
        </div>
    </div>

    <div class="titleRow">
        <div>
            <p class="title">Ocean View Resort - Admin Panel</p>
            <div class="hint">Operational analytics and administration controls.</div>
        </div>
    </div>

    <div class="grid kpi">
        <div class="card kpiCard">
            <div class="k">Today • Total Reservations</div>
            <div class="v"><%= stats.getTodayReservations() %></div>
            <div class="s">Check-in date = today</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Today • Checked-in</div>
            <div class="v"><%= stats.getTodayCheckedIn() %></div>
            <div class="s">Status = CHECKED_IN</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Today • Guests In-house</div>
            <div class="v"><%= stats.getTodayGuestsInHotel() %></div>
            <div class="s">Active stays</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Today • Full Payments Paid</div>
            <div class="v"><%= stats.getTodayFullPaymentsTotal() %></div>
            <div class="s">Bills marked PAID today</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Today • Outstanding Balances</div>
            <div class="v"><%= stats.getTodayOutstandingBalanceTotal() %></div>
            <div class="s">Unpaid + partial balances</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Today • Arrivals Not Checked-in</div>
            <div class="v"><%= stats.getTodayArrivalsNotCheckedIn() %></div>
            <div class="s">Arrivals pending check-in</div>
        </div>
    </div>

    <div class="sectionHead">
        <h2>Date range analytics</h2>
        <form class="rangeForm" method="get" action="<%=request.getContextPath()%>/admin/dashboard">
            <div class="field">
                <label>Start date</label>
                <input type="date" name="start" value="<%= start %>"/>
            </div>
            <div class="field">
                <label>End date</label>
                <input type="date" name="end" value="<%= end %>"/>
            </div>
            <button class="apply" type="submit">Apply</button>
        </form>
    </div>

    <div class="grid range">
        <div class="card kpiCard">
            <div class="k">Range • Reservations</div>
            <div class="v"><%= stats.getRangeReservations() %></div>
            <div class="s">By check-in date</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Range • Checked-in</div>
            <div class="v"><%= stats.getRangeCheckedIn() %></div>
            <div class="s">By check-in date</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Range • Cancelled</div>
            <div class="v"><%= stats.getRangeCancelled() %></div>
            <div class="s">By check-in date</div>
        </div>
        <div class="card kpiCard">
            <div class="k">Range • Total Paid</div>
            <div class="v"><%= stats.getRangeTotalPaid() %></div>
            <div class="s">Payments received</div>
        </div>
    </div>

    <div class="sectionHead">
        <h2>Trends (last 30 days)</h2>
        <div class="pill">Fixed window • last 30 days</div>
    </div>

    <div class="grid two">
        <div class="card">
            <h2 style="margin:0;font-size:16px;font-weight:900;">Busy days (checked-in)</h2>
            <div class="hint">Checked-in reservations per day.</div>
            <div style="margin-top:10px;"><canvas id="busyDaysChart"></canvas></div>
        </div>

        <div class="card">
            <h2 style="margin:0;font-size:16px;font-weight:900;">Most chosen room types</h2>
            <div class="hint">Room type selection frequency.</div>
            <div style="margin-top:10px;"><canvas id="roomTypesChart"></canvas></div>
        </div>
    </div>

    <div class="sectionHead">
        <h2>Administration</h2>
        <div class="pill">CRUD management</div>
    </div>

    <div class="manageGrid">
        <div class="card manageCard">
            <h3>User Management</h3>
            <p>Create, edit, activate/deactivate, or remove users. Supports both ADMIN and STAFF roles.</p>
            <div class="manageBtns">
                <a class="linkBtn primary" href="<%=request.getContextPath()%>/admin/users">Manage Users</a>
                <a class="linkBtn" href="<%=request.getContextPath()%>/admin/users/add">Add User</a>
            </div>
        </div>

        <div class="card manageCard">
            <h3>Room Management</h3>
            <p>Add, edit, or remove rooms and manage room status (AVAILABLE/RESERVED/OCCUPIED/MAINTENANCE).</p>
            <div class="manageBtns">
                <a class="linkBtn primary" href="<%=request.getContextPath()%>/admin/rooms">Manage Rooms</a>
                <a class="linkBtn" href="<%=request.getContextPath()%>/admin/rooms/add">Add Room</a>
            </div>
        </div>

        <div class="card manageCard">
            <h3>Room Type Management</h3>
            <p>Add, edit, or remove room types (e.g., Standard, Suite) and update rates.</p>
            <div class="manageBtns">
                <a class="linkBtn primary" href="<%=request.getContextPath()%>/admin/room-types">Manage Types</a>
                <a class="linkBtn" href="<%=request.getContextPath()%>/admin/room-types/add">Add Type</a>
            </div>
        </div>
    </div>

</div>

<% if (toastSuccess != null) { %>
<div id="toastSuccess" class="toast toast-success">
    <button class="close" onclick="document.getElementById('toastSuccess').style.display='none'">&times;</button>
    <h3>Success</h3>
    <div class="line"><%= toastSuccess %></div>
</div>
<% } %>

<script>
    const busyLabels = [
        <% for (int i=0; i<busyDays.size(); i++) { %>
        "<%= String.valueOf(busyDays.get(i).get("day")) %>"<%= (i<busyDays.size()-1 ? "," : "") %>
        <% } %>
    ];
    const busyValues = [
        <% for (int i=0; i<busyDays.size(); i++) { %>
        <%= busyDays.get(i).get("value") %><%= (i<busyDays.size()-1 ? "," : "") %>
        <% } %>
    ];

    new Chart(document.getElementById('busyDaysChart'), {
        type: 'line',
        data: {
            labels: busyLabels,
            datasets: [{
                label: 'Checked-in',
                data: busyValues,
                tension: 0.25,
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: true } },
            scales: { x: { grid: { display: false } }, y: { beginAtZero: true } }
        }
    });

    const rtLabels = [
        <% for (int i=0; i<popularRoomTypes.size(); i++) { %>
        "<%= String.valueOf(popularRoomTypes.get(i).get("label")).replace("\"","\\\"") %>"<%= (i<popularRoomTypes.size()-1 ? "," : "") %>
        <% } %>
    ];
    const rtValues = [
        <% for (int i=0; i<popularRoomTypes.size(); i++) { %>
        <%= popularRoomTypes.get(i).get("value") %><%= (i<popularRoomTypes.size()-1 ? "," : "") %>
        <% } %>
    ];

    new Chart(document.getElementById('roomTypesChart'), {
        type: 'bar',
        data: {
            labels: rtLabels,
            datasets: [{
                label: 'Selections',
                data: rtValues
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: true } },
            scales: { x: { grid: { display: false } }, y: { beginAtZero: true } }
        }
    });

    (function () {
        var toast = document.getElementById("toastSuccess");
        if (toast) {
            toast.style.display = "block";
            setTimeout(function () {
                if (toast) {
                    toast.style.display = "none";
                }
            }, 3000);
        }
    })();
</script>

</body>
</html>