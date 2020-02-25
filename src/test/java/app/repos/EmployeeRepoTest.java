package app.repos;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Before
    public void createEmployee() {
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setFirstName("ADMIN");
            employee.setLastName("ADMIN");
            employee.setEmail("email@email" + i);
            employee.setPhoneNumber("37544100101" + i);
            employee.setPassword("12345678");
            employee.setPosition(EmployeePosition.DIRECTOR);
            employee.setCity(City.MINSK);
            employeeRepo.save(employee);
        }
    }

    @Test
    public void test_findAll_size() {
        assertEquals(10, employeeRepo.findAll().size());
    }

}