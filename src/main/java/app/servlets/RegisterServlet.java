package app.servlets;

import app.entities.Client;
import app.entities.enums.ClientOccupation;
import app.services.ClientService;
import app.util.PasswordEncryptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterServlet extends HttpServlet {
    private ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        Date dateOfBirth = null;
        try {
            dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("date_of_birth"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ClientOccupation occupation = ClientOccupation.valueOf(req.getParameter("occupation"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();
        String password = PasswordEncryptor.encryptWithMd5(req.getParameter("password"));
        String personalDataAgree = req.getParameter("personal_data_agree");
        boolean isAgree = false;

        if (personalDataAgree != null) {
            isAgree = true;
        }

        Client client = null;
        if (dateOfBirth != null) {
            client = new Client(firstName, lastName, dateOfBirth, occupation, email, phoneNumber, password, isAgree);
        }

        boolean isRegistered = false;
        if (client != null) {
            isRegistered = clientService.addClient(client);
        }

        if (isRegistered) {
            resp.sendRedirect("/login");
        } else {
            req.setAttribute("register_error", "Client with such e-mail already registered!");
            doGet(req, resp);
        }
    }
}