/*
package app.servlets;

import app.entities.Appointment;
import app.entities.Client;
import app.entities.User;
import app.services.AppointmentService;
import app.util.UriManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientPageServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User client = (Client) session.getAttribute("client_data");
        long clientId = client.getId();
        Appointment appointment = appointmentService.findAppointmentByClientId(clientId); // to show appointment manager

        boolean isAppointmentInPast = appointmentService.isAppointmentInPast(appointment);

        if (isAppointmentInPast) {
            appointment = null; // to show visa status
        }

        req.setAttribute("appointment", appointment);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client_page.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if (action.equals("update")) {
            UriManager uriManager = new UriManager();
            URI newUri = null;
            try {
                newUri = uriManager.appendUri("/book-appointment", "requestingUri=client-page");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if (newUri != null) {
                resp.sendRedirect(newUri.toString());
            }
        } else if (action.equals("delete")) {
            long appointmentToDeleteId = Long.parseLong(req.getParameter("appointmentId"));
            appointmentService.deleteAppointment(appointmentToDeleteId);
            resp.sendRedirect("/book-appointment");
        }
    }
}*/
