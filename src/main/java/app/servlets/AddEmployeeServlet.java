/*
package app.servlets;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;
import app.services.AdminService;
import app.util.PasswordEncryptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddEmployeeServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/director-page/add_employee.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = AdminPosition.valueOf(req.getParameter("position"));
        City city = City.valueOf(req.getParameter("city"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();
        String password = PasswordEncryptor.encryptWithMd5(req.getParameter("password"));

        Admin employee = new Admin(firstName, lastName, position, city, email, phoneNumber, password);

        boolean isAdded = adminService.addAdmin(employee);

        if (isAdded) {
            resp.sendRedirect("/director-page");
        } else {
            req.setAttribute("add_employee_error", "Employee with such e-mail already registered!");
            doGet(req, resp);
        }
    }
}*/
