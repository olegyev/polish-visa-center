package app.dao;

import app.entities.Client;
import app.entities.enums.ClientOccupation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends UserDao<Client> {
    private static final String CREATE_CLIENT = "INSERT INTO clients (first_name, last_name, date_of_birth, " +
            "occupation, email, phone_number, password, personal_data_process_agreement) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String READ_ALL_CLIENTS = "SELECT * FROM clients";
    private static final String READ_CLIENT_BY_ID = "SELECT * FROM clients WHERE id = ?";
    private static final String READ_CLIENT_BY_EMAIL = "SELECT * FROM clients WHERE email = ?";
    private static final String READ_CLIENT_BY_LAST_NAME = "SELECT * FROM clients WHERE last_name = ?";
    private static final String GET_ID_BY_EMAIL = "SELECT id FROM clients WHERE email = ?";
    private static final String UPDATE_CLIENT = "UPDATE clients SET first_name = ?, last_name = ?, date_of_birth = ?, " +
            "occupation = ?, email = ?, phone_number = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM clients WHERE id = ?";
    private static final String AUTHENTICATE = "SELECT * FROM clients WHERE email = ? AND password = ?";

    @Override
    public boolean create(Client client) {
        int rowAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement(CREATE_CLIENT)) {
            int i = 0;
            statement.setString(++i, client.getFirstName());
            statement.setString(++i, client.getLastName());
            statement.setDate(++i, new java.sql.Date(client.getDateOfBirth().getTime()));
            statement.setString(++i, client.getOccupation().getTitle());
            statement.setString(++i, client.getEmail());
            statement.setString(++i, client.getPhoneNumber());
            statement.setString(++i, client.getPassword());
            statement.setBoolean(++i, client.getPersonalDataProcAgreement());

            rowAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected == 1;
    }

    @Override
    public List<Client> readAll() {
        List<Client> clients = new ArrayList<Client>();
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ALL_CLIENTS); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setDateOfBirth(resultSet.getDate("date_of_birth"));
                client.setOccupation(ClientOccupation.findByTitle(resultSet.getString("occupation")));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setPassword(resultSet.getString("password"));
                client.setPersonalDataProcAgreement(resultSet.getBoolean("personal_data_process_agreement"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public Client readById(long id) {
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_CLIENT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                client = new Client();
                client.setId(id);
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setDateOfBirth(resultSet.getDate("date_of_birth"));
                client.setOccupation(ClientOccupation.findByTitle(resultSet.getString("occupation")));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setPassword(resultSet.getString("password"));
                client.setPersonalDataProcAgreement(resultSet.getBoolean("personal_data_process_agreement"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }

    @Override
    public Client readByEmail(String email) {
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_CLIENT_BY_EMAIL)) {
            statement.setString(1, email.trim());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setDateOfBirth(resultSet.getDate("date_of_birth"));
                client.setOccupation(ClientOccupation.findByTitle(resultSet.getString("occupation")));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setPassword(resultSet.getString("password"));
                client.setPersonalDataProcAgreement(resultSet.getBoolean("personal_data_process_agreement"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }

    @Override
    public List<Client> readByLastName(String lastName) {
        List<Client> clients = new ArrayList<Client>();
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_CLIENT_BY_LAST_NAME)) {
            statement.setString(1, lastName.trim().toUpperCase());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setDateOfBirth(resultSet.getDate("date_of_birth"));
                client.setOccupation(ClientOccupation.findByTitle(resultSet.getString("occupation")));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setPassword(resultSet.getString("password"));
                client.setPersonalDataProcAgreement(resultSet.getBoolean("personal_data_process_agreement"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public long getIdByEmail(String email) {
        long result = 0;

        try (PreparedStatement statement = connection.prepareStatement(GET_ID_BY_EMAIL)) {
            statement.setString(1, email.trim());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Client update(Client client) {
        Client updatedClient = null;

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CLIENT)) {
            int i = 0;
            statement.setString(++i, client.getFirstName());
            statement.setString(++i, client.getLastName());
            statement.setDate(++i, new java.sql.Date(client.getDateOfBirth().getTime()));
            statement.setString(++i, client.getOccupation().getTitle());
            statement.setString(++i, client.getEmail());
            statement.setString(++i, client.getPhoneNumber());
            statement.setLong(++i, client.getId());

            updatedClient = new Client();
            updatedClient.setId(client.getId());
            updatedClient.setFirstName(client.getFirstName());
            updatedClient.setLastName(client.getLastName());
            updatedClient.setDateOfBirth(client.getDateOfBirth());
            updatedClient.setOccupation(client.getOccupation());
            updatedClient.setEmail(client.getEmail());
            updatedClient.setPhoneNumber(client.getPhoneNumber());
            updatedClient.setPassword(client.getPassword());
            updatedClient.setPersonalDataProcAgreement(client.getPersonalDataProcAgreement());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedClient;
    }

    @Override
    public boolean deleteById(long id) {
        int rowAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);) {
            statement.setLong(1, id);
            rowAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected == 1;
    }

    @Override
    public Client authenticate(String email, String password) {
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE)) {
            statement.setString(1, email.trim());
            statement.setString(2, password.trim());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setDateOfBirth(resultSet.getDate("date_of_birth"));
                client.setOccupation(ClientOccupation.findByTitle(resultSet.getString("occupation")));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setPassword(resultSet.getString("password"));
                client.setPersonalDataProcAgreement(resultSet.getBoolean("personal_data_process_agreement"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }
}