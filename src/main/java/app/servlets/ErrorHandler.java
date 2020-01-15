/*
package app.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer errorCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String errorUri = (String) req.getAttribute("javax.servlet.error.request_uri");

        req.setAttribute("error_code", errorCode);
        req.setAttribute("error_uri", errorUri);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/errors/error.jsp");
        requestDispatcher.forward(req, resp);
    }
}*/
