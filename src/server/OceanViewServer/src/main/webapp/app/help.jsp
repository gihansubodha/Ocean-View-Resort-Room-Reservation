<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title>Help & Guidelines</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">

    <style>
        .help-grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
            margin-top: 14px;
        }
        @media (max-width: 980px){
            .help-grid{ grid-template-columns: 1fr; }
        }

        .step{
            display:flex;
            gap:12px;
            align-items:flex-start;
        }
        .badge{
            width: 38px;
            height: 38px;
            border-radius: 14px;
            display:flex;
            align-items:center;
            justify-content:center;
            font-weight: 900;
            background: #DBEAFE;
            color: #1D4ED8;
            border: 1px solid #BFDBFE;
            flex: 0 0 auto;
        }
        .note{
            margin-top:10px;
            padding: 12px 12px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background: #F9FAFB;
            color: var(--muted);
            font-size: 13px;
            line-height: 1.55;
        }

        .flow{ display:grid; grid-template-columns: 1fr; gap: 10px; margin-top: 12px; }
        .flow-item{ border: 1px solid var(--border); border-radius: 14px; padding: 12px; background: #fff; }
        .flow-item b{ font-size: 14px; }
        .flow-item .small{ color: var(--muted); font-size: 13px; line-height: 1.55; margin-top: 6px; }

        ul{ margin: 8px 0 0; padding-left: 18px; }
        li{ margin: 6px 0; color: var(--muted); font-size: 13px; line-height:1.6; }
        code{ background:#F3F4F6; padding:2px 6px; border-radius:8px; border:1px solid #E5E7EB; }
        .iconTitle{ display:flex; align-items:center; gap:10px; }
        .iconTitle i{ color: var(--primary); }
    </style>
</head>
<body>

<div class="app-shell">
    <div class="container">

        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Help & Guidelines</div>
                </div>
            </div>

            <div class="nav-actions">
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp"><i class="fa-solid fa-house"></i> Home</a>
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Help & Guidelines</h2>
            <div class="page-subtitle">
                This page explains the full operational flow used by staff and admins:
                Reservation → Confirmation/Advance → Check-in → Billing/Payments → Check-out, plus Admin CRUD tools.
            </div>

            <!-- FLOW OVERVIEW -->
            <div class="section">
                <div class="iconTitle">
                    <i class="fa-solid fa-route"></i>
                    <h3 style="margin:0; font-size:16px;">Operational Flow (Recommended)</h3>
                </div>

                <div class="flow">
                    <div class="flow-item">
                        <b>1) Create Reservation</b>
                        <span class="pill2 pill-warn">Status: PENDING</span>
                        <div class="small">
                            Create a reservation and generate the bill record. Room may be assigned (optional). Availability list shows only <code>AVAILABLE</code> rooms.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>2) Confirm Reservation</b>
                        <span class="pill2 pill-ok">Status: CONFIRMED</span>
                        <div class="small">
                            Confirm using (A) identification verification OR (B) advance payment (20%). If incorrect doc number is entered, system shows:
                            <b>“Enter correct document number!”</b>
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>3) Check-in Guest</b>
                        <span class="pill2 pill-ok">Reservation: CHECKED_IN</span>
                        <span class="pill2 pill-ok">Room: OCCUPIED</span>
                        <div class="small">
                            Check-in is allowed only when reservation status is <code>CONFIRMED</code>. Otherwise toast: <b>“Confirm the reservation first”</b>.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>4) Billing & Payments</b>
                        <span class="pill2 pill-warn">UNPAID / PARTIAL</span>
                        <div class="small">
                            Accept payments from the Bill page. Partial payments are allowed. Balance is updated after each payment.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>5) Check-out Guest</b>
                        <span class="pill2 pill-ok">Reservation: CHECKED_OUT</span>
                        <span class="pill2 pill-ok">Room: AVAILABLE</span>
                        <div class="small">
                            Check-out is blocked until bill balance becomes 0. If not paid: toast
                            <b>“Payment required before Check-out”</b> and redirects to Bill page.
                        </div>
                    </div>
                </div>

                <div class="note">
                    # Best practice: Always open a booking from <b>Reservation Details</b> and follow the flow above to keep room/bill states correct.
                </div>
            </div>

            <!-- MAIN FUNCTIONS -->
            <div class="help-grid">

                <div class="section">
                    <div class="step">
                        <div class="badge">1</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-plus"></i><h3 style="margin:0; font-size:16px;">Create a Reservation</h3></div>
                            <ul>
                                <li>Go to <b>Home → Add Reservation</b>.</li>
                                <li>Fill guest details and select <b>Room Type</b>.</li>
                                <li>Optionally select an available room in <b>Available Room</b>.</li>
                                <li>Select dates, guest count, and special requests.</li>
                                <li>Click <b>Create Reservation</b> → system generates a bill record.</li>
                            </ul>
                            <div class="note">If no rooms appear: select Room Type first and ensure rooms are <code>AVAILABLE</code>.</div>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="step">
                        <div class="badge">2</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-magnifying-glass"></i><h3 style="margin:0; font-size:16px;">Find Reservation Details</h3></div>
                            <ul>
                                <li>Go to <b>Home → Reservation Details</b>.</li>
                                <li>Search by <b>Reservation ID</b> or <b>Reservation Code</b> (fastest).</li>
                                <li>Or search by NIC/Passport, phone, email, room number, and check-in date range.</li>
                                <li>Use <b>Today’s Arrivals</b> / <b>Today’s Departures</b> for daily operations.</li>
                                <li>Click <b>Open</b> to load full booking + actions.</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="step">
                        <div class="badge">3</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-shield-halved"></i><h3 style="margin:0; font-size:16px;">Confirm Reservation</h3></div>
                            <ul>
                                <li>From <b>Reservation Details</b>, click <b>Confirm</b>.</li>
                                <li><b>Option A:</b> Verify ID document (NIC/Passport/Driving licence).</li>
                                <li><b>Option B:</b> Pay advance payment (20%) from the <b>Bill</b> page.</li>
                            </ul>
                            <div class="note">If document is wrong → toast: <b>“Enter correct document number!”</b></div>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="step">
                        <div class="badge">4</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-right-to-bracket"></i><h3 style="margin:0; font-size:16px;">Check-in & Check-out</h3></div>
                            <ul>
                                <li><b>Check-in</b> allowed only when status is <code>CONFIRMED</code>.</li>
                                <li>On check-in success: reservation → <code>CHECKED_IN</code>, room → <code>OCCUPIED</code>.</li>
                                <li><b>Check-out</b> allowed only when bill balance = <code>0</code>.</li>
                                <li>If not paid: toast shows <b>“Payment required before Check-out”</b> and redirects to Bill page.</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="step">
                        <div class="badge">5</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-receipt"></i><h3 style="margin:0; font-size:16px;">Billing & Payments</h3></div>
                            <ul>
                                <li>Open Bill from <b>Reservation Details → View Bill</b> OR <b>Home → Billing</b>.</li>
                                <li>Bill shows nights, nightly rate, subtotal, tax, discount, total, paid total, balance.</li>
                                <li>Partial payments are allowed; balance updates after each payment.</li>
                                <li><b>Billing Dashboard</b> shows today’s payments + unpaid/partial reservations list.</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="step">
                        <div class="badge">6</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-bed"></i><h3 style="margin:0; font-size:16px;">Room Management</h3></div>
                            <ul>
                                <li>Go to <b>Home → Rooms</b>.</li>
                                <li>Use filters: Available / Reserved / Maintenance.</li>
                                <li>Status is updated through reservation flow (check-in/check-out rules).</li>
                            </ul>
                            <div class="note">Expected: after check-in → <code>OCCUPIED</code>, after check-out → <code>AVAILABLE</code>.</div>
                        </div>
                    </div>
                </div>

                  <div class="section">
                    <div class="step">
                        <div class="badge">8</div>
                        <div>
                            <div class="iconTitle"><i class="fa-solid fa-envelope-circle-check"></i><h3 style="margin:0; font-size:16px;">Notifications</h3></div>
                            <ul>
                                <li>System sends email notifications after reservation placement / Reservation confirmation / Check-in / Check-out.</li>
                                <li>Ensure SMTP settings are correct in your environment.</li>
                            </ul>
                            </div>
                    </div>
                </div>

            </div>

            <div class="section">
                <div class="iconTitle">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    <h3 style="margin:0; font-size:16px;">Troubleshooting & Common Errors</h3>
                </div>
                <ul>
                    <li><b>404 Not Found:</b> confirm Tomcat context path and artifact deployment.</li>
                    <li><b>HTTP 500 JSP errors:</b> stop Tomcat → delete Tomcat <code>work/</code> folder → restart.</li>
                    <li><b>No rooms in dropdown:</b> select Room Type first and ensure rooms are <code>AVAILABLE</code>.</li>
                    <li><b>Cannot check-in:</b> confirm reservation first (toast explains why).</li>
                    <li><b>Cannot check-out:</b> pay remaining balance first (toast explains why).</li>
                </ul>
            </div>

            <div class="nav-actions" style="margin-top:16px;">
                <a class="btn btn-primary" href="<%= request.getContextPath() %>/app/home.jsp"><i class="fa-solid fa-house"></i> Back to Home</a>
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view"><i class="fa-solid fa-magnifying-glass"></i> Reservation Details</a>
                <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view"><i class="fa-solid fa-receipt"></i> Billing</a>
            </div>

        </div>
    </div>
</div>

</body>
</html>