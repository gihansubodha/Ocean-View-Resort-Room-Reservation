package com.oceanview.web.admin;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;
import com.oceanview.model.User;

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

        HttpSession session = req.getSession();

        try {
            User target = userDAO.findById(id);
            if (target == null) {
                session.setAttribute("toastError", "User not found.");
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }

            User authUser = (User) session.getAttribute("authUser");
            if (authUser != null && authUser.getUserId() == id) {
                session.setAttribute("toastError", "You cannot delete the currently logged-in account.");
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }

            userDAO.delete(id);
            session.setAttribute("toastSuccess", "User deleted successfully: " + target.getUsername());
            resp.sendRedirect(req.getContextPath() + "/admin/users");

        } catch (Exception e) {
            session.setAttribute("toastError", "Delete user failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        }
    }
}