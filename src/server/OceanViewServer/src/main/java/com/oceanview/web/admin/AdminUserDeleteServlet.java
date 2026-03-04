package com.oceanview.web.admin;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/users/delete")
public class AdminUserDeleteServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = 0;
        try { id = Integer.parseInt(req.getParameter("id")); } catch (Exception ignored) {}

        try {
            userDAO.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        } catch (Exception e) {
            throw new ServletException("Delete user failed", e);
        }
    }
}