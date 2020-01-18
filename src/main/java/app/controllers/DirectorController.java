package app.controllers;

import app.domain.Admin;
import app.domain.enums.City;
import app.domain.views.UserViews;
import app.exceptions.NotFoundException;
import app.services.impl.AdminServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/* Director operates all employees. */

@RestController
@RequestMapping("employees")
public class DirectorController {

    private final AdminServiceImpl adminService;

    @Autowired
    public DirectorController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public List<Admin> getAllEmployees() {
        return adminService.readAll();
    }

    @GetMapping("{id}")
    @JsonView(UserViews.IncludingPassword.class)
    public Admin getAdminById(@PathVariable("id") Admin admin) {
        return admin;
    }

    @GetMapping("search/{key}")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public List<Admin> getEmployeesByKey(@PathVariable String key) {
        List<Admin> employees = new ArrayList<>();

        if (key.contains("@")) {
            employees.add(adminService.readByEmail(key));
        } else {
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
                employees = adminService.readByLastName(key);
            }
        }

        if (!employees.isEmpty() && employees.get(0) != null) {
            return employees;
        } else {
            throw new NotFoundException("Cannot find employee by key '" + key +"'");
        }
    }

    @PostMapping
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public Admin addEmployee(@Valid @RequestBody Admin employee) {
        return adminService.create(employee);
    }

    @PutMapping("{id}")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    public Admin updateEmployee(@PathVariable("id") Admin oldEmployee, @Valid @RequestBody Admin newEmployee) {
        return adminService.update(oldEmployee, newEmployee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable long id) {
        adminService.delete(id);
    }

}