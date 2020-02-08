package app.services.impl;

import app.domain.Client;
import app.domain.Employee;
import app.dto.ClientDtoRequest;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.ClientRepo;
import app.repos.specs.ClientJpaSpecification;
import app.security.utils.PasswordEncoder;
import app.services.ClientServiceInterface;
import app.services.EmployeeServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientServiceInterface {

    private final static Logger log = LogManager.getLogger(ClientServiceImpl.class);

    private final ClientRepo clientRepo;
    private final EmployeeServiceInterface employeeService;

    @Autowired
    public ClientServiceImpl(final ClientRepo clientRepo,
                             final EmployeeServiceInterface employeeService) {
        this.clientRepo = clientRepo;
        this.employeeService = employeeService;
    }

    @Override
    public Client create(Client client) {
        if (!bodyIsOk(client)) {
            log.error("Attempt to add new client failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        }

        if (emailIsInDataBase(client) || passportIdIsInDataBase(client)) {
            log.error("Attempt to add new client failed due to the presence of another client" +
                    " with the same email and/or passport ID in the database.");
            throw new BadRequestException("There is another client in the database with such email and/or passport ID.");
        }

        if (!client.isPersonalDataProcAgreement()) {
            log.error("Attempt to add new client failed due to the absence of the agreement to a processing of the personal data.");
            throw new BadRequestException("You need to select checkbox 'Agreement to a processing of the personal data'.");
        }

        client.setPassword(PasswordEncoder.encryptPassword(client.getPassword()));
        Client createdClient = clientRepo.save(client);

        log.info("New employee added to a database with ID = {} and username = '{}'.",
                createdClient.getId(), createdClient.getEmail());

        return createdClient;
    }

    @Override
    public List<Client> readAll(UserDetails userDetails, String lastName, String passportId, String email, String phoneNumber, Pageable pageable) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        List<Client> clients;

        if (lastName == null && passportId == null && email == null && phoneNumber == null) {
            clients = readAll(null, pageable);

            if (clients.isEmpty()) {
                NotFoundException exception = new NotFoundException("Cannot find clients.");
                log.error(exception);
                throw exception;
            } else {
                log.info("Found employees by no search criteria by employee with ID = {}.", loggedOperator.getId());
            }

        } else {
            Specification<Client> spec = Specification
                    .where(lastName == null ? null : ClientJpaSpecification.lastNameContains(lastName.toUpperCase()))
                    .and(passportId == null ? null : ClientJpaSpecification.passportIdContains(passportId))
                    .and(email == null ? null : ClientJpaSpecification.emailContains(email))
                    .and(phoneNumber == null ? null : ClientJpaSpecification.phoneNumberContains(phoneNumber));

            clients = readAll(spec, pageable);

            if (clients.isEmpty() || clients.get(0) == null) {
                NotFoundException exception = new NotFoundException("Cannot find clients by search criteria.");
                log.error(exception);
                throw exception;
            } else {
                log.info("Found clients by search criteria by operator with ID = {}.", loggedOperator.getId());
            }
        }

        return clients;
    }

    @Override
    public List<Client> readAll(Specification<Client> spec, Pageable pageable) {
        Page<Client> page = clientRepo.findAll(spec, pageable);
        return page.getContent();
    }

    @Override
    public Client readById(long id, UserDetails userDetails) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        Client employee = readById(id);

        if (employee == null) {
            NotFoundException exception = new NotFoundException("Cannot find client with ID = " + id + ".");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found client with ID = {} by operator with ID = {}.", id, loggedOperator.getId());
        }

        return employee;
    }

    @Override
    public Client readById(long id) {
        return clientRepo.findById(id).orElse(null);
    }

    @Override
    public Client update(long id, ClientDtoRequest newClient, UserDetails userDetails) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());

        Client updatedClient;
        if (!bodyIsOk(newClient)) {
            log.error("Attempt to update a client failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        } else {
            updatedClient = update(id, newClient);
        }

        log.info("Updated client with ID = {} by operator with ID = {}.", id, loggedOperator.getId());

        return updatedClient;
    }

    @Override
    public Client update(long id, ClientDtoRequest newClient) {
        Client clientFromDb = readById(id);

        if (clientFromDb == null) {
            NotFoundException exception = new NotFoundException("Cannot find client with ID = " + id + ".");
            log.error(exception);
            throw exception;

        } else if ((emailIsInDataBase(newClient) && !clientFromDb.getEmail().equals(newClient.getEmail()))
                || (passportIdIsInDataBase(newClient) && !clientFromDb.getPassportId().equals(newClient.getPassportId()))) {
            log.error("Attempt to update a client with ID = {} failed due to the presence of another"
                            + " client with the same email and/or passport ID in the database (EMAIL = '{}', PASSPORT ID = '{}').",
                    id, newClient.getEmail(), newClient.getPassportId());
            throw new BadRequestException("There is another client in database with such email and/or passport ID.");

        } else {
            BeanUtils.copyProperties(newClient, clientFromDb, "id", "password", "personalDataProcAgreement");
        }

        return clientRepo.save(clientFromDb);
    }

    /* IGNORED */
    @Override
    public Client update(long id, Client client) {
        return null;
    }

    @Override
    public void delete(long id, UserDetails userDetails) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        Client client = readById(id);

        if (client == null) {
            NotFoundException exception = new NotFoundException("Cannot find client with ID = " + id + ".");
            log.error(exception);
            throw exception;
        }

        log.info("Deleted client with ID = {} by operator with ID = {}.", id, loggedOperator.getId());

        delete(id);
    }

    @Override
    public void delete(long id) {
        clientRepo.deleteById(id);
    }

    @Override
    public Client readByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public List<Client> readByLastName(String lastName) {
        return clientRepo.findByLastName(lastName);
    }

    @Override
    public Client readByPassportId(String passportId) {
        return clientRepo.findByPassportId(passportId);
    }

    private boolean bodyIsOk(Client body) {
        return body.getFirstName() != null
                && body.getLastName() != null
                && body.getDateOfBirth() != null
                && body.getPassportId() != null
                && body.getOccupation() != null
                && body.getEmail() != null
                && body.getPhoneNumber() != null
                && body.getPassword() != null;
    }

    private boolean bodyIsOk(ClientDtoRequest body) {
        return body.getFirstName() != null
                && body.getLastName() != null
                && body.getDateOfBirth() != null
                && body.getPassportId() != null
                && body.getOccupation() != null
                && body.getEmail() != null
                && body.getPhoneNumber() != null;
    }

    private boolean emailIsInDataBase(Client client) {
        String email = client.getEmail();
        Client clientInDataBase = readByEmail(email);
        return clientInDataBase != null;
    }

    private boolean emailIsInDataBase(ClientDtoRequest client) {
        String email = client.getEmail();
        Client clientInDataBase = readByEmail(email);
        return clientInDataBase != null;
    }

    private boolean passportIdIsInDataBase(Client client) {
        String passportId = client.getPassportId();
        Client clientInDataBase = readByPassportId(passportId);
        return clientInDataBase != null;
    }

    private boolean passportIdIsInDataBase(ClientDtoRequest client) {
        String passportId = client.getPassportId();
        Client clientInDataBase = readByPassportId(passportId);
        return clientInDataBase != null;
    }

}