/*
package app.servlets.filters;

import app.entities.Appointment;
import app.entities.Client;
import app.services.AppointmentService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClientFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        Client clientData = (Client) session.getAttribute("client_data");

        if (clientData != null) {
            AppointmentService appointmentService = new AppointmentService();
            long clientId = clientData.getId();
            Appointment appointment = appointmentService.findAppointmentByClientId(clientId);

            if (appointment != null) {
                chain.doFilter(req, resp);
            } else {
                response.sendRedirect("/book-appointment");
            }

        } else {
            response.sendRedirect("/login");
        }
    }
}*/
