package app.servlets.filters;

import app.entities.Admin;
import app.entities.enums.AdminPosition;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OperatorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        Admin adminData = (Admin) session.getAttribute("admin_data");

        if (adminData != null) {
            AdminPosition position = adminData.getPosition();

            if (position.equals(AdminPosition.OPERATOR)) {
                chain.doFilter(req, resp);
            } else {
                response.sendRedirect("/login");
            }

        } else {
            response.sendRedirect("/login");
        }
    }
}
