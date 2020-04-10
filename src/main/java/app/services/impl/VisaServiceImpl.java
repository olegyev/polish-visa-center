package app.services.impl;

import app.domain.Client;
import app.domain.ClientVisa;
import app.domain.Employee;
import app.domain.enums.VisaType;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.VisaRepo;
import app.repos.specs.VisaJpaSpecification;
import app.services.ClientServiceInterface;
import app.services.EmployeeServiceInterface;
import app.services.VisaServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class VisaServiceImpl implements VisaServiceInterface {

    private final static Logger log = LogManager.getLogger(VisaApplicationServiceImpl.class);

    private final VisaRepo visaRepo;
    private final EmployeeServiceInterface employeeService;
    private final ClientServiceInterface clientService;

    @Autowired
    public VisaServiceImpl(final VisaRepo visaRepo,
                           final EmployeeServiceInterface employeeService,
                           final ClientServiceInterface clientService) {
        this.visaRepo = visaRepo;
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public ClientVisa create(UserDetails userDetails, ClientVisa visa, long clientId) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        Client client = clientService.readById(clientId);

        if (client == null) {
            NotFoundException exception = new NotFoundException("Cannot find client with ID = " + clientId + ".");
            log.error(exception);
            throw exception;
        }

        visa.setClient(client);

        if (!bodyIsOk(visa)) {
            log.error("Attempt to add new client's visa failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (!expiryDateIsLaterThanIssueDate(visa.getIssueDate(), visa.getExpiryDate())) {
            log.error("Attempt to add new client's visa failed because indicated issue date is later than expiry date.");
            throw new BadRequestException("The expiry date should be later than issue date.");
        }

        log.info("Created new visa for client with ID = {} by employee with ID = {}.", clientId, loggedOperator.getId());
        return create(visa);
    }

    @Override
    @Transactional
    public ClientVisa create(ClientVisa clientVisa) {
        return visaRepo.save(clientVisa);
    }

    @Override
    @Transactional
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
    @Transactional
    public Page<ClientVisa> readAll(Specification<ClientVisa> spec, Pageable pageable) {
        return visaRepo.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public ClientVisa readById(long id) {
        return visaRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public ClientVisa readByClientIdAndApplicationId(UserDetails userDetails, long clientId, long visaId) {
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());
        ClientVisa visa = visaRepo.findByIdAndClient(visaId, clientService.readById(clientId));

        if (visa == null) {
            NotFoundException exception = new NotFoundException("Cannot find visa with ID = " + visaId +
                    " for client with ID = " + clientId + ".");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found client's visa with ID = {} by employee with ID = {}.", visaId, loggedEmployee.getId());
        }

        return visa;
    }

    @Override
    @Transactional
    public ClientVisa update(UserDetails userDetails, long clientId, long visaId, ClientVisa newVisa) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        ClientVisa visaFromDb = readByClientIdAndApplicationId(userDetails, clientId, visaId);

        if (!bodyIsOk(newVisa)) {
            log.error("Attempt to update client's visa with ID = {} by operator with ID = {} failed due to the incorrect form filling.",
                    visaId, loggedOperator.getId());
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (visaFromDb == null) {
            BadRequestException exception = new BadRequestException("Cannot find visa with ID = " + visaId + " for client with ID = " + clientId + ".");
            log.error(exception);
            throw exception;
        }

        if (!expiryDateIsLaterThanIssueDate(newVisa.getIssueDate(), newVisa.getExpiryDate())) {
            log.error("Attempt to add new client's visa failed because indicated issue date is later than expiry date.");
            throw new BadRequestException("The expiry date should be later than issue date.");
        }

        BeanUtils.copyProperties(newVisa, visaFromDb, "id", "client");

        log.info("Updated visa with ID = {} for client with ID = {} by operator with ID = {}.",
                visaId, clientId, loggedOperator.getId());

        return update(visaFromDb.getId(), visaFromDb);
    }

    @Override
    @Transactional
    public ClientVisa update(long id, ClientVisa clientVisa) {
        return visaRepo.save(clientVisa);
    }

    @Override
    @Transactional
    public void delete(UserDetails userDetails, long clientId, long visaId) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        ClientVisa visa = readByClientIdAndApplicationId(userDetails, clientId, visaId);

        if (visa == null) {
            NotFoundException exception = new NotFoundException("Cannot find visa with ID = " + visaId +
                    " for client with ID = " + clientId + ".");
            log.error(exception);
            throw exception;
        }

        log.info("Deleted visa with ID = {} for client with ID = {} by operator with ID = {}.",
                visaId, clientId, loggedOperator.getId());

        delete(visa.getId());
    }

    @Override
    @Transactional
    public void delete(long id) {
        visaRepo.deleteById(id);
    }

    private boolean expiryDateIsLaterThanIssueDate(Date issueDate, Date expiryDate) {
        return expiryDate.getTime() > issueDate.getTime();
    }

    private boolean bodyIsOk(ClientVisa body) {
        return body.getVisaNumber() != null
                && body.getVisaType() != null
                && body.getIssueDate() != null
                && body.getExpiryDate() != null;
    }

}