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
  <style>
    body{font-family:Arial;background:#f6f7fb;margin:0;padding:24px;}
    .card{background:#fff;border:1px solid #e6eaf2;border-radius:16px;padding:16px;box-shadow:0 10px 26px rgba(17,24,39,.06);}
    .top{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;gap:12px;flex-wrap:wrap;}
    .btn{background:#0f172a;color:#fff;border:none;padding:10px 14px;border-radius:12px;text-decoration:none;font-weight:900;}
    table{width:100%;border-collapse:collapse;}
    th,td{padding:10px;border-bottom:1px solid #eef2ff;text-align:left;font-size:14px;}
    .pill{display:inline-block;padding:4px 10px;border-radius:999px;background:#eef2ff;font-size:12px;}
    .edit{background:#2563eb;color:#fff;padding:8px 10px;border-radius:10px;text-decoration:none;font-weight:900;}
    .dangerBtn{background:#dc2626;color:#fff;border:none;padding:8px 10px;border-radius:10px;cursor:pointer;font-weight:900;}
    .muted{color:#64748b;font-size:13px;}
  </style>
</head>
<body>
<div class="card">
  <div class="top">
    <div>
      <div style="font-weight:900;font-size:18px;">User Management</div>
      <div class="muted">Create / Edit / Delete users (ADMIN & STAFF)</div>
    </div>
    <div style="display:flex;gap:10px;flex-wrap:wrap;">
      <a class="btn" href="<%=request.getContextPath()%>/admin/dashboard">← Dashboard</a>
      <a class="btn" href="<%=request.getContextPath()%>/admin/users/add">+ Add User</a>
    </div>
  </div>

  <table>
    <thead>
    <tr>
      <th>ID</th><th>Username</th><th>Role</th><th>Active</th><th>Created</th><th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% if (users != null) for (User u : users) { %>
    <tr>
      <td><%=u.getUserId()%></td>
      <td><%=u.getUsername()%></td>
      <td><span class="pill"><%=u.getRole()%></span></td>
      <td><%=u.isActive() ? "Yes" : "No"%></td>
      <td><%=u.getCreatedAt()%></td>
      <td>
        <a class="edit" href="<%=request.getContextPath()%>/admin/users/edit?id=<%=u.getUserId()%>">Edit</a>
        <form method="post" action="<%=request.getContextPath()%>/admin/users/delete" style="display:inline;">
          <input type="hidden" name="id" value="<%=u.getUserId()%>"/>
          <button class="dangerBtn" type="submit" onclick="return confirm('Delete this user?')">Delete</button>
        </form>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>
</body>
</html>
