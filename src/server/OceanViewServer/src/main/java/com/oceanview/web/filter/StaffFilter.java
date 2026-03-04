package com.oceanview.web.filter;

import com.oceanview.model.User;
import com.oceanview.security.Roles;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/app/*")
public class StaffFilter implements Filter {

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

        // staff area allows STAFF and ADMIN (admin can still open staff pages if you want)
        String role = u.getRole();
        if (!Roles.STAFF.equalsIgnoreCase(role) && !Roles.ADMIN.equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Access denied");
            return;
        }

        chain.doFilter(request, response);
    }
}