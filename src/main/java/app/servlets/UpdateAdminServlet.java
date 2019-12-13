package app.servlets;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long adminId = Long.parseLong(req.getParameter("adminId"));
        AdminService service = new AdminService();
        Admin admin = service.findAdminById(adminId);

        req.getSession().setAttribute("admin", admin);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/update_admin.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin admin = (Admin) req.getSession().getAttribute("admin");

        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = AdminPosition.valueOf(req.getParameter("position"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();

        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setPosition(position);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);

        AdminService service = new AdminService();
        Admin updatedAdmin = service.updateAdmin(admin);

        if (updatedAdmin != null) {
            resp.sendRedirect("/super-admin-page");
        } else {
            req.setAttribute("update_admin_error", "There is another admin with such e-mail in the database!");
            doGet(req, resp);
        }
    }
}