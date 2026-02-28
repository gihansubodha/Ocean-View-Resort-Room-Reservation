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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = authService.authenticate(username, password);
            if (user != null) {
                HttpSession session = req.getSession(true);
                session.setAttribute("authUser", user);
                session.setMaxInactiveInterval(15 * 60); // 15 mins

                resp.sendRedirect(req.getContextPath() + "/app/home.jsp");
            } else {
                req.setAttribute("error", "Invalid username/password or inactive user.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
