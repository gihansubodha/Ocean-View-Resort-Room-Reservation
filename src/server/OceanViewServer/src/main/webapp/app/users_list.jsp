<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.User" %>
<%
    List<User> users = (List<User>) request.getAttribute("users");

    String toastSuccess = (String) session.getAttribute("toastSuccess");
    if (toastSuccess != null) session.removeAttribute("toastSuccess");

    String toastError = (String) session.getAttribute("toastError");
    if (toastError != null) session.removeAttribute("toastError");
%>
<!DOCTYPE html>
<html>
<head>
    <title>User Management</title>
    <style>
        body{font-family:Arial;background:#f4f6fb;margin:0;padding:24px;}
        .card{background:#fff;border-radius:16px;box-shadow:0 10px 25px rgba(17,24,39,.08);padding:16px;}
        .top{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;gap:10px;flex-wrap:wrap;}
        .btn{background:#111827;color:#fff;border:none;padding:10px 14px;border-radius:12px;text-decoration:none;font-weight:700;cursor:pointer;}
        table{width:100%;border-collapse:collapse;}
        th,td{padding:10px;border-bottom:1px solid #eef2ff;text-align:left;font-size:14px;}
        .pill{padding:4px 10px;border-radius:999px;background:#eef2ff;display:inline-block;font-size:12px;}
        .dangerBtn{background:#dc2626;color:#fff;border:none;padding:8px 10px;border-radius:10px;cursor:pointer;}
        .toast{
            position:fixed; right:20px; top:20px; z-index:9999;
            min-width:280px; max-width:420px;
            background:#fff; border-radius:14px; padding:14px 16px;
            box-shadow:0 12px 30px rgba(0,0,0,.12);
            border:1px solid #e5e7eb; display:none;
        }
        .toast-success{border-left:6px solid #16a34a;}
        .toast-error{border-left:6px solid #dc2626;}
        .toast h3{margin:0 0 6px;font-size:15px;}
        .toast .line{font-size:14px;color:#374151;}
        .toast .close{
            float:right; border:none; background:none; font-size:18px; cursor:pointer;
        }
    </style>
</head>
<body>
<div class="card">
    <div class="top">
        <div>
            <div style="font-weight:900;font-size:18px;">User Management</div>
            <div style="color:#6b7280;font-size:13px;">Add / Edit / Remove staff accounts</div>
        </div>

        <div style="display:flex;gap:10px;flex-wrap:wrap;">
            <button type="button" class="btn" onclick="history.back()">Back</button>
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

<% if (toastSuccess != null) { %>
<div id="toastSuccess" class="toast toast-success">
    <button class="close" onclick="document.getElementById('toastSuccess').style.display='none'">&times;</button>
    <h3>Success</h3>
    <div class="line"><%= toastSuccess %></div>
</div>
<% } %>

<% if (toastError != null) { %>
<div id="toastError" class="toast toast-error">
    <button class="close" onclick="document.getElementById('toastError').style.display='none'">&times;</button>
    <h3>Error</h3>
    <div class="line"><%= toastError %></div>
</div>
<% } %>

<script>
    (function () {
        var successToast = document.getElementById("toastSuccess");
        if (successToast) {
            successToast.style.display = "block";
            setTimeout(function () {
                successToast.style.display = "none";
            }, 3000);
        }

        var errorToast = document.getElementById("toastError");
        if (errorToast) {
            errorToast.style.display = "block";
            setTimeout(function () {
                errorToast.style.display = "none";
            }, 3500);
        }
    })();
</script>
</body>
</html>