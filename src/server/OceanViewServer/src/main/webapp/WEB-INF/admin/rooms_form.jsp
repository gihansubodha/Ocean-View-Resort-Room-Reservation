<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 03/04/26
  Time: 21:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Room" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="java.util.List" %>
<%
    Room r = (Room) request.getAttribute("r");
    List<RoomType> types = (List<RoomType>) request.getAttribute("types");
    String error = (String) request.getAttribute("error");
    boolean edit = (r != null && r.getRoomId() > 0);
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= edit ? "Edit Room" : "Add Room" %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">

    <style>
        .wide{ max-width: 980px; margin: 0 auto; }
        .row3{
            display:grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 14px 18px;
        }
        @media (max-width: 980px){ .row3{ grid-template-columns: 1fr; } }
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
                    <div class="sub">Admin • Rooms</div>
                </div>
            </div>

            <div class="nav-actions">
                <button type="button" class="btn btn-soft" onclick="history.back()">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </button>
                <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/dashboard">
                    <i class="fa-solid fa-chart-line"></i> Dashboard
                </a>
                <a class="btn btn-slate" href="<%=request.getContextPath()%>/admin/rooms">
                    <i class="fa-solid fa-list"></i> Back to List
                </a>
                <a class="btn btn-danger" href="<%=request.getContextPath()%>/logout">
                    <i class="fa-solid fa-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>

        <div class="card card-pad">
            <h2 class="page-title"><%= edit ? "Edit Room" : "Add Room" %></h2>
            <div class="page-subtitle">Maintain room numbers, types and operational status.</div>

            <% if (error != null) { %>
            <div class="alert alert-err"><%= error %></div>
            <% } %>

            <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/rooms/edit" : "/admin/rooms/add" %>">
                <input type="hidden" name="roomId" value="<%= edit ? r.getRoomId() : 0 %>"/>

                <div class="section">
                    <div class="row3">
                        <div>
                            <label>Room Number</label>
                            <input type="number" name="roomNumber" value="<%= edit ? r.getRoomNumber() : "" %>" required/>
                        </div>

                        <div>
                            <label>Room Type</label>
                            <select name="roomTypeId" required>
                                <% if (types != null) for (RoomType t : types) { %>
                                <option value="<%=t.getRoomTypeId()%>" <%= edit && t.getRoomTypeId()==r.getRoomTypeId() ? "selected":"" %>>
                                    <%=t.getTypeName()%> (ID <%=t.getRoomTypeId()%>)
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div>
                            <label>Status</label>
                            <select name="status" required>
                                <%
                                    String st = edit ? r.getStatus() : "AVAILABLE";
                                %>
                                <option value="AVAILABLE" <%= "AVAILABLE".equals(st)?"selected":"" %>>AVAILABLE</option>
                                <option value="RESERVED" <%= "RESERVED".equals(st)?"selected":"" %>>RESERVED</option>
                                <option value="OCCUPIED" <%= "OCCUPIED".equals(st)?"selected":"" %>>OCCUPIED</option>
                                <option value="MAINTENANCE" <%= "MAINTENANCE".equals(st)?"selected":"" %>>MAINTENANCE</option>
                            </select>
                        </div>
                    </div>

                    <div class="btnbar" style="margin-top:14px;">
                        <button class="btn btn-primary" type="submit">
                            <i class="fa-solid fa-floppy-disk"></i> <%= edit ? "Save Changes" : "Create Room" %>
                        </button>
                        <a class="btn btn-soft" href="<%=request.getContextPath()%>/admin/rooms">
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