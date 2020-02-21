package app.services;

import app.domain.ClientVisa;
import app.domain.enums.VisaType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface VisaServiceInterface extends CrudServiceInterface<ClientVisa> {

    Page<ClientVisa> readAll(UserDetails userDetails, String visaNumber, VisaType issuedVisaType, String issueDate, String expiryDate,
                             String lastName, String passportId, String email, String phoneNumber, Pageable pageable);

}