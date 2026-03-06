package com.oceanview.web;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;
import com.oceanview.model.User;
import com.oceanview.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        UserDAO userDAO = new UserDAOImpl();
        authService = new AuthService(userDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = safe(req.getParameter("username"));
        String password = safe(req.getParameter("password"));

        try {
            User user = authService.authenticate(username, password);

            if (user != null) {
                HttpSession session = req.getSession(true);
                session.setAttribute("authUser", user);
                session.setAttribute("role", user.getRole());
                session.setMaxInactiveInterval(1 * 60); // 1 minute for testing

                session.setAttribute("toastSuccess", "Login successful. Welcome to Ocean View Resort.");

                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/app/home.jsp");
                }
                return;
            }

            req.setAttribute("error", "Invalid username/password or inactive user.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}