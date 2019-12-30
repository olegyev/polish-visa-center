package app.services;

import app.dao.AppointmentDao;
import app.dao.ClientDao;
import app.dao.EntityTransaction;
import app.entities.Appointment;
import app.entities.Client;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppointmentService {
    private AdminService adminService = new AdminService();
    private AppointmentDao dao = new AppointmentDao();
    private EntityTransaction transaction = new EntityTransaction();

    public boolean addAppointment(Appointment appointment) {
        boolean isAdded = false;
        transaction.begin(dao);

        try {
            if (appointment != null) {
                isAdded = dao.create(appointment);
            }
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return isAdded;
    }

    public List<Appointment> findAllAppointments() {
        List<Appointment> appointments = new ArrayList<Appointment>();
        transaction.begin(dao);

        try {
            appointments = dao.readAll();
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointments;
    }

    public Appointment findAppointmentById(long id) {
        Appointment appointment = null;
        transaction.begin(dao);

        try {
            appointment = dao.readById(id);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointment;
    }

    public Appointment findAppointmentByClientId(long clientId) {
        Appointment appointment = null;
        transaction.begin(dao);

        try {
            appointment = dao.readByClientId(clientId);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointment;
    }

    public Appointment findAppointmentByClientEmail(String email) {
        ClientDao clientDao = new ClientDao();
        Appointment appointment = null;
        transaction.begin(dao, clientDao);

        try {
            Client client = clientDao.readByEmail(email);
            appointment = dao.readByClientId(client.getId());
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointment;
    }

    public List<Appointment> findAppointmentsByClientLastName(String lastName) {
        ClientDao clientDao = new ClientDao();
        Appointment appointment = null;
        List<Appointment> appointments = new ArrayList<Appointment>();
        transaction.begin(dao, clientDao);

        try {
            List<Client> clients = clientDao.readByLastName(lastName);
            for (Client client : clients) {
                appointment = dao.readByClientId(client.getId());
                appointments.add(appointment);
            }
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointments;
    }

    public List<Appointment> findAppointmentsByCity(City city) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        transaction.begin(dao);

        try {
            appointments = dao.readAllByCity(city);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointments;
    }

    public List<Appointment> findAppointmentsByCityAndDate(City city, Date date) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        transaction.begin(dao);

        try {
            appointments = dao.readAllByCityAndDate(city, date);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return appointments;
    }

    public Set<Date> findDatesByCity(City city) {
        Set<Date> dates = null;
        transaction.begin(dao);

        try {
            dates = dao.readDatesByCity(city);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return dates;
    }

    public String[] findTimeByCityAndDate(City city, Date date) {
        String[] timeList = null;
        transaction.begin(dao);

        try {
            timeList = dao.readTimeByCityAndDate(city, date);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return timeList;
    }

    public boolean deleteAppointment(long id) {
        boolean isDeleted = false;
        transaction.begin(dao);

        try {
            isDeleted = dao.deleteById(id);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return isDeleted;
    }

    public Appointment updateAppointment(Appointment appointment) {
        Appointment updatedAppointment = null;
        transaction.begin(dao);

        try {
            if (appointment != null) {
                updatedAppointment = dao.update(appointment);
            }
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return updatedAppointment;
    }

    public Map<String, String[]> findTimeToDisable(City city) {
        Set<Date> datesInDb = findDatesByCity(city);
        List<String> timeToDisable = new ArrayList<String>();
        Map<String, String[]> disabledTimeByCityAndDate = new HashMap<String, String[]>();

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
        int operatorsNum = adminService.countAdminsByCityAndPosition(city, AdminPosition.OPERATOR);
        int maxAppointmentsNum = (workDayEnd - workDayBegin) * (60 / stepInMinutes) * operatorsNum;

        List<String> disabledDates = new ArrayList<String>();

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