package app.services.impl;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import app.exceptions.BadRequestException;
import app.exceptions.InternalServerException;
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
    public Admin create(Admin employee) {
        if (!bodyIsOk(employee)) {
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (emailIsInDataBase(employee)) {
            throw new BadRequestException("There is another employee in database with such email.");
        }

        Admin createdEmployee;

        try {
            createdEmployee = adminRepo.save(employee);
        } catch (IllegalArgumentException e) {
            throw new InternalServerException("The admin is not added to the database due to the server's internal reasons.");
        }

        return createdEmployee;
    }

    @Override
    public List<Admin> readAll() {
        return adminRepo.findAll();
    }

    @Override
    public Admin readById(long id) {
        return adminRepo.findById(id).orElse(null);
    }

    @Override
    public Admin update(long id, Admin newAdmin) {
        Admin adminFromDb;

        if (!bodyIsOk(newAdmin)) {
            throw new BadRequestException("The form filled incorrectly.");
        } else {
            adminFromDb = adminRepo.findById(id).orElse(null);

            if (adminFromDb == null) {
                throw new NotFoundException("Cannot find employee with ID = '" + id + "'.");
            } else if (emailIsInDataBase(newAdmin) && !adminFromDb.getEmail().equals(newAdmin.getEmail())) {
                throw new BadRequestException("There is another employee in database with such email.");
            } else {
                BeanUtils.copyProperties(newAdmin, adminFromDb, "id");
            }
        }

        Admin updatedAdmin;

        try {
            updatedAdmin = adminRepo.save(adminFromDb);
        } catch (IllegalArgumentException e) {
            throw new InternalServerException("The admin is not modified due to the server's internal reasons.");
        }

        return updatedAdmin;
    }

    @Override
    public void delete(long id) {
        Admin admin = readById(id);

        if (admin == null) {
            throw new NotFoundException("Cannot find employee with ID = '" + id + "'.");
        } else {
            adminRepo.deleteById(id);
        }
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
    public List<Admin> readByPosition(AdminPosition position) {
        return adminRepo.findByPosition(position);
    }

    @Override
    public List<Admin> readByPositionAndCity(AdminPosition position, City city) {
        return adminRepo.readByPositionAndCity(position, city);
    }

    @Override
    public long countAdminsByCityAndPosition(City city, AdminPosition position) {
        return adminRepo.countAdminsByCityAndPosition(city, position);
    }

    private boolean bodyIsOk(Admin body) {
        return body.getFirstName() != null
                && body.getLastName() != null
                && body.getEmail() != null
                && body.getPhoneNumber() != null
                && body.getPassword() != null
                && body.getPosition() != null
                && body.getCity() != null;
    }

    private boolean emailIsInDataBase(Admin employeeToCreate) {
        String email = employeeToCreate.getEmail();
        Admin employeeInDataBase = readByEmail(email);
        return employeeInDataBase != null;
    }

}