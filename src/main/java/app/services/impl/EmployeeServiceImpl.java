package app.services.impl;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.EmployeeRepo;
import app.repos.specs.EmployeeJpaSpecification;
import app.security.utils.PasswordEncoder;
import app.services.EmployeeServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Employee create(Employee employee, UserDetails userDetails) {
        Employee loggedEmployee = readByEmail(userDetails.getUsername());

        if (!bodyIsOk(employee)) {
            log.error("Attempt to add new employee failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (emailIsInDataBase(employee)) {
            log.error("Attempt to add new employee failed due to the presence of another employee with the same email in the database.");
            throw new BadRequestException("There is another employee in the database with such email.");
        }

        if (isManager(loggedEmployee) && !isOperatorFromManagersCity(employee, loggedEmployee)) {
            log.error("Manager with ID = {} from {} tried to add not operator from his/her city.",
                    loggedEmployee.getId(), loggedEmployee.getCity().toString());
            throw new BadRequestException("You can add only operators in " + loggedEmployee.getCity().toString() + ".");
        }

        employee.setPassword(PasswordEncoder.encryptPassword(employee.getPassword()));
        Employee createdEmployee = create(employee);

        log.info("New employee added to a database with ID = {} and username = '{}' by employee with ID = {}.",
                createdEmployee.getId(), createdEmployee.getEmail(), loggedEmployee.getId());

        return createdEmployee;
    }

    @Override
    public Employee create(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public Page<Employee> readAll(UserDetails userDetails, City city, EmployeePosition position, String lastName, Pageable pageable) {
        Employee loggedEmployee = readByEmail(userDetails.getUsername());

        if (city == null && position == null && lastName == null) {
            List<Employee> employees = !isManager(loggedEmployee)
                    ? readAll(null, pageable).getContent()
                    : readByPositionAndCity(EmployeePosition.OPERATOR, loggedEmployee.getCity());

            if (employees.isEmpty()) {
                NotFoundException exception = new NotFoundException("Cannot find employees.");
                log.error(exception);
                throw exception;
            } else {
                log.info("Found employees by no search criteria by employee with ID = {}.", loggedEmployee.getId());
            }

            return readAll(null, pageable);

        } else {
            Page<Employee> employees;
            Specification<Employee> spec;

            if (!isManager(loggedEmployee)) {
                spec = Specification
                        .where(city == null ? null : EmployeeJpaSpecification.cityContains(city))
                        .and(position == null ? null : EmployeeJpaSpecification.positionContains(position))
                        .and(lastName == null ? null : EmployeeJpaSpecification.lastNameContains(lastName.toUpperCase()));

                employees = readAll(spec, pageable);

                if (employees.isEmpty()) {
                    NotFoundException exception = new NotFoundException("Cannot find employees by search criteria.");
                    log.error(exception);
                    throw exception;
                } else {
                    log.info("Found employees by search criteria by employee with ID = {}.", loggedEmployee.getId());
                }

            } else {
                if (city != null || position != null) {
                    log.error("Manager with ID = {} from {} tried to search not operator from his/her city.",
                            loggedEmployee.getId(), loggedEmployee.getCity().toString());
                    throw new BadRequestException("You can search only operators in "
                            + loggedEmployee.getCity().toString()
                            + " by last name.");
                } else {
                    spec = Specification.where(EmployeeJpaSpecification.lastNameContains(lastName.toUpperCase()));
                    employees = readAll(spec, pageable);

                    if (employees.isEmpty()) {
                        NotFoundException exception = new NotFoundException("Cannot find operators in "
                                + loggedEmployee.getCity().toString()
                                + " with last name '" + lastName.toUpperCase() + "'.");
                        log.error(exception);
                        throw exception;
                    } else {
                        log.info("Found operators in {} with last name '{}' by employee with ID = {}.",
                                loggedEmployee.getCity().toString(), lastName.toUpperCase(), loggedEmployee.getId());
                    }
                }
            }

            return readAll(spec, pageable);
        }
    }

    @Override
    public Page<Employee> readAll(Specification<Employee> spec, Pageable pageable) {
        return employeeRepo.findAll(spec, pageable);
    }

    @Override
    public Employee readById(long id, UserDetails userDetails) {
        Employee loggedEmployee = readByEmail(userDetails.getUsername());
        Employee employee = readById(id);

        if (employee == null && !isManager(loggedEmployee)) {
            NotFoundException exception = new NotFoundException("Cannot find employee with ID = " + id + ".");
            log.error(exception);
            throw exception;
        } else if (employee == null || (isManager(loggedEmployee) && !isOperatorFromManagersCity(employee, loggedEmployee))) {
            NotFoundException exception = new NotFoundException("Cannot find operator in "
                    + loggedEmployee.getCity().toString()
                    + " with ID = " + id + ".");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found employee with ID = {} by employee with ID = {}.", id, loggedEmployee.getId());
        }

        return employee;
    }

    @Override
    public Employee readById(long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public Employee update(long id, Employee newEmployee, UserDetails userDetails) {
        Employee loggedEmployee = readByEmail(userDetails.getUsername());

        if (id == loggedEmployee.getId() && !loggedEmployee.getPosition().equals(newEmployee.getPosition())) {
            log.error("Employee with ID = {} tried to change his/her position from '{}' to '{}'.",
                    loggedEmployee.getId(), loggedEmployee.getPosition().toString(), newEmployee.getPosition().toString());
            throw new BadRequestException("The logged employee cannot change his/her position.");
        } else if (isManager(loggedEmployee)) {
            Employee employeeToUpdate = readById(id);

            if (employeeToUpdate == null || !isOperatorFromManagersCity(employeeToUpdate, loggedEmployee)) {
                NotFoundException exception = new NotFoundException("Cannot find operator in "
                        + loggedEmployee.getCity().toString()
                        + " with ID = " + id + ".");
                log.error(exception);
                throw exception;
            } else if (!isOperatorFromManagersCity(newEmployee, loggedEmployee)) {
                log.error("Manager with ID = {} from {} tried to change operator's (ID = {})"
                                + " position from OPERATOR to {} and/or city to {}.",
                        loggedEmployee.getId(), loggedEmployee.getCity(),
                        id, newEmployee.getPosition().toString(), newEmployee.getCity().toString());
                throw new BadRequestException("A manager cannot change operator's position or city.");
            }
        }

        Employee updatedEmployee;
        if (!bodyIsOk(newEmployee)) {
            log.error("Attempt to update an employee with ID = {} failed due to the incorrect form filling.", id);
            throw new BadRequestException("The form filled incorrectly.");
        } else {
            updatedEmployee = update(id, newEmployee);
        }

        log.info("Updated employee with ID = {} by employee with ID = {}.", id, loggedEmployee.getId());

        return updatedEmployee;
    }

    @Override
    public Employee update(long id, Employee newEmployee) {
        Employee employeeFromDb = readById(id);

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

        employeeFromDb.setPassword(PasswordEncoder.encryptPassword(newEmployee.getPassword()));
        return create(employeeFromDb);
    }

    @Override
    public void delete(long id, UserDetails userDetails) {
        Employee loggedEmployee = readByEmail(userDetails.getUsername());
        Employee employee = readById(id);

        if (id == loggedEmployee.getId()) {
            log.error("The logged employee with ID = {} tried to delete him-/herself.", loggedEmployee.getId());
            throw new BadRequestException("The logged employee cannot delete him-/herself.");
        } else if (isManager(loggedEmployee)) {
            Employee employeeToDelete = readById(id);

            if (employeeToDelete == null || !isOperatorFromManagersCity(employeeToDelete, loggedEmployee)) {
                NotFoundException exception = new NotFoundException("Cannot find operator in "
                        + loggedEmployee.getCity().toString() + " with ID = " + id + ".");
                log.error(exception);
                throw exception;
            }
        }

        if (employee == null) {
            NotFoundException exception = new NotFoundException("Cannot find employee with ID = " + id + ".");
            log.error(exception);
            throw exception;
        }

        log.info("Deleted employee with ID = {} by employee with ID = {}.", id, loggedEmployee.getId());

        delete(id);
    }

    @Override
    public void delete(long id) {
        employeeRepo.deleteById(id);
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

    private boolean emailIsInDataBase(Employee employee) {
        String email = employee.getEmail();
        Employee employeeInDataBase = readByEmail(email);
        return employeeInDataBase != null;
    }

    private boolean isManager(Employee loggedEmployee) {
        return loggedEmployee.getPosition().equals(EmployeePosition.MANAGER);
    }

    private boolean isOperatorFromManagersCity(Employee operator, Employee manager) {
        return operator.getPosition().equals(EmployeePosition.OPERATOR) && operator.getCity().equals(manager.getCity());
    }

}