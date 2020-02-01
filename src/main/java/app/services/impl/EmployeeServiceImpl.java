package app.services.impl;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.exceptions.BadRequestException;
import app.exceptions.InternalServerException;
import app.exceptions.NotFoundException;
import app.repos.EmployeeRepo;
import app.security.utils.PasswordEncoder;
import app.services.EmployeeServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeServiceInterface {

    private final static Logger log = LogManager.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeServiceImpl(final EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Employee create(Employee employee) {
        if (!bodyIsOk(employee)) {
            log.error("Attempt to add new employee failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (emailIsInDataBase(employee)) {
            log.error("Attempt to add new employee failed due to the presence of another employee with the same email in the database.");
            throw new BadRequestException("There is another employee in the database with such email.");
        }

        Employee createdEmployee;

        try {
            employee.setPassword(PasswordEncoder.encryptPassword(employee.getPassword()));
            createdEmployee = employeeRepo.save(employee);
        } catch (IllegalArgumentException e) {
            InternalServerException exception = new InternalServerException("Employee is not passed to the server.");
            log.error(exception);
            throw exception;
        }

        return createdEmployee;
    }

    @Override
    public List<Employee> readAll(Specification<Employee> spec) {
        return employeeRepo.findAll(spec);
    }

    @Override
    public Employee readById(long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public Employee update(long id, Employee newEmployee) {
        Employee employeeFromDb;

        if (!bodyIsOk(newEmployee)) {
            log.error("Attempt to update an employee failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        } else {
            employeeFromDb = employeeRepo.findById(id).orElse(null);

            if (employeeFromDb == null) {
                NotFoundException exception = new NotFoundException("Cannot find employee with ID = " + id + ".");
                log.error(exception);
                throw exception;
            } else if (emailIsInDataBase(newEmployee) && !employeeFromDb.getEmail().equals(newEmployee.getEmail())) {
                log.error("Attempt to update an employee with ID = {} failed due to the presence of another"
                        + " employee with the same email in the database (EMAIL = '{}').", id, newEmployee.getEmail());
                throw new BadRequestException("There is another employee in database with such email.");
            } else {
                BeanUtils.copyProperties(newEmployee, employeeFromDb, "id");
            }
        }

        Employee updatedEmployee;

        try {
            employeeFromDb.setPassword(PasswordEncoder.encryptPassword(newEmployee.getPassword()));
            updatedEmployee = employeeRepo.save(employeeFromDb);
        } catch (IllegalArgumentException e) {
            InternalServerException exception = new InternalServerException("Employee is not passed to the server.");
            log.error(exception);
            throw exception;
        }

        return updatedEmployee;
    }

    @Override
    public void delete(long id) {
        Employee employee = readById(id);

        if (employee == null) {
            NotFoundException exception = new NotFoundException("Cannot find employee with ID = " + id + ".");
            log.error(exception);
            throw exception;
        } else {
            employeeRepo.deleteById(id);
        }
    }

    @Override
    public Employee readByEmail(String email) {
        return employeeRepo.findByEmail(email);
    }

    @Override
    public List<Employee> readByLastName(String lastName) {
        return employeeRepo.findByLastName(lastName.toUpperCase());
    }

    @Override
    public List<Employee> readByCity(City city) {
        return employeeRepo.findByCity(city);
    }

    @Override
    public List<Employee> readByPosition(EmployeePosition position) {
        return employeeRepo.findByPosition(position);
    }

    @Override
    public List<Employee> readByPositionAndCity(EmployeePosition position, City city) {
        return employeeRepo.readByPositionAndCity(position, city);
    }

    @Override
    public long countAdminsByCityAndPosition(City city, EmployeePosition position) {
        return employeeRepo.countAdminsByCityAndPosition(city, position);
    }

    private boolean bodyIsOk(Employee body) {
        return body.getFirstName() != null
                && body.getLastName() != null
                && body.getEmail() != null
                && body.getPhoneNumber() != null
                && body.getPassword() != null
                && body.getPosition() != null
                && body.getCity() != null;
    }

    private boolean emailIsInDataBase(Employee employeeToCreate) {
        String email = employeeToCreate.getEmail();
        Employee employeeInDataBase = readByEmail(email);
        return employeeInDataBase != null;
    }

}