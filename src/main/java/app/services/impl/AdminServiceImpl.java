package app.services.impl;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import app.exceptions.NotFoundException;
import app.repos.AdminRepo;
import app.services.AdminServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminServiceInterface {

    private final AdminRepo adminRepo;

    @Autowired
    public AdminServiceImpl(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public Admin create(Admin admin) {
        return adminRepo.save(admin);
    }

    @Override
    public List<Admin> readAll() {
        return adminRepo.findAll();
    }

    @Override
    public Admin readById(long id) {
        return adminRepo.getOne(id);
    }

    @Override
    public Admin update(Admin adminFromDb, Admin newAdmin) {
        Admin updatedAdmin;

        try {
            BeanUtils.copyProperties(newAdmin, adminFromDb, "id");
            updatedAdmin = adminRepo.save(adminFromDb);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Admin is not found");
        }

        return updatedAdmin;
    }

    @Override
    public void delete(long id) {
        adminRepo.deleteById(id);
    }

    @Override
    public Admin authenticate(String email, String password) {
        return adminRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public Admin readByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    @Override
    public List<Admin> readByLastName(String lastName) {
        return adminRepo.findByLastName(lastName.toUpperCase());
    }

    @Override
    public List<Admin> readByCity(City city) {
        return adminRepo.findByCity(city);
    }

    @Override
    public List<Admin> readByPositionAndCity(AdminPosition position, City city) {
        return adminRepo.readByPositionAndCity(position, city);
    }

    @Override
    public long countAdminsByCityAndPosition(City city, AdminPosition position) {
        return adminRepo.countAdminsByCityAndPosition(city, position);
    }

}