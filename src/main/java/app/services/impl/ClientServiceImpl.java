package app.services.impl;

import app.domain.Client;
import app.exceptions.NotFoundException;
import app.repos.ClientRepo;
import app.services.ClientServiceInterface;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientServiceInterface {

    private final ClientRepo clientRepo;

    @Autowired
    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Client create(Client client) {
        return clientRepo.save(client);
    }

    @Override
    public List<Client> readAll() {
        return clientRepo.findAll();
    }

    @Override
    public Client readById(long id) {
        return clientRepo.findById(id).orElse(null);
    }

    @Override
    public Client update(long id, Client newClient) {
        Client clientFromDb;
        clientFromDb = clientRepo.findById(id).orElse(null);
        if (clientFromDb == null) {
            throw new NotFoundException("Cannot find employee by id = '" + id + "'.");
        }
        BeanUtils.copyProperties(newClient, clientFromDb, "id");
        return clientRepo.save(clientFromDb);
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

}