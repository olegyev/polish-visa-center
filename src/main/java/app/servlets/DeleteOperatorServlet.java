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

public class DeleteOperatorServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long operatorToDeleteId = Long.parseLong(req.getParameter("operatorId"));
            Admin operatorToDelete = adminService.findAdminById(operatorToDeleteId);

            req.setAttribute("operator_to_delete_id", operatorToDeleteId);
            req.setAttribute("operator_to_delete_first_name", operatorToDelete.getFirstName());
            req.setAttribute("operator_to_delete_last_name", operatorToDelete.getLastName());

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/manager-page/delete_operator.jsp");
            requestDispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/manager-page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long operatorToDeleteId = Long.parseLong(req.getParameter("operatorId"));
        adminService.deleteAdmin(operatorToDeleteId);
        resp.sendRedirect("/manager-page");
    }
}*/
