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
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AddOperatorServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Admin manager = (Admin) session.getAttribute("admin_data");

        req.setAttribute("city", manager.getCity().getTitle());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/manager-page/add_operator.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Admin manager = (Admin) session.getAttribute("admin_data");

        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = AdminPosition.OPERATOR;
        City city = manager.getCity();
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();
        String password = PasswordEncryptor.encryptWithMd5(req.getParameter("password"));

        Admin operator = new Admin(firstName, lastName, position, city, email, phoneNumber, password);

        boolean isAdded = adminService.addAdmin(operator);

        if (isAdded) {
            resp.sendRedirect("/manager-page");
        } else {
            req.setAttribute("add_operator_error", "Employee with such e-mail already registered!");
            doGet(req, resp);
        }
    }
}*/
