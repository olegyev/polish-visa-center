package app.services;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface EmployeeServiceInterface extends UserServiceInterface<Employee> {

    Page<Employee> readAll(UserDetails userDetails, City city, EmployeePosition position, String lastName, Pageable pageable);

    Employee readById(long id, UserDetails userDetails);

    Employee create(Employee employee, UserDetails userDetails);

    Employee update(long id, Employee newEmployee, UserDetails userDetails);

    void delete(long id, UserDetails userDetails);

    List<Employee> readByCity(City city);

    List<Employee> readByPosition(EmployeePosition position);

    List<Employee> readByPositionAndCity(EmployeePosition position, City city);

    long countEmployeesByCityAndPosition(City city, EmployeePosition position);

}