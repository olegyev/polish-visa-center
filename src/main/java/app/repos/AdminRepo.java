package app.repos;

import app.domain.Admin;
import app.domain.enums.AdminPosition;
import app.domain.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    Admin findByEmailAndPassword(String email, String password);
    Admin findByEmail(String email);
    List<Admin> findByLastName(String lastName);
    List<Admin> findByCity (City city);
    List<Admin> findByPosition(AdminPosition position);
    List<Admin> readByPositionAndCity(AdminPosition position, City city);
    long countAdminsByCityAndPosition (City city, AdminPosition position);

}