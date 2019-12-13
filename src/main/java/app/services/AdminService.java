package app.services;

import app.dao.AdminDao;
import app.dao.EntityTransaction;
import app.entities.Admin;

import java.util.List;

public class AdminService {
    public boolean addAdmin(Admin admin) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        boolean isAdded = false;
        if (admin != null && !isInDataBase(admin, dao)) {
            isAdded = dao.create(admin);
        }

        transaction.commit();
        transaction.end();

        return isAdded;
    }

    public Admin login(String email, String password) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        Admin admin = dao.authenticate(email, password);

        transaction.commit();
        transaction.end();

        return admin;
    }

    public List<Admin> findAllAdmins() {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        List<Admin> admins = dao.readAll();

        transaction.commit();
        transaction.end();

        return admins;
    }

    public Admin findAdminById(long id) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        Admin admin = dao.readById(id);

        transaction.commit();
        transaction.end();

        return admin;
    }

    public Admin findAdminByEmail(String email) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        Admin admin = dao.readByEmail(email);

        transaction.commit();
        transaction.end();

        return admin;
    }

    public List<Admin> findAdminByLastName(String lastName) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        List<Admin> admin = dao.readByLastName(lastName);

        transaction.commit();
        transaction.end();

        return admin;
    }

    public boolean deleteAdmin(long id) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        boolean isDeleted = dao.deleteById(id);

        transaction.commit();
        transaction.end();

        return isDeleted;
    }

    public Admin updateAdmin(Admin admin) {
        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        Admin updatedAdmin = null;

        if (admin != null && !isInDataBase(admin, dao)) {
            updatedAdmin = dao.update(admin);
        }

        transaction.commit();
        transaction.end();

        return updatedAdmin;
    }

    private boolean isInDataBase(Admin admin, AdminDao dao) {
        boolean isInDataBase = false;
        Admin adminInDataBase = dao.readByEmail(admin.getEmail());

        if (adminInDataBase != null && adminInDataBase.getId() != admin.getId()) {
            isInDataBase = true;
        }

        return isInDataBase;
    }
}