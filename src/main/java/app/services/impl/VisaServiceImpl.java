package app.services.impl;

import app.domain.ClientVisa;
import app.domain.Employee;
import app.domain.enums.VisaType;
import app.exceptions.NotFoundException;
import app.repos.VisaRepo;
import app.repos.specs.VisaJpaSpecification;
import app.services.EmployeeServiceInterface;
import app.services.VisaServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class VisaServiceImpl implements VisaServiceInterface {

    private final static Logger log = LogManager.getLogger(VisaApplicationServiceImpl.class);

    private final VisaRepo visaRepo;
    private final EmployeeServiceInterface employeeService;

    @Autowired
    public VisaServiceImpl(final VisaRepo visaRepo,
                           final EmployeeServiceInterface employeeService) {
        this.visaRepo = visaRepo;
        this.employeeService = employeeService;
    }

    @Override
    public ClientVisa create(ClientVisa clientVisa) {
        return null;
    }

    @Override
    public Page<ClientVisa> readAll(UserDetails userDetails, String visaNumber, VisaType issuedVisaType, String issueDate,
                                    String expiryDate, String lastName, String passportId, String email,
                                    String phoneNumber, Pageable pageable) {
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());
        Page<ClientVisa> visas;

        Specification<ClientVisa> spec = Specification
                .where(visaNumber == null ? null : VisaJpaSpecification.visaNumberContains(visaNumber))
                .and(issuedVisaType == null ? null : VisaJpaSpecification.issuedVisaTypeContains(issuedVisaType))
                .and(issueDate == null ? null : VisaJpaSpecification.issueDateContains(issueDate))
                .and(expiryDate == null ? null : VisaJpaSpecification.expiryDateContains(expiryDate))
                .and(lastName == null ? null : VisaJpaSpecification.lastNameContains(lastName.toUpperCase()))
                .and(passportId == null ? null : VisaJpaSpecification.passportIdContains(passportId))
                .and(email == null ? null : VisaJpaSpecification.emailContains(email))
                .and(phoneNumber == null ? null : VisaJpaSpecification.phoneNumberContains(phoneNumber));

        visas = readAll(spec, pageable);

        if (visas.isEmpty()) {
            NotFoundException exception = new NotFoundException("Cannot find visas.");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found visas by employee with ID = {}.", loggedEmployee.getId());
        }

        return visas;
    }

    @Override
    public Page<ClientVisa> readAll(Specification<ClientVisa> spec, Pageable pageable) {
        return visaRepo.findAll(spec, pageable);
    }

    @Override
    public ClientVisa readById(long id) {
        return null;
    }

    @Override
    public ClientVisa update(long id, ClientVisa clientVisa) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}