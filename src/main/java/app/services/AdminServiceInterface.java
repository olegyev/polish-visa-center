package app.services;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminServiceInterface extends UserServiceInterface<Admin> {

    List<Admin> readByCity(City city);

    List<Admin> readByPosition(AdminPosition position);

    List<Admin> readByPositionAndCity(AdminPosition position, City city);

    long countAdminsByCityAndPosition(City city, AdminPosition position);

}