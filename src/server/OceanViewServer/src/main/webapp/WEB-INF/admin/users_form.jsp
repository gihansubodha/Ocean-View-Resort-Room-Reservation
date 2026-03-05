<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.User" %>
<%
    User u = (User) request.getAttribute("u");
    String error = (String) request.getAttribute("error");
    boolean edit = (u != null && u.getUserId() > 0);
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= edit ? "Edit User" : "Add User" %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        .wide{ max-width: 980px; margin: 0 auto; }
        .row2{
            display:grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px 18px;
        }
        @media (max-width: 860px){ .row2{ grid-template-columns: 1fr; } }
        .muted2{ color: var(--muted); font-size: 13px; line-height:1.5; margin-top:4px; }
        .btnbar{ display:flex; flex-wrap:wrap; gap:10px; align-items:center; }
    </style>
</head>
<body>

<div class="app-shell">
    <div class="container wide">

        <div class="topbar">
            <div class="brand">
                <div class="logo"></div>
                <div>
                    <h1>Ocean View Resort</h1>
                    <div class="sub">Admin • Users</div>
                </div>
            </div>

            <div class="nav-actions">
                <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
                    <i class="fa-solid fa-chart-line"></i> Dashboard
                </a>
                <a class="btn btn-slate" href="<%=request.getContextPath()%>/admin/users">
                    <i class="fa-solid fa-list"></i> Back to List
                </a>
                <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
                    <i class="fa-solid fa-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title"><%= edit ? "Edit User" : "Add User" %></h2>
            <div class="page-subtitle">Create and maintain staff accounts (ADMIN / STAFF).</div>

            <% if (error != null) { %>
            <div class="alert alert-err"><%=error%></div>
            <% } %>

            <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/users/edit" : "/admin/users/add" %>">
                <input type="hidden" name="userId" value="<%= edit ? u.getUserId() : 0 %>"/>

                <div class="section">
                    <div>
                        <label>Username</label>
                        <input name="username" value="<%= edit ? u.getUsername() : "" %>" required/>
                    </div>

                    <div class="row2" style="margin-top:14px;">
                        <div>
                            <label>Role</label>
                            <select name="role" required>
                                <option value="STAFF" <%= edit && "STAFF".equalsIgnoreCase(u.getRole()) ? "selected":"" %>>STAFF</option>
                                <option value="ADMIN" <%= edit && "ADMIN".equalsIgnoreCase(u.getRole()) ? "selected":"" %>>ADMIN</option>
                            </select>
                            <div class="muted2">STAFF = receptionist/front desk. ADMIN = system administrator.</div>
                        </div>

                        <div>
                            <label>Active</label>
                            <select name="isActive">
                                <option value="1" <%= !edit || (u != null && u.isActive()) ? "selected":"" %>>Yes</option>
                                <option value="0" <%= edit && u != null && !u.isActive() ? "selected":"" %>>No</option>
                            </select>
                            <div class="muted2">Deactivate to block login without deleting the account.</div>
                        </div>
                    </div>

                    <div style="margin-top:14px;">
                        <label>Password <span class="muted2">(leave blank to keep current password on edit)</span></label>
                        <input type="password" name="password" />
                    </div>

                    <div class="btnbar" style="margin-top:14px;">
                        <button class="btn btn-primary" type="submit">
                            <i class="fa-solid fa-floppy-disk"></i> <%= edit ? "Save Changes" : "Create User" %>
                        </button>
                        <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/users">
                            <i class="fa-solid fa-arrow-left"></i> Cancel
                        </a>
                    </div>
                </div>

            </form>
        </div>

    </div>
</div>

</body>
</html>