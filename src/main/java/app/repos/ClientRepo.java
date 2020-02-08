package app.repos;

import app.domain.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Client findByEmail(String email);

    List<Client> findByLastName(String lastName);

    Client findByPassportId(String passportId);

}