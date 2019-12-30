package app.dao;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDao extends UserDao<Admin> {
    private static final String CREATE_ADMIN = "INSERT INTO admins (first_name, last_name, position, city, " +
            "email, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String READ_ALL_ADMINS = "SELECT * FROM admins ORDER BY city, position, id";
    private static final String READ_ADMIN_BY_ID = "SELECT * FROM admins WHERE id = ?";
    private static final String READ_ADMIN_BY_EMAIL = "SELECT * FROM admins WHERE email = ?";
    private static final String READ_ADMIN_BY_LAST_NAME = "SELECT * FROM admins WHERE last_name = ?";
    private static final String READ_ADMINS_BY_CITY = "SELECT * FROM admins WHERE city = ? ORDER BY position, id";
    private static final String READ_ADMINS_BY_POSITION_AND_CITY = "SELECT * FROM admins WHERE position = ? AND city = ? ORDER BY id";
    private static final String GET_ID_BY_EMAIL = "SELECT id FROM admins WHERE email = ?";
    private static final String COUNT_ADMINS_BY_CITY_AND_POSITION = "SELECT COUNT(position) AS num FROM admins WHERE city = ? AND position = ?";
    private static final String UPDATE_ADMIN = "UPDATE admins SET first_name = ?, last_name = ?, position = ?, city = ?, " +
            "email = ?, phone_number = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM admins WHERE id = ?";
    private static final String AUTHENTICATE = "SELECT * FROM admins WHERE email = ? AND password = ?";

    @Override
    public boolean create(Admin admin) {
        int rowAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement(CREATE_ADMIN)) {
            int i = 0;
            statement.setString(++i, admin.getFirstName());
            statement.setString(++i, admin.getLastName());
            statement.setString(++i, admin.getPosition().getTitle());
            statement.setString(++i, admin.getCity().getTitle());
            statement.setString(++i, admin.getEmail());
            statement.setString(++i, admin.getPhoneNumber());
            statement.setString(++i, admin.getPassword());

            rowAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected == 1;
    }

    @Override
    public List<Admin> readAll() {
        List<Admin> admins = new ArrayList<Admin>();
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ALL_ADMINS); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    @Override
    public Admin readById(long id) {
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ADMIN_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(id);
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }

    @Override
    public Admin readByEmail(String email) {
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ADMIN_BY_EMAIL)) {
            statement.setString(1, email.trim());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }

    @Override
    public List<Admin> readByLastName(String lastName) {
        List<Admin> admins = new ArrayList<Admin>();
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ADMIN_BY_LAST_NAME)) {
            statement.setString(1, lastName.trim().toUpperCase());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    public List<Admin> readByCity(City city) {
        List<Admin> admins = new ArrayList<Admin>();
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ADMINS_BY_CITY)) {
            statement.setString(1, city.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    public List<Admin> readByPositionAndCity(AdminPosition position, City city) {
        List<Admin> admins = new ArrayList<Admin>();
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_ADMINS_BY_POSITION_AND_CITY)) {
            statement.setString(1, position.getTitle());
            statement.setString(2, city.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
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

    public int countAdminsByCityAndPosition(City city, AdminPosition position) {
        int result = 0;

        try (PreparedStatement statement = connection.prepareStatement(COUNT_ADMINS_BY_CITY_AND_POSITION)) {
            statement.setString(1, city.getTitle());
            statement.setString(2, position.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getInt("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Admin update(Admin admin) {
        Admin updatedAdmin = null;

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ADMIN)) {
            int i = 0;
            statement.setString(++i, admin.getFirstName());
            statement.setString(++i, admin.getLastName());
            statement.setString(++i, admin.getPosition().getTitle());
            statement.setString(++i, admin.getCity().getTitle());
            statement.setString(++i, admin.getEmail());
            statement.setString(++i, admin.getPhoneNumber());
            statement.setLong(++i, admin.getId());

            updatedAdmin = new Admin();
            updatedAdmin.setId(admin.getId());
            updatedAdmin.setFirstName(admin.getFirstName());
            updatedAdmin.setLastName(admin.getLastName());
            updatedAdmin.setPosition(admin.getPosition());
            updatedAdmin.setCity(admin.getCity());
            updatedAdmin.setEmail(admin.getEmail());
            updatedAdmin.setPhoneNumber(admin.getPhoneNumber());
            updatedAdmin.setPassword(admin.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedAdmin;
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

    @Override
    public Admin authenticate(String email, String password) {
        Admin admin = null;

        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE)) {
            statement.setString(1, email.trim());
            statement.setString(2, password.trim());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setFirstName(resultSet.getString("first_name"));
                admin.setLastName(resultSet.getString("last_name"));
                admin.setPosition(AdminPosition.findByTitle(resultSet.getString("position")));
                admin.setCity(City.findByTitle(resultSet.getString("city")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
}