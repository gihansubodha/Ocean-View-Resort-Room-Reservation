<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.User" %>
<%
  List<User> users = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Admin - Users</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
   <style>
    .actionsCell{ display:flex; gap:10px; flex-wrap:wrap; }
    .dangerBtn{
      border:none; cursor:pointer;
      padding:10px 14px; border-radius:12px; font-weight:900;
      background: var(--danger); color:#fff;
    }
    .dangerBtn:hover{ background: var(--danger2); }

    .pillRole{
      display:inline-flex; align-items:center; gap:6px;
      padding:4px 10px; border-radius:999px; font-size:12px; font-weight:900;
      border:1px solid var(--border); background:#fff; color: var(--muted);
    }
    .roleADMIN{ background:#EEF2FF; border-color:#C7D2FE; color:#1E3A8A; }
    .roleSTAFF{ background:#ECFDF5; border-color:#A7F3D0; color:#065F46; }
    .pillActiveYes{ background:#ECFDF5; border-color:#A7F3D0; color:#065F46; }
    .pillActiveNo{ background:#FEF2F2; border-color:#FECACA; color:#991B1B; }
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
          <div class="sub">Admin • Users</div>
        </div>
      </div>

      <div class="nav-actions">
        <button type="button" class="btn btn-soft" onclick="history.back()">
          <i class="fa-solid fa-arrow-left"></i> Back
        </button>
        <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
          <i class="fa-solid fa-chart-line"></i> Dashboard
        </a>
        <a class="btn btn-primary" href="<%=request.getContextPath()%>/admin/users/add">
          <i class="fa-solid fa-user-plus"></i> Add User
        </a>
        <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
          <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>
      </div>
    </div>

    <div class="card card-pad">
      <h2 class="page-title">User Management</h2>
      <div class="page-subtitle">Create / Edit / Delete users (ADMIN & STAFF).</div>

      <div class="section">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Active</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <% if (users != null) for (User u : users) { %>
          <tr>
            <td><%=u.getUserId()%></td>
            <td><%=u.getUsername()%></td>
            <td>
              <span class="pillRole <%= "ADMIN".equalsIgnoreCase(u.getRole()) ? "roleADMIN" : "roleSTAFF" %>">
                <i class="fa-solid <%= "ADMIN".equalsIgnoreCase(u.getRole()) ? "fa-user-shield" : "fa-user" %>"></i>
                <%=u.getRole()%>
              </span>
            </td>
            <td>
              <span class="pill2 <%= u.isActive() ? "pill-ok" : "pill-bad" %>">
                <i class="fa-solid <%= u.isActive() ? "fa-circle-check" : "fa-circle-xmark" %>"></i>
                <%=u.isActive() ? "Yes" : "No"%>
              </span>
            </td>
            <td><%=u.getCreatedAt()%></td>
            <td class="actionsCell">
              <a class="btn btn-soft btn-mini" href="<%=request.getContextPath()%>/admin/users/edit?id=<%=u.getUserId()%>">
                <i class="fa-solid fa-pen"></i> Edit
              </a>
              <form method="post" action="<%=request.getContextPath()%>/admin/users/delete" style="display:inline;">
                <input type="hidden" name="id" value="<%=u.getUserId()%>"/>
                <button class="dangerBtn btn-mini" type="submit" onclick="return confirm('Delete this user?')">
                  <i class="fa-solid fa-trash"></i> Delete
                </button>
              </form>
            </td>
          </tr>
          <% } %>

          <% if (users == null || users.isEmpty()) { %>
          <tr><td colspan="6">No users found.</td></tr>
          <% } %>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</div>

</body>
</html>