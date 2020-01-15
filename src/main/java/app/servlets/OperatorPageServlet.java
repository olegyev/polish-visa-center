/*
package app.servlets;

import app.entities.Admin;
import app.entities.Appointment;
import app.services.AppointmentService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class OperatorPageServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/operator_page.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Admin operator = (Admin) session.getAttribute("admin_data");

        Appointment appointment = null;
        List<Appointment> appointments = new ArrayList<Appointment>();

        String action = req.getParameter("action");

        switch (action) {
            case "show_all_appointments":
                appointments = appointmentService.findAppointmentsByCity(operator.getCity());
                break;
            case "today":
                Date today = new Date();
                appointments = appointmentService.findAppointmentsByCityAndDate(operator.getCity(), today);
                break;
            case "find_appointment":
                String searchKey = req.getParameter("search_key").trim();
                String regExp = "^\\d{4}-\\d{2}-\\d{2}$";
                Pattern pattern = Pattern.compile(regExp);
                long clientId = 0;

                try {
                    clientId = Long.parseLong(searchKey);
                } catch (NumberFormatException ignored) {
                }

                if (clientId != 0) {
                    appointment = appointmentService.findAppointmentByClientId(clientId);
                } else if (searchKey.contains("@")) {
                    appointment = appointmentService.findAppointmentByClientEmail(searchKey);
                } else if (pattern.matcher(searchKey).matches()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        appointments = appointmentService.findAppointmentsByCityAndDate(operator.getCity(), format.parse(searchKey));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    appointments = appointmentService.findAppointmentsByClientLastName(searchKey.toUpperCase());
                }

                if (appointment != null) {
                    appointments.add(appointment);
                }
                break;
        }

        req.setAttribute("operator", operator);
        req.setAttribute("appointments", appointments);
        req.setAttribute("no_matches", "No matches");
        doGet(req, resp);
    }
}*/
