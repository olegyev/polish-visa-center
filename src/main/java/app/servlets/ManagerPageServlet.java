/*
package app.servlets;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.services.AdminService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagerPageServlet extends HttpServlet {
    private AdminService adminService = new AdminService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/manager_page.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Admin manager = (Admin) session.getAttribute("admin_data");

        List<Admin> operatorsFromManagersCity = adminService.findAdminsByPositionAndCity(AdminPosition.OPERATOR, manager.getCity());
        Admin operator = null;
        List<Admin> operators = new ArrayList<Admin>();

        String action = req.getParameter("action");

        if (action.equals("show_all_operators")) {
            operators = operatorsFromManagersCity;

        } else if (action.equals("find_operator")) {
            String searchKey = req.getParameter("search_key").trim();
            long id = 0;

            try {
                id = Long.parseLong(searchKey);
            } catch (NumberFormatException ignored) {
            }

            if (id != 0) {
                for (Admin operatorById : operatorsFromManagersCity) {
                    if (operatorById.getId() == id) {
                        operator = operatorById;
                        break;
                    }
                }
            } else if (searchKey.contains("@")) {
                for (Admin operatorByEmail : operatorsFromManagersCity) {
                    if (operatorByEmail.getEmail().equals(searchKey)) {
                        operator = operatorByEmail;
                        break;
                    }
                }
            } else {
                for (Admin operatorByLastName : operatorsFromManagersCity) {
                    if (operatorByLastName.getLastName().equalsIgnoreCase(searchKey)) {
                        operator = operatorByLastName;
                        break;
                    }
                }
            }

            if (operator != null) {
                operators.add(operator);
            }
        }

        req.setAttribute("operators", operators);
        req.setAttribute("no_matches", "No matches");
        doGet(req, resp);
    }
}*/
