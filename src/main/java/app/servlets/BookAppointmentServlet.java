/*
package app.servlets;

import app.entities.Appointment;
import app.entities.Client;
import app.entities.enums.City;
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
import java.util.*;

public class BookAppointmentServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int workDayBegin = 9;
        int workDayEnd = 17;
        int stepInMinutes = 15;

        Map<String, String[]> disabledTimeInMinsk = appointmentService.findTimeToDisable(City.MINSK);
        Map<String, String[]> disabledTimeInGomel = appointmentService.findTimeToDisable(City.GOMEL);
        Map<String, String[]> disabledTimeInMogilev = appointmentService.findTimeToDisable(City.MOGILEV);
        Map<String, String[]> disabledTimeInBrest = appointmentService.findTimeToDisable(City.BREST);
        Map<String, String[]> disabledTimeInGrodno = appointmentService.findTimeToDisable(City.GRODNO);

        String[] disabledDatesInMinsk = appointmentService.findDatesToDisable(disabledTimeInMinsk, City.MINSK, workDayBegin, workDayEnd, stepInMinutes);
        String[] disabledDatesInGomel = appointmentService.findDatesToDisable(disabledTimeInGomel, City.GOMEL, workDayBegin, workDayEnd, stepInMinutes);
        String[] disabledDatesInMogilev = appointmentService.findDatesToDisable(disabledTimeInMogilev, City.MOGILEV, workDayBegin, workDayEnd, stepInMinutes);
        String[] disabledDatesInBrest = appointmentService.findDatesToDisable(disabledTimeInBrest, City.BREST, workDayBegin, workDayEnd, stepInMinutes);
        String[] disabledDatesInGrodno = appointmentService.findDatesToDisable(disabledTimeInGrodno, City.GRODNO, workDayBegin, workDayEnd, stepInMinutes);

        req.setAttribute("disabled_time_in_Minsk", disabledTimeInMinsk);
        req.setAttribute("disabled_time_in_Gomel", disabledTimeInGomel);
        req.setAttribute("disabled_time_in_Mogilev", disabledTimeInMogilev);
        req.setAttribute("disabled_time_in_Brest", disabledTimeInBrest);
        req.setAttribute("disabled_time_in_Grodno", disabledTimeInGrodno);

        req.setAttribute("disabled_dates_in_Minsk", disabledDatesInMinsk);
        req.setAttribute("disabled_dates_in_Gomel", disabledDatesInGomel);
        req.setAttribute("disabled_dates_in_Mogilev", disabledDatesInMogilev);
        req.setAttribute("disabled_dates_in_Brest", disabledDatesInBrest);
        req.setAttribute("disabled_dates_in_Grodno", disabledDatesInGrodno);

        String requestingUri = req.getParameter("requestingUri");
        boolean isRequestFromClientPage = false;

        if (requestingUri != null && requestingUri.equals("client-page")) {
            isRequestFromClientPage = true;
        }
        req.setAttribute("request_from_client_page", isRequestFromClientPage);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/book_appointment.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Client client = (Client) session.getAttribute("client_data");

        City city = City.valueOf(req.getParameter("city"));
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = req.getParameter("time");

        Appointment appointment = null;
        if (date != null && time != null && !time.isEmpty()) {
           appointment = new Appointment(client, city, date, time);
        } else {
            req.setAttribute("datepicker_and_timepicker_required", "Please select date and time!");
            doGet(req, resp);
        }

        Appointment appointmentInDb = appointmentService.findAppointmentByClientId(client.getId());
        boolean isBookedSuccessfully = false;
        Appointment updatedAppointment = null;

        if (appointmentInDb == null) {
            if (appointment != null) {
                isBookedSuccessfully = appointmentService.addAppointment(appointment);
            }
        } else {
            if (appointment != null) {
                // appointment.setId(appointmentInDb.getId());
                updatedAppointment = appointmentService.updateAppointment(appointment);
            }
        }

        if (isBookedSuccessfully || updatedAppointment != null) {
            resp.sendRedirect("/client-page");
        }
    }
}*/
