package app.dao;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import javax.persistence.PersistenceException;
import java.util.List;

public interface AdminDaoInterface extends UserDaoInterface<Admin> {
    List<Admin> readByCity(City city) throws PersistenceException;
    List<Admin> readByPositionAndCity(AdminPosition position, City city) throws PersistenceException;
    long countAdminsByCityAndPosition(City city, AdminPosition position) throws PersistenceException;
}