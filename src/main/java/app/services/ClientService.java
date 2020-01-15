package app.services;

import app.dao.ClientDaoImpl;
import app.entities.Client;

import java.util.List;

public class ClientService {
    private ClientDaoImpl dao = new ClientDaoImpl();

    public long addClient(Client client) {
        long clientId = 0;
        if (client != null && !isInDataBase(client)) {
            clientId = dao.create(client);
        }
        return clientId;
    }

    public List<Client> findAllClients() {
        List<Client> clients = dao.readAll();
        return clients;
    }

    public Client findClientById(long id) {
        Client client = dao.readById(id);
        return client;
    }

    public Client updateClient(Client client) {
        Client updatedClient = dao.update(client);
        return updatedClient;
    }

    public void deleteClient(Client client) {
        dao.delete(client);
    }

    public Client login(String email, String password) {
        Client client = dao.authenticate(email, password);
        return client;
    }

    public Client findClientByEmail(String email) {
        Client client = dao.readByEmail(email);
        return client;
    }

    public List<Client> findClientByLastName(String lastName) {
        List<Client> clients = dao.readByLastName(lastName);
        return clients;
    }

    public long findIdByEmail(String email) {
        long id = dao.getIdByEmail(email);
        return id;
    }

    private boolean isInDataBase(Client client) {
        boolean isInDataBase = false;
        Client clientInDataBase = dao.readByEmail(client.getEmail());
        if (clientInDataBase != null && clientInDataBase.getId() != client.getId()) {
            isInDataBase = true;
        }
        return isInDataBase;
    }
}