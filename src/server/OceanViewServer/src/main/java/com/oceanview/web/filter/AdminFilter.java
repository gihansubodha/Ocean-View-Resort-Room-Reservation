package com.oceanview.web.filter;

import com.oceanview.model.User;
import com.oceanview.security.Roles;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User u = (session == null) ? null : (User) session.getAttribute("authUser");

        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Please login");
            return;
        }
        if (!Roles.ADMIN.equalsIgnoreCase(u.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/app/home.jsp?error=Access denied");
            return;
        }

        chain.doFilter(request, response);
    }
}