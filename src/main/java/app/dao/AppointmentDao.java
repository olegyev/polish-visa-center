package app.dao;

import app.entities.Appointment;
import app.entities.enums.City;
import app.services.ClientService;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class AppointmentDao extends AbstractDao<Appointment> {
    private ClientService clientService = new ClientService();

    private static final String CREATE_APPOINTMENT = "INSERT INTO appointments (client_id, city, appointment_date, " +
            "appointment_time) VALUES (?, ?, ?, ?)";
    private static final String READ_ALL_APPOINTMENTS = "SELECT * FROM appointments ORDER BY city, appointment_date, appointment_time";
    private static final String READ_APPOINTMENT_BY_ID = "SELECT * FROM appointments WHERE id = ?";
    private static final String READ_APPOINTMENT_BY_CLIENT_ID = "SELECT * FROM appointments WHERE client_id = ?";
    private static final String READ_APPOINTMENTS_BY_CITY = "SELECT * FROM appointments WHERE city = ? ORDER BY appointment_date, appointment_time";
    private static final String READ_APPOINTMENTS_BY_CITY_AND_DATE = "SELECT * FROM appointments WHERE city = ? AND appointment_date = ? ORDER BY appointment_time";
    private static final String READ_DATES_BY_CITY = "SELECT appointment_date FROM appointments WHERE city = ?";
    private static final String READ_TIME_BY_CITY_AND_DATES = "SELECT appointment_time FROM appointments " +
            "WHERE city = ? AND appointment_date = ? ORDER BY appointment_time";
    private static final String UPDATE_APPOINTMENT = "UPDATE appointments SET client_id = ?, city = ?, appointment_date = ?, " +
            "appointment_time = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM appointments WHERE id = ?";

    @Override
    public boolean create(Appointment appointment) {
        int rowAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement(CREATE_APPOINTMENT)) {
            int i = 0;
            statement.setLong(++i, appointment.getClient().getId());
            statement.setString(++i, appointment.getCity().getTitle());
            statement.setDate(++i, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            statement.setString(++i, appointment.getAppointmentTime());

            rowAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected == 1;
    }

    @Override
    public List<Appointment> readAll() {
        List<Appointment> appointments = new ArrayList<Appointment>();
        Appointment appointment = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ALL_APPOINTMENTS); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                appointment.setClient(clientService.findClientById(resultSet.getLong("client_id")));
                appointment.setCity(City.findByTitle(resultSet.getString("city")));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setAppointmentTime(resultSet.getString("appointment_time"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    @Override
    public Appointment readById(long id) {
        Appointment appointment = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_APPOINTMENT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                appointment.setClient(clientService.findClientById(resultSet.getLong("client_id")));
                appointment.setCity(City.findByTitle(resultSet.getString("city")));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setAppointmentTime(resultSet.getString("appointment_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    public Appointment readByClientId(long id) {
        Appointment appointment = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_APPOINTMENT_BY_CLIENT_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                appointment.setClient(clientService.findClientById(resultSet.getLong("client_id")));
                appointment.setCity(City.findByTitle(resultSet.getString("city")));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setAppointmentTime(resultSet.getString("appointment_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    public List<Appointment> readAllByCity(City city) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        Appointment appointment = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_APPOINTMENTS_BY_CITY)) {
            statement.setString(1, city.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                appointment.setClient(clientService.findClientById(resultSet.getLong("client_id")));
                appointment.setCity(City.findByTitle(resultSet.getString("city")));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setAppointmentTime(resultSet.getString("appointment_time"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public List<Appointment> readAllByCityAndDate(City city, java.util.Date date) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        Appointment appointment = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_APPOINTMENTS_BY_CITY_AND_DATE)) {
            statement.setString(1, city.getTitle());
            statement.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                appointment.setClient(clientService.findClientById(resultSet.getLong("client_id")));
                appointment.setCity(City.findByTitle(resultSet.getString("city")));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date"));
                appointment.setAppointmentTime(resultSet.getString("appointment_time"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public Set<java.util.Date> readDatesByCity(City city) {
        Set<java.util.Date> dates = new HashSet<java.util.Date>();
        Date date = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_DATES_BY_CITY)) {
            statement.setString(1, city.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                date = resultSet.getDate("appointment_date");
                dates.add(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dates;
    }

    public String[] readTimeByCityAndDate(City city, java.util.Date date) {
        List<String> timeList = new ArrayList<String>();
        String time = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_TIME_BY_CITY_AND_DATES)) {
            statement.setString(1, city.getTitle());
            statement.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                time = resultSet.getString("appointment_time");
                timeList.add(time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timeList.toArray(new String[0]);
    }

    @Override
    public Appointment update(Appointment appointment) {
        Appointment updatedAppointment = null;

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_APPOINTMENT)) {
            int i = 0;
            statement.setLong(++i, appointment.getClient().getId());
            statement.setString(++i, appointment.getCity().getTitle());
            statement.setDate(++i, new java.sql.Date(appointment.getAppointmentDate().getTime()));
            statement.setString(++i, appointment.getAppointmentTime());
            statement.setLong(++i, appointment.getId());

            updatedAppointment = new Appointment();
            updatedAppointment.setId(appointment.getId());
            appointment.setClient(appointment.getClient());
            appointment.setCity(appointment.getCity());
            appointment.setAppointmentDate(appointment.getAppointmentDate());
            appointment.setAppointmentTime(appointment.getAppointmentTime());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedAppointment;
    }

    @Override
    public boolean deleteById(long id) {
        int rowAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            rowAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected == 1;
    }
}