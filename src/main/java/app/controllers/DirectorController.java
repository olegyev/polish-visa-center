package app.controllers;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import app.domain.views.UserViews;
import app.exceptions.NotFoundException;
import app.services.impl.AdminServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/* Director operates all employees. */

@RestController
@RequestMapping("director/{director_id}") // Path variable "director_id" is added after login because of REST's statelessness.
public class DirectorController {

    private final AdminServiceImpl adminService;

    @Autowired
    public DirectorController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public Admin getDirectorById(@PathVariable("director_id") Admin director) {
        return director;
    }

    @GetMapping("employees")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public List<Admin> getAllEmployees() {
        return adminService.readAll();
    }

    @GetMapping("employee/{id}")
    @JsonView(UserViews.IncludingPassword.class)
    public Admin getEmployeeById(@PathVariable("id") Admin employee) {
        return employee;
    }

    /* Search employees by city, position and last name. */

    @GetMapping("employees/{key}")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public List<Admin> getEmployeesByKey(@PathVariable String key) {
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
            return employees;
        } else {
            throw new NotFoundException("Cannot find employee by key '" + key + "'");
        }
    }

    @PostMapping("employee")
    public ResponseEntity<HttpStatus> addEmployee(@Valid @RequestBody Admin employee) {
        adminService.create(employee);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("employee/{id}")
    public ResponseEntity<HttpStatus> updateEmployee(@PathVariable("id") Admin oldEmployee, @Valid @RequestBody Admin newEmployee) {
        adminService.update(oldEmployee, newEmployee);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("employee/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable long id) {
        adminService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}