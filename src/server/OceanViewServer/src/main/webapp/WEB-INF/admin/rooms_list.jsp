<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 21:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Room" %>
<%
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Rooms</title>
    <style>
        body{font-family:Arial;background:#f6f7fb;margin:0;padding:24px;}
        .card{background:#fff;border:1px solid #e6eaf2;border-radius:16px;padding:16px;box-shadow:0 10px 26px rgba(17,24,39,.06);}
        .top{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;}
        .btn{background:#0f172a;color:#fff;border:none;padding:10px 14px;border-radius:12px;text-decoration:none;font-weight:800;}
        table{width:100%;border-collapse:collapse;}
        th,td{padding:10px;border-bottom:1px solid #eef2ff;text-align:left;font-size:14px;}
        .dangerBtn{background:#dc2626;color:#fff;border:none;padding:8px 10px;border-radius:10px;cursor:pointer;}
        .edit{background:#2563eb;color:#fff;padding:8px 10px;border-radius:10px;text-decoration:none;font-weight:800;}
    </style>
</head>
<body>
<div class="card">
    <div class="top">
        <div>
            <div style="font-weight:900;font-size:18px;">Room Management</div>
            <div style="color:#64748b;font-size:13px;">Add / Edit / Remove rooms</div>
        </div>
        <a class="btn" href="<%=request.getContextPath()%>/admin/rooms/add">+ Add Room</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>ID</th><th>Room #</th><th>Room Type ID</th><th>Status</th><th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% if (rooms != null) for (Room r : rooms) { %>
        <tr>
            <td><%=r.getRoomId()%></td>
            <td><%=r.getRoomNumber()%></td>
            <td><%=r.getRoomTypeId()%></td>
            <td><%=r.getStatus()%></td>
            <td>
                <a class="edit" href="<%=request.getContextPath()%>/admin/rooms/edit?id=<%=r.getRoomId()%>">Edit</a>
                <form method="post" action="<%=request.getContextPath()%>/admin/rooms/delete" style="display:inline;">
                    <input type="hidden" name="id" value="<%=r.getRoomId()%>"/>
                    <button class="dangerBtn" type="submit" onclick="return confirm('Delete this room?')">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
