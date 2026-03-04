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
    <style>
        body{font-family:Arial;background:#f6f7fb;margin:0;padding:24px;}
        .card{max-width:860px;margin:auto;background:#fff;border:1px solid #e6eaf2;border-radius:16px;padding:18px;box-shadow:0 10px 26px rgba(17,24,39,.06);}
        label{display:block;color:#64748b;font-size:12px;margin:10px 0 6px;}
        input,select,textarea{padding:10px 12px;border-radius:12px;border:1px solid #e5e7eb;min-width:260px;}
        textarea{min-width:520px;min-height:90px;}
        .row{display:flex;gap:12px;flex-wrap:wrap;}
        .btn{background:#0f172a;color:#fff;border:none;padding:10px 14px;border-radius:12px;font-weight:900;cursor:pointer;}
        .back{background:#2563eb;text-decoration:none;display:inline-block;}
        .err{background:#fee2e2;color:#991b1b;padding:10px 12px;border-radius:12px;margin:10px 0;}
    </style>
</head>
<body>
<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <div style="font-weight:900;font-size:18px;"><%= edit ? "Edit Room Type" : "Add Room Type" %></div>
        <a class="btn back" href="<%=request.getContextPath()%>/admin/room-types">Back</a>
    </div>

    <% if (error != null) { %><div class="err"><%=error%></div><% } %>

    <form method="post" action="<%=request.getContextPath()%><%= edit ? "/admin/room-types/edit" : "/admin/room-types/add" %>">
        <input type="hidden" name="roomTypeId" value="<%= edit ? rt.getRoomTypeId() : 0 %>"/>

        <div class="row">
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

        <label>Description</label>
        <textarea name="description"><%= edit && rt.getDescription()!=null ? rt.getDescription() : "" %></textarea>

        <div style="margin-top:14px;">
            <button class="btn" type="submit"><%= edit ? "Save Changes" : "Create Room Type" %></button>
        </div>
    </form>
</div>
</body>
</html>
