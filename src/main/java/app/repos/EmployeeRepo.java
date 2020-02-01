package app.repos;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Employee findByEmail(String email);

    List<Employee> findByLastName(String lastName);

    List<Employee> findByCity(City city);

    List<Employee> findByPosition(EmployeePosition position);

    List<Employee> readByPositionAndCity(EmployeePosition position, City city);

    long countAdminsByCityAndPosition(City city, EmployeePosition position);

}