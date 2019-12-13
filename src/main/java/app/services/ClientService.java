package app.services;

import app.dao.ClientDao;
import app.dao.EntityTransaction;
import app.entities.Client;

public class ClientService {
    public boolean addClient(Client client) {
        ClientDao dao = new ClientDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        boolean isRegistered = false;
        if (client != null && !isInDataBase(client, dao)) {
            isRegistered = dao.create(client);
        }

        transaction.commit();
        transaction.end();

        return isRegistered;
    }

    public Client login(String email, String password) {
        ClientDao dao = new ClientDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        Client client = dao.authenticate(email, password);

        transaction.commit();
        transaction.end();

        return client;
    }

    private boolean isInDataBase(Client client, ClientDao dao) {
        boolean isInDataBase = false;
        Client clientInDataBase = dao.readByEmail(client.getEmail());

        if (clientInDataBase != null && clientInDataBase.getId() != client.getId()) {
            isInDataBase = true;
        }

        return isInDataBase;
    }
}