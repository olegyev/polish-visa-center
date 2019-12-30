package app.servlets;

import app.entities.Admin;
import app.entities.enums.City;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectorPageServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/director_page.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin employee = null;
        List<Admin> employees = new ArrayList<Admin>();

        String action = req.getParameter("action");

        if (action.equals("show_all_employees")) {
            employees = adminService.findAllAdmins();

        } else if (action.equals("find_employee")) {
            String searchKey = req.getParameter("search_key").trim();
            long id = 0;

            try {
                id = Long.parseLong(searchKey);
            } catch (NumberFormatException ignored) {
            }

            if (id != 0) {
                employee = adminService.findAdminById(id);
            } else if (searchKey.contains("@")) {
                employee = adminService.findAdminByEmail(searchKey);
            } else {
                boolean found = false;

                City[] cities = City.values();
                for (City city : cities) {
                    if (city.toString().equals(searchKey.toUpperCase())) {
                        employees = adminService.findAdminsByCity(City.valueOf(searchKey.toUpperCase()));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    employees = adminService.findAdminByLastName(searchKey);
                }
            }

            if (employee != null) {
                employees.add(employee);
            }
        }

        req.setAttribute("employees", employees);
        req.setAttribute("no_matches", "No matches");
        doGet(req, resp);
    }
}