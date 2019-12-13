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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long adminToDeleteId = Long.parseLong(req.getParameter("adminId"));
        AdminService service = new AdminService();
        Admin adminToDelete = service.findAdminById(adminToDeleteId);
        Admin thisAdmin = (Admin) req.getSession().getAttribute("admin_data");
        long thisAdminId = thisAdmin.getId();

        req.setAttribute("admin_to_delete_id", adminToDeleteId);
        req.setAttribute("this_admin_id", thisAdminId);
        req.setAttribute("admin_to_delete_first_name", adminToDelete.getFirstName());
        req.setAttribute("admin_to_delete_last_name", adminToDelete.getLastName());
        req.setAttribute("delete_admin_error", "You cannot delete yourself!");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/delete_admin.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long adminToDeleteId = Long.parseLong(req.getParameter("adminId"));
        AdminService service = new AdminService();
        service.deleteAdmin(adminToDeleteId);
        resp.sendRedirect("/super-admin-page");
    }
}