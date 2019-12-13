package app.servlets;

import app.entities.Admin;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuperAdminPageServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/super_admin_page.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminService service = new AdminService();
        Admin admin = null;
        List<Admin> admins = new ArrayList<Admin>();

        String action = req.getParameter("action");

        if (action.equals("show_all_admins")) {
            admins = service.findAllAdmins();

        } else if (action.equals("find_admin")) {
            String searchKey = req.getParameter("search_key").trim();
            long id = 0;

            try {
                id = Long.parseLong(searchKey);
            } catch (NumberFormatException e) {
            }

            if (id != 0) {
                admin = service.findAdminById(id);
            } else if (searchKey.contains("@")) {
                admin = service.findAdminByEmail(searchKey);
            } else {
                admins = service.findAdminByLastName(searchKey);
            }

            if (admin != null) {
                admins.add(admin);
            }
        }

        req.setAttribute("admins", admins);
        req.setAttribute("no_matches", "No matches :(");
        doGet(req, resp);
    }
}