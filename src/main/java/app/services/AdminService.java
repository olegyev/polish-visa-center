package app.services;

import app.dao.AdminDao;
import app.dao.EntityTransaction;
import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private AdminDao dao = new AdminDao();
    private EntityTransaction transaction = new EntityTransaction();

    public boolean addAdmin(Admin admin) {
        boolean isAdded = false;
        transaction.begin(dao);

        try {
            if (admin != null && !isInDataBase(admin)) {
                isAdded = dao.create(admin);
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

    public Admin login(String email, String password) {
        Admin admin = null;
        transaction.begin(dao);

        try {
            admin = dao.authenticate(email, password);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admin;
    }

    public List<Admin> findAllAdmins() {
        List<Admin> admins = new ArrayList<Admin>();
        transaction.begin(dao);

        try {
            admins = dao.readAll();
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admins;
    }

    public Admin findAdminById(long id) {
        Admin admin = null;
        transaction.begin(dao);

        try {
            admin = dao.readById(id);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admin;
    }

    public Admin findAdminByEmail(String email) {
        Admin admin = null;
        transaction.begin(dao);

        try {
            admin = dao.readByEmail(email);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admin;
    }

    public List<Admin> findAdminByLastName(String lastName) {
        List<Admin> admins = new ArrayList<Admin>();
        transaction.begin(dao);

        try {
            admins = dao.readByLastName(lastName);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admins;
    }

    public List<Admin> findAdminsByCity(City city) {
        List<Admin> admins = new ArrayList<Admin>();
        transaction.begin(dao);

        try {
            admins = dao.readByCity(city);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admins;
    }

    public List<Admin> findAdminsByPositionAndCity(AdminPosition position, City city) {
        List<Admin> admins = new ArrayList<Admin>();
        transaction.begin(dao);

        try {
            admins = dao.readByPositionAndCity(position, city);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return admins;
    }

    public int countAdminsByCityAndPosition(City city, AdminPosition position) {
        int result = 0;
        transaction.begin(dao);

        try {
            result = dao.countAdminsByCityAndPosition(city, position);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return result;
    }

    public Admin updateAdmin(Admin admin) {
        Admin updatedAdmin = null;
        transaction.begin(dao);

        try {
            if (admin != null && !isInDataBase(admin)) {
                updatedAdmin = dao.update(admin);
            }
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return updatedAdmin;
    }

    public boolean deleteAdmin(long id) {
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

    private boolean isInDataBase(Admin admin) {
        boolean isInDataBase = false;
        Admin adminInDataBase = dao.readByEmail(admin.getEmail());

        if (adminInDataBase != null && adminInDataBase.getId() != admin.getId()) {
            isInDataBase = true;
        }

        return isInDataBase;
    }
}