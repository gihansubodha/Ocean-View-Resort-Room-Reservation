package com.oceanview.web.admin;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet({"/admin/users/add", "/admin/users/edit"})
public class AdminUserFormServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getServletPath().endsWith("/edit")) {
                int id = parseInt(req.getParameter("id"));
                User u = userDAO.findById(id);
                req.setAttribute("u", u);
            }
            req.getRequestDispatcher("/WEB-INF/admin/users_form.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("User form load failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean edit = req.getServletPath().endsWith("/edit");

        int userId = parseInt(req.getParameter("userId"));
        String username = safe(req.getParameter("username"));
        String role = safe(req.getParameter("role"));
        boolean active = "1".equals(req.getParameter("isActive"));
        String password = safe(req.getParameter("password"));

        HttpSession session = req.getSession();

        try {
            if (username.isEmpty()) throw new IllegalArgumentException("Username is required");
            if (!"ADMIN".equalsIgnoreCase(role) && !"STAFF".equalsIgnoreCase(role))
                throw new IllegalArgumentException("Role must be ADMIN or STAFF");

            if (!edit) {
                if (password.isEmpty()) throw new IllegalArgumentException("Password is required");

                User u = new User();
                u.setUsername(username);
                u.setRole(role.toUpperCase());
                u.setActive(active);
                u.setPasswordHash(PasswordUtil.createStoredPassword(password));

                userDAO.create(u);
                session.setAttribute("toastSuccess", "User created successfully: " + username);

            } else {
                User u = userDAO.findById(userId);
                if (u == null) throw new IllegalArgumentException("User not found");

                String oldRole = u.getRole();

                u.setUsername(username);
                u.setRole(role.toUpperCase());
                u.setActive(active);

                if (!password.isEmpty()) {
                    u.setPasswordHash(PasswordUtil.createStoredPassword(password));
                }

                if (u.getPasswordHash() == null || u.getPasswordHash().trim().isEmpty())
                    throw new IllegalStateException("Existing password_hash missing for this user");

                userDAO.update(u);

                if (!oldRole.equalsIgnoreCase(role)) {
                    session.setAttribute("toastSuccess",
                            "User updated successfully. Role changed from " + oldRole + " to " + role.toUpperCase() + ".");
                } else {
                    session.setAttribute("toastSuccess", "User updated successfully: " + username);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/admin/users");

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());

            User temp = new User();
            temp.setUserId(userId);
            temp.setUsername(username);
            temp.setRole(role);
            temp.setActive(active);
            req.setAttribute("u", temp);

            req.getRequestDispatcher("/WEB-INF/admin/users_form.jsp").forward(req, resp);
        }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}