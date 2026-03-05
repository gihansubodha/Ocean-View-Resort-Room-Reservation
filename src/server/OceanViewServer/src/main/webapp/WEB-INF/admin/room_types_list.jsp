<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 21:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%
    List<RoomType> types = (List<RoomType>) request.getAttribute("types");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Room Types</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        .pillMini{
            display:inline-flex; align-items:center; gap:6px;
            padding:4px 10px; border-radius:999px; font-size:12px; font-weight:900;
            border:1px solid var(--border); background:#fff; color: var(--muted);
        }
        .pillYes{ background:#ECFDF5; border-color:#A7F3D0; color:#065F46; }
        .pillNo{ background:#FEF2F2; border-color:#FECACA; color:#991B1B; }
        .actionsCell{ display:flex; gap:10px; flex-wrap:wrap; }
        .dangerBtn{
            border:none; cursor:pointer;
            padding:10px 14px; border-radius:12px; font-weight:900;
            background: var(--danger); color:#fff;
        }
        .dangerBtn:hover{ background: var(--danger2); }
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
                    <div class="sub">Admin • Room Types</div>
                </div>
            </div>

            <div class="nav-actions">
                <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
                    <i class="fa-solid fa-chart-line"></i> Dashboard
                </a>
                <a class="btn btn-primary" href="<%=request.getContextPath()%>/admin/room-types/add">
                    <i class="fa-solid fa-plus"></i> Add Room Type
                </a>
                <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
                    <i class="fa-solid fa-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title">Room Type Management</h2>
            <div class="page-subtitle">Add / Edit / Remove room types and pricing rules.</div>

            <div class="section">
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Rate</th>
                        <th>Max Guests</th>
                        <th>Active</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (types != null) for (RoomType t : types) { %>
                    <tr>
                        <td><%=t.getRoomTypeId()%></td>
                        <td><%=t.getTypeName()%></td>
                        <td><%=t.getNightlyRate()%></td>
                        <td><%=t.getMaxGuests()%></td>
                        <td>
                            <span class="pillMini <%= t.isActive() ? "pillYes" : "pillNo" %>">
                                <i class="fa-solid <%= t.isActive() ? "fa-circle-check" : "fa-circle-xmark" %>"></i>
                                <%= t.isActive() ? "Yes" : "No" %>
                            </span>
                        </td>
                        <td class="actionsCell">
                            <a class="btn btn-soft btn-mini" href="<%=request.getContextPath()%>/admin/room-types/edit?id=<%=t.getRoomTypeId()%>">
                                <i class="fa-solid fa-pen"></i> Edit
                            </a>
                            <form method="post" action="<%=request.getContextPath()%>/admin/room-types/delete" style="display:inline;">
                                <input type="hidden" name="id" value="<%=t.getRoomTypeId()%>"/>
                                <button class="dangerBtn btn-mini" type="submit" onclick="return confirm('Delete this room type?')">
                                    <i class="fa-solid fa-trash"></i> Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } %>

                    <% if (types == null || types.isEmpty()) { %>
                    <tr><td colspan="6">No room types found.</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

</body>
</html>