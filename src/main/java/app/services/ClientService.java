package app.services;

import app.dao.ClientDao;
import app.dao.EntityTransaction;
import app.entities.Client;

import java.sql.SQLException;

public class ClientService {
    private ClientDao dao = new ClientDao();
    private EntityTransaction transaction = new EntityTransaction();

    public boolean addClient(Client client) {
        boolean isRegistered = false;
        transaction.begin(dao);

        try {
            if (client != null && !isInDataBase(client)) {
                isRegistered = dao.create(client);
            }
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return isRegistered;
    }

    public Client login(String email, String password) {
        Client client = null;
        transaction.begin(dao);

        try {
            client = dao.authenticate(email, password);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return client;
    }

    public Client findClientById(long id) {
        Client client = null;
        transaction.begin(dao);

        try {
            client = dao.readById(id);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return client;
    }

    public Client findClientByEmail(String email) {
        Client client = null;
        transaction.begin(dao);

        try {
            client = dao.readByEmail(email);
            transaction.commit();
        } catch (SQLException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            transaction.end();
        }

        return client;
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