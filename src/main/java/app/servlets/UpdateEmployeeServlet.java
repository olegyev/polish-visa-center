/*
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

public class UpdateEmployeeServlet extends HttpServlet {
    AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long employeeToUpdateId = Long.parseLong(req.getParameter("employeeId"));
            Admin employeeToUpdate = adminService.findAdminById(employeeToUpdateId);
            Admin director = (Admin) req.getSession().getAttribute("admin_data");
            long directorId = director.getId();

            req.setAttribute("employee_to_update_id", employeeToUpdateId);
            req.setAttribute("director_id", directorId);

            req.getSession().setAttribute("employee", employeeToUpdate);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/director-page/update_employee.jsp");
            requestDispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/director-page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin employeeToUpdate = (Admin) req.getSession().getAttribute("employee");
        Admin director = (Admin) req.getSession().getAttribute("admin_data");

        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        AdminPosition position = employeeToUpdate.getPosition().equals(AdminPosition.DIRECTOR) && employeeToUpdate.getId() == director.getId() ?
                                 AdminPosition.DIRECTOR : AdminPosition.valueOf(req.getParameter("position"));
        City city = City.valueOf(req.getParameter("city"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();

        employeeToUpdate.setFirstName(firstName);
        employeeToUpdate.setLastName(lastName);
        employeeToUpdate.setPosition(position);
        employeeToUpdate.setCity(city);
        employeeToUpdate.setEmail(email);
        employeeToUpdate.setPhoneNumber(phoneNumber);

        Admin updatedEmployee = adminService.updateAdmin(employeeToUpdate);

        if (updatedEmployee != null) {
            resp.sendRedirect("/director-page");
        } else {
            req.setAttribute("update_employee_error", "There is another employee with such e-mail in the database!");
            doGet(req, resp);
        }
    }
}*/
