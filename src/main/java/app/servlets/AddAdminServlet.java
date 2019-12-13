package app.servlets;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.services.AdminService;
import app.util.PasswordEncryptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add_admin.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = AdminPosition.valueOf(req.getParameter("position"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();
        String password = PasswordEncryptor.encryptWithMd5(req.getParameter("password"));

        Admin admin = new Admin(firstName, lastName, position, email, phoneNumber, password);

        AdminService service = new AdminService();
        boolean isAdded = service.addAdmin(admin);

        if (isAdded) {
            resp.sendRedirect("/super-admin-page");
        } else {
            req.setAttribute("add_admin_error", "Admin with such e-mail already registered!");
            doGet(req, resp);
        }
    }
}