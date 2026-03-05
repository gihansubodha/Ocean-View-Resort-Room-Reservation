<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 15:47
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
    <title>User Management</title>
    <style>
        body{font-family:Arial;background:#f4f6fb;margin:0;padding:24px;}
        .card{background:#fff;border-radius:16px;box-shadow:0 10px 25px rgba(17,24,39,.08);padding:16px;}
        .top{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;}
        .btn{background:#111827;color:#fff;border:none;padding:10px 14px;border-radius:12px;text-decoration:none;font-weight:700;}
        table{width:100%;border-collapse:collapse;}
        th,td{padding:10px;border-bottom:1px solid #eef2ff;text-align:left;font-size:14px;}
        .pill{padding:4px 10px;border-radius:999px;background:#eef2ff;display:inline-block;font-size:12px;}
        .danger{background:#dc2626;}
        .dangerBtn{background:#dc2626;color:#fff;border:none;padding:8px 10px;border-radius:10px;cursor:pointer;}
    </style>
</head>
<body>
<div class="card">
    <div class="top">
        <div>
            <div style="font-weight:900;font-size:18px;">User Management</div>
            <div style="color:#6b7280;font-size:13px;">Add / Edit / Remove staff accounts</div>
        </div>
        <button type="button" class="btn btn-soft" onclick="history.back()">
            <i class="fa-solid fa-arrow-left"></i> Back
        </button>
        <a class="btn" href="<%=request.getContextPath()%>/admin/users/add">+ Add User</a>
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
                <a class="btn" style="background:#2563eb" href="<%=request.getContextPath()%>/admin/users/edit?id=<%=u.getUserId()%>">Edit</a>
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