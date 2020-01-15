/*
package app.servlets.filters;

import app.entities.Client;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BookAppointmentFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        Client clientData = (Client) session.getAttribute("client_data");

        if (clientData != null) {
            // if (visaStatus == null) { chain.doFilter(req, resp); }
            // else { response.sendRedirect("/client-page"); } --> если у клиента выставлен статус визы (сдал документы в визовый центр),
            // значит он не может бронировать новый визит, пока его visaStatus снова не обнулится,
            // что равносильно возврату ему его документов с визой или без.
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect("/login");
        }
    }
}*/
