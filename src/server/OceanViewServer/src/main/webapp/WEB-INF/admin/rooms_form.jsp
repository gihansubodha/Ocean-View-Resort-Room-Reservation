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
    <style>
        body{font-family:Arial;background:#f6f7fb;margin:0;padding:24px;}
        .card{max-width:820px;margin:auto;background:#fff;border:1px solid #e6eaf2;border-radius:16px;padding:18px;box-shadow:0 10px 26px rgba(17,24,39,.06);}
        label{display:block;color:#64748b;font-size:12px;margin:10px 0 6px;}
        input,select{padding:10px 12px;border-radius:12px;border:1px solid #e5e7eb;min-width:260px;}
        .row{display:flex;gap:12px;flex-wrap:wrap;}
        .btn{background:#0f172a;color:#fff;border:none;padding:10px 14px;border-radius:12px;font-weight:900;cursor:pointer;}
        .back{background:#2563eb;text-decoration:none;display:inline-block;}
        .err{background:#fee2e2;color:#991b1b;padding:10px 12px;border-radius:12px;margin:10px 0;}
    </style>
</head>
<body>
<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <div style="font-weight:900;font-size:18px;"><%= edit ? "Edit Room" : "Add Room" %></div>
        <a class="btn back" href="<%=request.getContextPath()%>/admin/rooms">Back</a>
    </div>

    <% if (error != null) { %><div class="err"><%=error%></div><% } %>

    <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/rooms/edit" : "/admin/rooms/add" %>">
        <input type="hidden" name="roomId" value="<%= edit ? r.getRoomId() : 0 %>"/>

        <div class="row">
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

        <div style="margin-top:14px;">
            <button class="btn" type="submit"><%= edit ? "Save Changes" : "Create Room" %></button>
        </div>
    </form>
</div>
</body>
</html>
