package app.dao;

import app.entities.Client;
import app.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl implements UserDaoInterface<Client> {
    @Override
    public long create(Client client) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(client);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Client> readAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Client> clients = new ArrayList<>();
        try {
            clients = session.createQuery("FROM Client").list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return clients;
    }

    @Override
    public Client readById(long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Client client = null;
        try {
            client = session.get(Client.class, id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client;
    }

    @Override
    public Client update(Client client) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Client updatedClient = null;
        try {
            updatedClient = (Client) session.merge(client);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return updatedClient;
    }

    @Override
    public void delete(Client client) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (client != null) {
                session.delete(client);
                transaction.commit();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Client authenticate(String patternEmail, String patternPassword) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Client client = null;
        try {
            Query<Client> query = session.createQuery("FROM Client WHERE email = :patternEmail AND password = :patternPassword");
            query.setParameter("patternEmail", patternEmail);
            query.setParameter("patternPassword", patternPassword);
            client = query.uniqueResult();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Client readByEmail(String patternEmail) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Client client = null;
        try {
            Query<Client> query = session.createQuery("FROM Client WHERE email = :patternEmail");
            query.setParameter("patternEmail", patternEmail);
            client = query.uniqueResult();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Client> readByLastName(String patternLastName) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Client> clients = new ArrayList<>();
        try {
            Query<Client> query = session.createQuery("FROM Client WHERE lastName = :patternLastName");
            query.setParameter("patternLastName", patternLastName);
            clients = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return clients;
    }

    @Override
    @SuppressWarnings("unchecked")
    public long getIdByEmail(String patternEmail) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        long id = 0;
        try {
            Query<Long> query = session.createQuery("SELECT id FROM Client WHERE email = :patternEmail");
            query.setParameter("patternEmail", patternEmail);
            id = query.uniqueResult();
        } catch (NullPointerException | PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }
}