package app.services;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import java.util.List;

public interface EmployeeServiceInterface extends UserServiceInterface<Employee> {

    List<Employee> readByCity(City city);

    List<Employee> readByPosition(EmployeePosition position);

    List<Employee> readByPositionAndCity(EmployeePosition position, City city);

    long countAdminsByCityAndPosition(City city, EmployeePosition position);

}