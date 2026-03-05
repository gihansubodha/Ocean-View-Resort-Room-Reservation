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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        .actionsCell{ display:flex; gap:10px; flex-wrap:wrap; }
        .dangerBtn{
            border:none; cursor:pointer;
            padding:10px 14px; border-radius:12px; font-weight:900;
            background: var(--danger); color:#fff;
        }
        .dangerBtn:hover{ background: var(--danger2); }
        .statusCell{
            font-weight:900;
        }
        .st-AVAILABLE{ color:#16A34A; }
        .st-RESERVED{ color:#DC2626; }
        .st-OCCUPIED{ color:#7C3AED; }
        .st-MAINTENANCE{ color:#D97706; }
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
                    <div class="sub">Admin • Rooms</div>
                </div>
            </div>

            <div class="nav-actions">
                <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
                    <i class="fa-solid fa-chart-line"></i> Dashboard
                </a>
                <a class="btn btn-primary" href="<%=request.getContextPath()%>/admin/rooms/add">
                    <i class="fa-solid fa-plus"></i> Add Room
                </a>
                <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
                    <i class="fa-solid fa-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Room Management</h2>
            <div class="page-subtitle">Add / Edit / Remove rooms and maintain operational status.</div>

            <div class="section">
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Room #</th>
                        <th>Room Type ID</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (rooms != null) for (Room r : rooms) { %>
                    <%
                        String st = (r.getStatus() == null ? "" : String.valueOf(r.getStatus()));
                        String stCls = "st-" + st;
                    %>
                    <tr>
                        <td><%=r.getRoomId()%></td>
                        <td><%=r.getRoomNumber()%></td>
                        <td><%=r.getRoomTypeId()%></td>
                        <td class="statusCell <%=stCls%>"><%=st%></td>
                        <td class="actionsCell">
                            <a class="btn btn-soft btn-mini" href="<%=request.getContextPath()%>/admin/rooms/edit?id=<%=r.getRoomId()%>">
                                <i class="fa-solid fa-pen"></i> Edit
                            </a>
                            <form method="post" action="<%=request.getContextPath()%>/admin/rooms/delete" style="display:inline;">
                                <input type="hidden" name="id" value="<%=r.getRoomId()%>"/>
                                <button class="dangerBtn btn-mini" type="submit" onclick="return confirm('Delete this room?')">
                                    <i class="fa-solid fa-trash"></i> Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } %>

                    <% if (rooms == null || rooms.isEmpty()) { %>
                    <tr><td colspan="5">No rooms found.</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

</body>
</html>