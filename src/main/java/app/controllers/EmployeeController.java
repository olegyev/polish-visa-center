package app.controllers;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.dto.EmployeeDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.specs.EmployeeJpaSpecification;
import app.services.EmployeeServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

/* ROLE_DIRECTOR operates all employees.
 * ROLE_MANAGER operates only operators from his/her city.*/

@RestController
@RequestMapping("employees")
@RolesAllowed({"ROLE_DIRECTOR", "ROLE_MANAGER"})
public class EmployeeController {

    private final static Logger log = LogManager.getLogger(EmployeeController.class);

    private final EmployeeServiceInterface employeeService;
    private final DtoAssemblerInterface<Employee, EmployeeDto> assembler;

    @Autowired
    public EmployeeController(final EmployeeServiceInterface employeeService,
                              final DtoAssemblerInterface<Employee, EmployeeDto> assembler) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<CollectionModel<EmployeeDto>> getEmployees(@RequestParam(required = false) String city,
                                                                     @RequestParam(required = false) String position,
                                                                     @RequestParam(required = false) String lastName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());

        if (city == null && position == null && lastName == null) {
            List<Employee> employees = !isManager(loggedEmployee)
                    ? employeeService.readAll(null)
                    : employeeService.readByPositionAndCity(EmployeePosition.OPERATOR, loggedEmployee.getCity());

            if (employees.isEmpty()) {
                NotFoundException exception = new NotFoundException("Cannot find employees.");
                log.error(exception);
                throw exception;
            } else {
                log.info("Found employees by no search criteria by employee with ID = {}.", loggedEmployee.getId());
            }

            CollectionModel<EmployeeDto> dtos = assembler.toCollectionModel(employees);
            return ResponseEntity.ok(dtos);

        } else {
            Employee filter = new Employee();
            List<Employee> employees = new ArrayList<>();

            try {

                if (!isManager(loggedEmployee)) {
                    if (city != null) {
                        filter.setCity(City.valueOf(city.toUpperCase()));
                    }

                    if (position != null) {
                        filter.setPosition(EmployeePosition.valueOf(position.toUpperCase()));
                    }

                    if (lastName != null) {
                        filter.setLastName(lastName.toUpperCase());
                    }

                    Specification<Employee> spec = new EmployeeJpaSpecification(filter);
                    employees = employeeService.readAll(spec);

                    if (employees.isEmpty() || employees.get(0) == null) {
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
                        List<Employee> operatorsInManagersCity = employeeService.readByPositionAndCity(
                                EmployeePosition.OPERATOR, loggedEmployee.getCity()
                        );

                        for (Employee operator : operatorsInManagersCity) {
                            if (operator.getLastName().equals(lastName.toUpperCase())) {
                                employees.add(operator);
                            }
                        }

                        if (employees.isEmpty() || employees.get(0) == null) {
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

            } catch (IllegalArgumentException e) {
                log.catching(e);
                throw new BadRequestException("Request parameters are incorrect.");
            }

            CollectionModel<EmployeeDto> dtos = assembler.toCollectionModel(employees);
            return ResponseEntity.ok(dtos);
        }
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id, Authentication authentication) {
        if (!isNumber(id)) {
            BadRequestException exception = new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
            log.error(exception);
            throw exception;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());

        Employee employee = employeeService.readById(Long.parseLong(id));

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

        EmployeeDto dto = assembler.toModel(employee);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody Employee employee, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());

        if (isManager(loggedEmployee) && !isOperatorFromManagersCity(employee, loggedEmployee)) {
            log.error("Manager with ID = {} from {} tried to add not operator from his/her city.",
                    loggedEmployee.getId(), loggedEmployee.getCity().toString());
            throw new BadRequestException("A manager can add only operators in his/her city.");
        }

        Employee createdEmployee = employeeService.create(employee);

        log.info("New employee added to a database with ID = {} and username = '{}' by employee with ID = {}.",
                createdEmployee.getId(), createdEmployee.getEmail(), loggedEmployee.getId());

        EmployeeDto dto = assembler.toModel(createdEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee newEmployee,
                                                      Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());

        if (!isNumber(id)) {
            BadRequestException exception = new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
            log.error(exception);
            throw exception;
        }

        long parsedId = Long.parseLong(id);

        if (parsedId == loggedEmployee.getId() && !loggedEmployee.getPosition().equals(newEmployee.getPosition())) {
            log.error("Employee with ID = {} tried to change his/her position from '{}' to '{}'.",
                    loggedEmployee.getId(), loggedEmployee.getPosition().toString(), newEmployee.getPosition().toString());
            throw new BadRequestException("The logged employee cannot change his/her position.");
        } else if (isManager(loggedEmployee)) {
            Employee employeeToUpdate = employeeService.readById(parsedId);

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
                        parsedId, newEmployee.getPosition().toString(), newEmployee.getCity().toString());
                throw new BadRequestException("A manager cannot change operator's position or city.");
            }
        }

        Employee updatedEmployee = employeeService.update(parsedId, newEmployee);

        log.info("Updated employee with ID = {} by employee with ID = {}.", parsedId, loggedEmployee.getId());

        EmployeeDto dto = assembler.toModel(updatedEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable String id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());

        if (!isNumber(id)) {
            BadRequestException exception = new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
            log.error(exception);
            throw exception;
        }

        long parsedId = Long.parseLong(id);

        if (parsedId == loggedEmployee.getId()) {
            log.error("The logged employee with ID = {} tried to delete him-/herself.", loggedEmployee.getId());
            throw new BadRequestException("The logged employee cannot delete him-/herself.");
        } else if (isManager(loggedEmployee)) {
            Employee employeeToDelete = employeeService.readById(parsedId);

            if (employeeToDelete == null || !isOperatorFromManagersCity(employeeToDelete, loggedEmployee)) {
                NotFoundException exception = new NotFoundException("Cannot find operator in "
                        + loggedEmployee.getCity().toString()
                        + " with ID = " + id + ".");
                log.error(exception);
                throw exception;
            }
        }

        employeeService.delete(parsedId);

        log.info("Deleted employee with ID = {} by employee with ID = {}.", parsedId, loggedEmployee.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isNumber(String idToCheck) {
        try {
            Long.parseLong(idToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isManager(Employee loggedEmployee) {
        return loggedEmployee.getPosition().equals(EmployeePosition.MANAGER);
    }

    private boolean isOperatorFromManagersCity(Employee operator, Employee manager) {
        return operator.getPosition().equals(EmployeePosition.OPERATOR) && operator.getCity().equals(manager.getCity());
    }

}