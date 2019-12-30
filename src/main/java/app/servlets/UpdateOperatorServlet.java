package app.servlets;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateOperatorServlet extends HttpServlet {
    AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long operatorToUpdateId = Long.parseLong(req.getParameter("operatorId"));
            Admin operatorToUpdate = adminService.findAdminById(operatorToUpdateId);
            Admin manager = (Admin) req.getSession().getAttribute("admin_data");

            req.setAttribute("city", manager.getCity().getTitle());
            req.getSession().setAttribute("operator", operatorToUpdate);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/manager-page/update_operator.jsp");
            requestDispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/manager-page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin operatorToUpdate = (Admin) req.getSession().getAttribute("operator");
        Admin manager = (Admin) req.getSession().getAttribute("admin_data");

        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = AdminPosition.OPERATOR;
        City city = manager.getCity();
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();

        operatorToUpdate.setFirstName(firstName);
        operatorToUpdate.setLastName(lastName);
        operatorToUpdate.setPosition(position);
        operatorToUpdate.setCity(city);
        operatorToUpdate.setEmail(email);
        operatorToUpdate.setPhoneNumber(phoneNumber);

        Admin updatedOperator = adminService.updateAdmin(operatorToUpdate);

        if (updatedOperator != null) {
            resp.sendRedirect("/manager-page");
        } else {
            req.setAttribute("update_operator_error", "There is another employee with such e-mail in the database!");
            doGet(req, resp);
        }
    }
}