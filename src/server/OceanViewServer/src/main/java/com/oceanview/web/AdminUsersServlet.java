package com.oceanview.web.admin;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;
import com.oceanview.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = userDAO.findAll();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/admin/users_list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Load users failed", e);
        }
    }
}