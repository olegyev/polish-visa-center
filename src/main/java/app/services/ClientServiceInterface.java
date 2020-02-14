package app.services;

import app.domain.Client;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.dto.ClientDtoRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface ClientServiceInterface extends UserServiceInterface<Client> {

    Page<Client> readAll(UserDetails userDetails,
                         String lastName,
                         String passportId,
                         String email,
                         String phoneNumber,
                         VisaType requiredVisaType,
                         City appointmentCity,
                         String appointmentDate,
                         String appointmentTime,
                         VisaApplicationStatus visaApplicationStatus,
                         String visaNumber,
                         VisaType visaType,
                         String issueDate,
                         String expiryDate,
                         Pageable pageable);

    Client readById(long id, UserDetails userDetails);

    Client update(long id, ClientDtoRequest newClient);

    Client update(long id, ClientDtoRequest newClient, UserDetails userDetails);

    void delete(long id, UserDetails userDetails);

    Client readByPassportId(String passportId);

}