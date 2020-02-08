package app.services;

import app.domain.Client;
import app.dto.ClientDtoRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ClientServiceInterface extends UserServiceInterface<Client> {

    List<Client> readAll(UserDetails userDetails, String lastName, String passportId, String email, String phoneNumber, Pageable pageable);

    Client readById(long id, UserDetails userDetails);

    Client update(long id, ClientDtoRequest newClient);

    Client update(long id, ClientDtoRequest newClient, UserDetails userDetails);

    void delete(long id, UserDetails userDetails);

    Client readByPassportId(String passportId);

}