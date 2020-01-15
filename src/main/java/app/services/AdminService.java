package app.services;

import app.dao.AdminDaoImpl;
import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.util.List;

public class AdminService {
    private AdminDaoImpl dao = new AdminDaoImpl();

    public long addAdmin(Admin admin) {
        long adminId = 0;
        if (admin != null && !isInDataBase(admin)) {
            adminId = dao.create(admin);
        }
        return adminId;
    }

    public List<Admin> findAllAdmins() {
        List<Admin> admins = dao.readAll();
        return admins;
    }

    public Admin findAdminById(long id) {
        Admin admin = dao.readById(id);
        return admin;
    }

    public Admin updateAdmin(Admin admin) {
        Admin updatedAdmin = dao.update(admin);
        return updatedAdmin;
    }

    public void deleteAdmin(Admin admin) {
        dao.delete(admin);
    }

    public Admin login(String email, String password) {
        Admin admin = dao.authenticate(email, password);
        return admin;
    }

    public Admin findAdminByEmail(String email) {
        Admin admin = dao.readByEmail(email);
        return admin;
    }

    public List<Admin> findAdminByLastName(String lastName) {
        List<Admin> admins = dao.readByLastName(lastName);
        return admins;
    }

    public long findIdByEmail(String email) {
        long id = dao.getIdByEmail(email);
        return id;
    }

    public List<Admin> findAdminsByCity(City city) {
        List<Admin> admins = dao.readByCity(city);
        return admins;
    }

    public List<Admin> findAdminsByPositionAndCity(AdminPosition position, City city) {
        List<Admin> admins = dao.readByPositionAndCity(position, city);
        return admins;
    }

    public long countAdminsByCityAndPosition(City city, AdminPosition position) {
        long result = dao.countAdminsByCityAndPosition(city, position);
        return result;
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