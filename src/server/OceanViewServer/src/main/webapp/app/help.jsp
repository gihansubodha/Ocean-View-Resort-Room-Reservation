<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
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
            gap:10px;
            align-items:flex-start;
        }
        .badge{
            width: 32px;
            height: 32px;
            border-radius: 12px;
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
        .flow{
            display:grid;
            grid-template-columns: 1fr;
            gap: 10px;
            margin-top: 12px;
        }
        .flow-item{
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 12px;
            background: #fff;
        }
        .flow-item b{ font-size: 14px; }
        .flow-item .small{ color: var(--muted); font-size: 13px; line-height: 1.55; margin-top: 6px; }
        .pill2{
            display:inline-block;
            padding: 4px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 800;
            border: 1px solid var(--border);
            background: #fff;
            margin-left: 8px;
            color: var(--muted);
        }
        .pill-ok{ background:#ECFDF5; border-color:#A7F3D0; color:#065F46; }
        .pill-warn{ background:#FFFBEB; border-color:#FDE68A; color:#92400E; }
        .pill-bad{ background:#FEF2F2; border-color:#FECACA; color:#991B1B; }
        ul{ margin: 8px 0 0; padding-left: 18px; }
        li{ margin: 6px 0; color: var(--muted); font-size: 13px; line-height:1.6; }
        code{ background:#F3F4F6; padding:2px 6px; border-radius:8px; border:1px solid #E5E7EB; }
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
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/app/home.jsp">Home</a>
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/logout">Logout</a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Help & Guidelines</h2>
            <div class="page-subtitle">
                This page explains all core features and the correct operational flow for staff (Reservation → Confirmation → Check-in → Billing → Check-out).
            </div>

            <!-- FLOW OVERVIEW -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Operational Flow (Recommended)</h3>

                <div class="flow">
                    <div class="flow-item">
                        <b>1) Create Reservation</b>
                        <span class="pill2 pill-warn">Status: PENDING</span>
                        <div class="small">
                            Create reservation + generate bill record. Room may be assigned (optional).
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>2) Confirm Reservation</b>
                        <span class="pill2 pill-ok">Status: CONFIRMED</span>
                        <div class="small">
                            Confirm using ID document verification OR advance payment (20%). Only confirmed reservations can be checked-in.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>3) Check-in Guest</b>
                        <span class="pill2 pill-ok">Reservation: CHECKED_IN</span>
                        <span class="pill2 pill-ok">Room: OCCUPIED</span>
                        <div class="small">
                            Check-in changes reservation status and room status. If you see “Confirm the reservation first” toast, confirm first.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>4) Billing & Payments</b>
                        <span class="pill2 pill-warn">UNPAID / PARTIAL</span>
                        <div class="small">
                            Accept payments from the Bill page. Partial payments are allowed. Check-out is blocked until balance becomes 0.
                        </div>
                    </div>

                    <div class="flow-item">
                        <b>5) Check-out Guest</b>
                        <span class="pill2 pill-ok">Reservation: CHECKED_OUT</span>
                        <span class="pill2 pill-ok">Room: AVAILABLE</span>
                        <div class="small">
                            Only allowed if the bill balance is fully paid. If payment is not complete, you will see “Payment required before Check-out”.
                        </div>
                    </div>
                </div>

                <div class="note">
                    ✅ Best practice: Always open the reservation from <b>Reservation Details</b> and follow the flow above. This prevents incorrect room states and billing mistakes.
                </div>
            </div>

            <!-- MAIN FUNCTIONS -->
            <div class="help-grid">

                <!-- Create Reservation -->
                <div class="section">
                    <div class="step">
                        <div class="badge">1</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Create a Reservation</h3>
                            <ul>
                                <li>Go to <b>Home → Add Reservation</b>.</li>
                                <li>Fill guest details and choose <b>Room Type</b>.</li>
                                <li>Optionally pick an available room in <b>Available Room</b>.</li>
                                <li>Select check-in/check-out dates, guest count, and special requests.</li>
                                <li>Click <b>Create Reservation</b>.</li>
                            </ul>
                            <div class="note">
                                If the room dropdown looks empty: pick a Room Type first and ensure the room status is <code>AVAILABLE</code>.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Search Reservation -->
                <div class="section">
                    <div class="step">
                        <div class="badge">2</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Find Reservation Details</h3>
                            <ul>
                                <li>Go to <b>Home → Reservation Details</b>.</li>
                                <li>Search by <b>Reservation ID</b> or <b>Reservation Code</b> for exact match.</li>
                                <li>Or search by NIC/Passport, phone, email, room number, and check-in date range.</li>
                                <li>Use <b>Today’s Arrivals</b> / <b>Today’s Departures</b> for daily operations.</li>
                            </ul>
                            <div class="note">
                                Use <b>Open</b> on a result row to load full reservation details and actions.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Confirm Reservation -->
                <div class="section">
                    <div class="step">
                        <div class="badge">3</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Confirm Reservation</h3>
                            <ul>
                                <li>Open reservation from <b>Reservation Details</b>.</li>
                                <li>Use either:</li>
                                <li><b>Option A:</b> Confirm using document verification (NIC/Passport/Driving licence).</li>
                                <li><b>Option B:</b> Pay advance payment (20%) from the Bill page.</li>
                            </ul>
                            <div class="note">
                                If the document is incorrect, system shows toast: <b>“Enter correct document number!”</b>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Check-in / Check-out -->
                <div class="section">
                    <div class="step">
                        <div class="badge">4</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Check-in & Check-out</h3>
                            <ul>
                                <li><b>Check-in</b> is allowed only when reservation is <code>CONFIRMED</code>.</li>
                                <li>On successful check-in: reservation becomes <code>CHECKED_IN</code>, room becomes <code>OCCUPIED</code>.</li>
                                <li><b>Check-out</b> is allowed only if bill balance = 0.</li>
                                <li>If balance is not paid: toast shows <b>“Payment required before Check-out”</b> and redirects to payment.</li>
                            </ul>
                            <div class="note">
                                If you see <b>“Confirm the reservation first”</b> toast — confirm before check-in.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Billing -->
                <div class="section">
                    <div class="step">
                        <div class="badge">5</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Billing & Payments</h3>
                            <ul>
                                <li>Open Bill from either:</li>
                                <li><b>Reservation Details → View Bill</b> OR <b>Home → Billing</b>.</li>
                                <li>Bill shows: nights, nightly rate, subtotal, tax, total, paid total, balance.</li>
                                <li>You can accept <b>partial payments</b>. System updates balance after each payment.</li>
                            </ul>
                            <div class="note">
                                Billing Dashboard shows: <b>Today’s payments</b> + list of reservations with unpaid/partial balances.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Rooms -->
                <div class="section">
                    <div class="step">
                        <div class="badge">6</div>
                        <div>
                            <h3 style="margin:0; font-size:16px;">Room Management</h3>
                            <ul>
                                <li>Go to <b>Home → Rooms</b>.</li>
                                <li>Use filters: <b>Available</b>, <b>Reserved</b>, <b>Maintenance</b>.</li>
                                <li>Room status changes automatically based on reservation flow.</li>
                            </ul>
                            <div class="note">
                                Expected: after check-in → <code>OCCUPIED</code>, after check-out → <code>AVAILABLE</code>.
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Troubleshooting / Errors -->
            <div class="section">
                <h3 style="margin:0; font-size:16px;">Troubleshooting & Common Errors</h3>
                <ul>
                    <li><b>Page shows 404:</b> ensure Tomcat deployed the latest build and the correct context path.</li>
                    <li><b>HTTP 500 JSP errors:</b> stop Tomcat → delete Tomcat <code>work/</code> folder → restart.</li>
                    <li><b>No rooms in dropdown:</b> select Room Type first and ensure rooms are <code>AVAILABLE</code>.</li>
                    <li><b>Cannot check-in:</b> confirm reservation first (toast explains why).</li>
                    <li><b>Cannot check-out:</b> pay remaining balance first (toast explains why).</li>
                </ul>
            </div>

            <div class="nav-actions" style="margin-top:16px;">
                <a class="btn btn-primary" href="<%= request.getContextPath() %>/app/home.jsp">Back to Home</a>
                <a class="btn btn-soft" href="<%= request.getContextPath() %>/reservations/view">Go to Reservation Details</a>
                <a class="btn btn-warning" href="<%= request.getContextPath() %>/bills/view">Go to Billing</a>
            </div>

        </div>

    </div>
</div>
</body>
</html>