package app.services;

import app.dao.AppointmentDaoImpl;
import app.entities.Appointment;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppointmentService {
    private AppointmentDaoImpl dao = new AppointmentDaoImpl();

    public long addAppointment(Appointment appointment) {
        long appointmentId = 0;
        if (appointment != null) {
            appointmentId = dao.create(appointment);
        }
        return appointmentId;
    }

    public List<Appointment> findAllAppointments() {
        List<Appointment> appointments = dao.readAll();
        return appointments;
    }

    public Appointment findAppointmentById(long id) {
        Appointment appointment = dao.readById(id);
        return appointment;
    }

    public Appointment updateAppointment(Appointment appointment) {
        Appointment updatedAppointment = dao.update(appointment);
        return updatedAppointment;
    }

    public void deleteAppointment(Appointment appointment) {
        dao.delete(appointment);
    }

    public List<Appointment> findAppointmentsByCity(City city) {
        List<Appointment> appointments = dao.readAllByCity(city);
        return appointments;
    }

    public List<Appointment> findAppointmentsByCityAndDate(City city, Date date) {
        List<Appointment> appointments = dao.readAllByCityAndDate(city, date);
        return appointments;
    }

    public Set<Date> findDatesByCity(City city) {
        Set<Date> dates = dao.readDatesByCity(city);
        return dates;
    }

    public String[] findTimeByCityAndDate(City city, Date date) {
        String[] time = dao.readTimeByCityAndDate(city, date);
        return time;
    }

    public Map<String, String[]> findTimeToDisable(City city) {
        AdminService adminService = new AdminService();
        Set<Date> datesInDb = findDatesByCity(city);
        List<String> timeToDisable = new ArrayList<>();
        Map<String, String[]> disabledTimeByCityAndDate = new HashMap<>();

        if (datesInDb != null) {
            for (Date dateInDb : datesInDb) {
                String[] timeInDb = findTimeByCityAndDate(city, dateInDb);

                if (adminService.countAdminsByCityAndPosition(city, AdminPosition.OPERATOR) == 1) {
                    timeToDisable.addAll(Arrays.asList(timeInDb));
                } else {
                    int timeCounter = 1;
                    for (int i = 0; (i + 1) < timeInDb.length; i++) {
                        if (timeInDb[i].equals(timeInDb[i + 1])) {
                            timeCounter++;
                        }

                        if (timeCounter == adminService.countAdminsByCityAndPosition(city, AdminPosition.OPERATOR)) {
                            timeToDisable.add(timeInDb[i]);
                            timeCounter = 1;
                        }
                    }
                }

                disabledTimeByCityAndDate.put(dateInDb.toString(), timeToDisable.toArray(new String[0]));
                timeToDisable.clear();
            }
        }

        return disabledTimeByCityAndDate;
    }

    public String[] findDatesToDisable(Map<String, String[]> disabledTime, City city,
                                       int workDayBegin, int workDayEnd, int stepInMinutes) {
        AdminService adminService = new AdminService();
        long operatorsNum = adminService.countAdminsByCityAndPosition(city, AdminPosition.OPERATOR);
        long maxAppointmentsNum = (workDayEnd - workDayBegin) * (60 / stepInMinutes) * operatorsNum;

        List<String> disabledDates = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : disabledTime.entrySet()) {
            String date = entry.getKey();
            String[] time = entry.getValue();

            if (time.length == maxAppointmentsNum) {
                disabledDates.add(date);
            }
        }

        return disabledDates.toArray(new String[0]);
    }

    public boolean isAppointmentInPast(Appointment appointment) {
        boolean isAppointmentInPast = false;

        Date today = new Date();
        String concatAppointmentDateAndTime = appointment.getAppointmentDate() + " " + appointment.getAppointmentTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date appointmentDateAndTime = null;
        try {
            appointmentDateAndTime = format.parse(concatAppointmentDateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (appointmentDateAndTime != null) {
            long todayInMillis = today.getTime();
            long appointmentInMillis = appointmentDateAndTime.getTime();
            long difference = todayInMillis - appointmentInMillis;

            if (difference > 0) {
                isAppointmentInPast = true;
            }
        }

        return isAppointmentInPast;
    }
}