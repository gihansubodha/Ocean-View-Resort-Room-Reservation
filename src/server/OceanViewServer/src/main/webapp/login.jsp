<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Ocean View Resort - Login</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- THEME (shared) -->
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
  <!-- ICONS -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

  <style>
    /* Login-only styling (NO logic changes) */
    .loginWrap{ max-width: 980px; margin: 0 auto; }
    .loginGrid{
      display:grid;
      grid-template-columns: 1.1fr 0.9fr;
      gap: 16px;
      margin-top: 14px;
      align-items: stretch;
    }
    @media (max-width: 980px){
      .loginGrid{ grid-template-columns: 1fr; }
    }

    .hero{
      border: 1px solid var(--border);
      border-radius: 16px;
      background: linear-gradient(135deg, rgba(37,99,235,.10), rgba(124,58,237,.10));
      padding: 18px;
      box-shadow: 0 10px 26px rgba(17,24,39,.06);
      overflow:hidden;
      position:relative;
    }
    .hero:before{
      content:"";
      position:absolute;
      right:-120px;
      top:-120px;
      width: 260px;
      height: 260px;
      border-radius: 999px;
      background: radial-gradient(circle, rgba(37,99,235,.22), rgba(124,58,237,.10), transparent 70%);
      filter: blur(1px);
    }
    .hero h2{ margin:0; font-size: 20px; }
    .hero p{ margin:8px 0 0; color: var(--muted); font-size: 13px; line-height:1.6; }
    .heroList{ margin-top: 12px; display:grid; gap:10px; }
    .heroItem{
      display:flex;
      gap:10px;
      align-items:flex-start;
      padding: 12px;
      border-radius: 14px;
      border: 1px solid rgba(229,231,235,.8);
      background: rgba(255,255,255,.7);
    }
    .heroIcon{
      width: 38px;
      height: 38px;
      border-radius: 14px;
      display:flex;
      align-items:center;
      justify-content:center;
      background: #DBEAFE;
      border: 1px solid #BFDBFE;
      color: #1D4ED8;
      flex: 0 0 auto;
    }
    .heroItem b{ display:block; font-size: 13px; }
    .heroItem span{ display:block; margin-top:4px; color: var(--muted); font-size: 13px; line-height:1.5; }

    .loginCard{
      max-width: 520px;
      margin: 0 auto;
    }

    .inputIcon{
      position:relative;
    }
    .inputIcon i{
      position:absolute;
      left: 12px;
      top: 50%;
      transform: translateY(-50%);
      color: var(--muted);
      font-size: 14px;
      pointer-events:none;
    }
    .inputIcon input{
      padding-left: 38px; /* room for icon */
    }

    .loginFooter{
      margin-top: 12px;
      color: var(--muted);
      font-size: 12px;
      text-align:center;
      line-height:1.5;
    }

    .btnWide{ width:100%; justify-content:center; }
  </style>
</head>

<body>
<div class="app-shell">
  <div class="container loginWrap">

    <!-- Top bar -->
    <div class="topbar">
      <div class="brand">
        <div class="logo"></div>
        <div>
          <h1>Ocean View Resort</h1>
          <div class="sub">User Login</div>
        </div>
      </div>
      <div class="pill"><i class="fa-solid fa-lock"></i> Secure User Access</div>
    </div>

    <div class="loginGrid">

      <!-- Left: eye-catching hero -->
      <div class="hero">
        <h2><i class="fa-solid fa-hotel"></i> Welcome back</h2>
        <p>
          Sign in to manage reservations, confirm guests, handle billing and payments, and keep room status accurate
          across the Ocean View workflow.
        </p>

        <div class="heroList">
          <div class="heroItem">
            <div class="heroIcon"><i class="fa-solid fa-calendar-check"></i></div>
            <div>
              <b>Reservations & Confirmation</b>
              <span>Create bookings, confirm via ID/advance, and follow the correct operational flow.</span>
            </div>
          </div>

          <div class="heroItem">
            <div class="heroIcon"><i class="fa-solid fa-receipt"></i></div>
            <div>
              <b>Billing & Payments</b>
              <span>Track bills, accept partial payments, and ensure balance is cleared before checkout.</span>
            </div>
          </div>

          <div class="heroItem">
            <div class="heroIcon"><i class="fa-solid fa-bed"></i></div>
            <div>
              <b>Room Status Accuracy</b>
              <span>Automatically align room state (AVAILABLE/OCCUPIED/MAINTENANCE) with guest actions.</span>
            </div>
          </div>
        </div>

        <div class="loginFooter">
          Ocean View Resort System • Java EE • Tomcat • MySQL
        </div>
      </div>

      <!-- Right: login form -->
      <div class="card card-pad loginCard">
        <h2 class="page-title"><i class="fa-solid fa-right-to-bracket"></i> Login</h2>
        <div class="page-subtitle">Enter your credentials to access the system.</div>

        <%
          String err = (String) request.getAttribute("error");
          if (err != null) {
        %>
        <div class="alert alert-err" style="margin-top:12px;"><%= err %></div>
        <%
          }
        %>

        <form method="post" action="<%= request.getContextPath() %>/login">
          <div class="section" style="margin-top:14px;">
            <div style="display:grid; gap:12px;">

              <div>
                <label><i class="fa-solid fa-user"></i> Username</label>
                <div class="inputIcon">
                  <i class="fa-solid fa-user"></i>
                  <input type="text" name="username" required autocomplete="username" />
                </div>
              </div>

              <div>
                <label><i class="fa-solid fa-key"></i> Password</label>
                <div class="inputIcon">
                  <i class="fa-solid fa-key"></i>
                  <input type="password" name="password" required autocomplete="current-password" />
                </div>
              </div>

              <button class="btn btn-primary btnWide" type="submit">
                <i class="fa-solid fa-lock"></i> Sign In
              </button>

              <div class="note" style="margin-top:0;">
                Tip: If your account is inactive, an admin must re-activate it in <b>Admin → Users</b>.
              </div>

            </div>
          </div>
        </form>

        <div class="loginFooter">
          <i class="fa-solid fa-shield-halved"></i>
          Authorized Users only • Do not share credentials
        </div>
      </div>

    </div>

  </div>
</div>
</body>
</html>