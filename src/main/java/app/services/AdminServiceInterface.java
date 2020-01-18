package app.services;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminServiceInterface extends CrudServiceInterface<Admin> {

    Admin authenticate(String email, String password);
    Admin readByEmail(String email);
    List<Admin> readByLastName(String lastName);
    List<Admin> readByCity(City city);
    List<Admin> readByPositionAndCity(AdminPosition position, City city);
    long countAdminsByCityAndPosition(City city, AdminPosition position);

}