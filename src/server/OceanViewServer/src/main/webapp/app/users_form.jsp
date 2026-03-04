<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 15:48
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
    <style>
        body{font-family:Arial;background:#f4f6fb;margin:0;padding:24px;}
        .card{max-width:720px;margin:auto;background:#fff;border-radius:16px;box-shadow:0 10px 25px rgba(17,24,39,.08);padding:18px;}
        .row{display:flex;gap:12px;flex-wrap:wrap;}
        label{display:block;color:#6b7280;font-size:12px;margin:10px 0 6px;}
        input,select{padding:10px 12px;border-radius:12px;border:1px solid #e5e7eb;min-width:240px;}
        .btn{background:#111827;color:#fff;border:none;padding:10px 14px;border-radius:12px;font-weight:700;cursor:pointer;}
        .back{background:#2563eb;text-decoration:none;display:inline-block;}
        .err{background:#fee2e2;color:#991b1b;padding:10px 12px;border-radius:12px;margin:10px 0;}
    </style>
</head>
<body>
<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <div style="font-weight:900;font-size:18px;"><%= edit ? "Edit User" : "Add User" %></div>
        <a class="btn back" href="<%=request.getContextPath()%>/admin/users">Back</a>
    </div>

    <% if (error != null) { %><div class="err"><%=error%></div><% } %>

    <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/users/edit" : "/admin/users/add" %>">
        <input type="hidden" name="userId" value="<%= edit ? u.getUserId() : 0 %>"/>

        <label>Username</label>
        <input name="username" value="<%= edit ? u.getUsername() : "" %>" required/>

        <div class="row">
            <div>
                <label>Role</label>
                <select name="role" required>
                    <option value="RECEPTIONIST" <%= edit && "RECEPTIONIST".equals(u.getRole()) ? "selected":"" %>>RECEPTIONIST</option>
                    <option value="ADMIN" <%= edit && "ADMIN".equals(u.getRole()) ? "selected":"" %>>ADMIN</option>
                </select>
            </div>
            <div>
                <label>Active</label>
                <select name="isActive">
                    <option value="1" <%= !edit || u.isActive() ? "selected":"" %>>Yes</option>
                    <option value="0" <%= edit && !u.isActive() ? "selected":"" %>>No</option>
                </select>
            </div>
        </div>

        <label>Password <span style="color:#6b7280">(leave blank to keep current on edit)</span></label>
        <input type="password" name="password" />

        <div style="margin-top:14px;">
            <button class="btn" type="submit"><%= edit ? "Save Changes" : "Create User" %></button>
        </div>
    </form>
</div>
</body>
</html>