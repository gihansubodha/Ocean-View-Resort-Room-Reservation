<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%
    RoomType rt = (RoomType) request.getAttribute("rt");
    String error = (String) request.getAttribute("error");
    boolean edit = (rt != null && rt.getRoomTypeId() > 0);
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= edit ? "Edit Room Type" : "Add Room Type" %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">

    <style>
        /* page-only (safe) */
        .wide{ max-width: 980px; margin: 0 auto; }
        .row4{
            display:grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 14px 18px;
        }
        @media (max-width: 980px){ .row4{ grid-template-columns: repeat(2, minmax(0,1fr)); } }
        @media (max-width: 620px){ .row4{ grid-template-columns: 1fr; } }
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
                    <div class="sub">Admin • Room Types</div>
                </div>
            </div>

            <div class="nav-actions">
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
                    <i class="fa-solid fa-chart-line"></i> Dashboard
                </a>
                <a class="btn btn-slate" href="<%=request.getContextPath()%>/admin/room-types">
                    <i class="fa-solid fa-list"></i> Back to List
                </a>
                <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
                    <i class="fa-solid fa-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title"><%= edit ? "Edit Room Type" : "Add Room Type" %></h2>
            <div class="page-subtitle">Maintain room types, rates, capacity and descriptions.</div>

            <% if (error != null) { %>
            <div class="alert alert-err"><%= error %></div>
            <% } %>

            <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/room-types/edit" : "/admin/room-types/add" %>">
                <input type="hidden" name="roomTypeId" value="<%= edit ? rt.getRoomTypeId() : 0 %>"/>

                <div class="section">
                    <div class="row4">
                        <div>
                            <label>Type Name</label>
                            <input name="typeName" value="<%= edit ? rt.getTypeName() : "" %>" required/>
                        </div>

                        <div>
                            <label>Nightly Rate</label>
                            <input name="nightlyRate" value="<%= edit ? rt.getNightlyRate() : "" %>" required/>
                        </div>

                        <div>
                            <label>Max Guests</label>
                            <input type="number" name="maxGuests" value="<%= edit ? rt.getMaxGuests() : "" %>" required/>
                        </div>

                        <div>
                            <label>Active</label>
                            <select name="isActive">
                                <option value="1" <%= !edit || rt.isActive() ? "selected":"" %>>Yes</option>
                                <option value="0" <%= edit && !rt.isActive() ? "selected":"" %>>No</option>
                            </select>
                        </div>
                    </div>

                    <div style="margin-top:14px;">
                        <label>Description</label>
                        <textarea name="description"><%= edit && rt.getDescription()!=null ? rt.getDescription() : "" %></textarea>
                    </div>

                    <div class="btnbar" style="margin-top:14px;">
                        <button class="btn btn-primary" type="submit">
                            <i class="fa-solid fa-floppy-disk"></i> <%= edit ? "Save Changes" : "Create Room Type" %>
                        </button>
                        <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/room-types">
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