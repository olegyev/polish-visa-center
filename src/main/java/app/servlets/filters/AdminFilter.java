/*
package app.servlets.filters;

import app.entities.Admin;
import app.entities.enums.AdminPosition;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        Admin adminData = (Admin) session.getAttribute("admin_data");

        if (adminData != null) {
            AdminPosition position = adminData.getPosition();

            if (position.equals(AdminPosition.OPERATOR)) {
                response.sendRedirect("/operator-page");
            } else if (position.equals(AdminPosition.MANAGER)) {
                response.sendRedirect("/manager-page");
            } else if (position.equals(AdminPosition.DIRECTOR)) {
                response.sendRedirect("/director-page");
            }

        } else {
            response.sendRedirect("/login");
        }
    }
}*/
