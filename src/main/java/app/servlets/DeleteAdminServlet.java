package app.servlets;

import app.entities.Admin;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteAdminServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long adminToDeleteId = Long.parseLong(req.getParameter("adminId"));
            Admin adminToDelete = adminService.findAdminById(adminToDeleteId);
            Admin thisAdmin = (Admin) req.getSession().getAttribute("admin_data");
            long thisAdminId = thisAdmin.getId();

            req.setAttribute("admin_to_delete_id", adminToDeleteId);
            req.setAttribute("this_admin_id", thisAdminId);
            req.setAttribute("admin_to_delete_first_name", adminToDelete.getFirstName());
            req.setAttribute("admin_to_delete_last_name", adminToDelete.getLastName());
            req.setAttribute("delete_admin_error", "You cannot delete yourself!");

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/super-admin-page/delete_admin.jsp");
            requestDispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/super-admin-page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long adminToDeleteId = Long.parseLong(req.getParameter("adminId"));
        adminService.deleteAdmin(adminToDeleteId);
        resp.sendRedirect("/super-admin-page");
    }
}