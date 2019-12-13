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

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name").trim().toUpperCase();
        String lastName = req.getParameter("last_name").trim().toUpperCase();
        String dateOfBirth = req.getParameter("date_of_birth");
        ClientOccupation occupation = ClientOccupation.valueOf(req.getParameter("occupation"));
        String email = req.getParameter("email").trim();
        String phoneNumber = req.getParameter("phone_number").trim();
        String password = PasswordEncryptor.encryptWithMd5(req.getParameter("password"));
        String persDataAgree = req.getParameter("pers_data_agree");
        boolean isAgree = false;

        if (persDataAgree != null) {
            isAgree = true;
        }

        Client client = null;
        try {
            client = new Client(firstName, lastName, new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth), occupation, email, phoneNumber, password, isAgree);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ClientService service = new ClientService();
        boolean isRegistered = service.addClient(client);

        if (isRegistered) {
            resp.sendRedirect("/login");
        } else {
            req.setAttribute("register_error", "Client with such e-mail already registered!");
            doGet(req, resp);
        }
    }
}