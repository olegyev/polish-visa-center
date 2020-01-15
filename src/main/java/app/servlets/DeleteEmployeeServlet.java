/*
package app.servlets;

import app.entities.Admin;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteEmployeeServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long employeeToDeleteId = Long.parseLong(req.getParameter("employeeId"));
            Admin employeeToDelete = adminService.findAdminById(employeeToDeleteId);
            Admin director = (Admin) req.getSession().getAttribute("admin_data");
            long directorId = director.getId();

            req.setAttribute("employee_to_delete_id", employeeToDeleteId);
            req.setAttribute("director_id", directorId);
            req.setAttribute("employee_to_delete_first_name", employeeToDelete.getFirstName());
            req.setAttribute("employee_to_delete_last_name", employeeToDelete.getLastName());
            req.setAttribute("delete_employee_error", "You cannot delete yourself!");

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/director-page/delete_employee.jsp");
            requestDispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/director-page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long employeeToDeleteId = Long.parseLong(req.getParameter("employeeId"));
        adminService.deleteAdmin(employeeToDeleteId);
        resp.sendRedirect("/director-page");
    }
}*/
