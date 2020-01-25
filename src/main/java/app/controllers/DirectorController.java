package app.controllers;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import app.dto.EmployeeDto;
import app.dto.assembler.EmployeeDtoAssembler;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.services.impl.AdminServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("employees")
@RolesAllowed("ROLE_DIRECTOR")
public class DirectorController {

    private final AdminServiceImpl adminService;

    @Autowired
    public DirectorController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<CollectionModel<EmployeeDto>> getEmployees(@RequestParam(required = false) String key) {
        if (key == null) {
            List<Admin> employees = adminService.readAll();

            if (employees.isEmpty()) {
                throw new NotFoundException("Cannot find employees.");
            }

            CollectionModel<EmployeeDto> dtos = new EmployeeDtoAssembler().toCollectionModel(employees);
            return ResponseEntity.ok(dtos);

        } else {
            List<Admin> employees = new ArrayList<>();
            boolean found = false;

            City[] cities = City.values();
            for (City city : cities) {
                if (city.toString().equals(key.toUpperCase())) {
                    employees = adminService.readByCity(City.valueOf(key.toUpperCase()));
                    found = true;
                    break;
                }
            }

            if (!found) {
                AdminPosition[] positions = AdminPosition.values();
                for (AdminPosition position : positions) {
                    if (position.toString().equals(key.toUpperCase())) {
                        employees = adminService.readByPosition(AdminPosition.valueOf(key.toUpperCase()));
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                employees = adminService.readByLastName(key);
            }

            if (!employees.isEmpty() && employees.get(0) != null) {
                CollectionModel<EmployeeDto> dtos = new EmployeeDtoAssembler().toCollectionModel(employees);
                return ResponseEntity.ok(dtos);
            } else {
                throw new NotFoundException("Cannot find employee by key '" + key + "'.");
            }
        }
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        if (!isNumber(id)) {
            throw new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
        }

        Admin employee = adminService.readById(Long.parseLong(id));

        if (employee == null) {
            throw new NotFoundException("Cannot find employee with ID = '" + id + "'.");
        }

        EmployeeDto dto = new EmployeeDtoAssembler().toModel(employee);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody Admin employee) {
        Admin createdEmployee = adminService.create(employee);
        EmployeeDto dto = new EmployeeDtoAssembler().toModel(createdEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String id, @Valid @RequestBody Admin newEmployee,
                                                      Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Admin loggedEmployee = adminService.readByEmail(userDetails.getUsername());

        if (!isNumber(id)) {
            throw new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
        }

        long parsedId = Long.parseLong(id);

        if (parsedId == loggedEmployee.getId() && !loggedEmployee.getPosition().equals(newEmployee.getPosition())) {
            throw new BadRequestException("The logged employee cannot change his/her position.");
        }

        Admin updatedEmployee = adminService.update(parsedId, newEmployee);
        EmployeeDto dto = new EmployeeDtoAssembler().toModel(updatedEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable String id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Admin loggedEmployee = adminService.readByEmail(userDetails.getUsername());

        if (!isNumber(id)) {
            throw new BadRequestException("ID = '" + id + "' is not valid. ID should be a number.");
        }

        long parsedId = Long.parseLong(id);

        if (parsedId == loggedEmployee.getId()) {
            throw new BadRequestException("The logged employee cannot delete his-/herself.");
        }

        adminService.delete(parsedId);
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

}