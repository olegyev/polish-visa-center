package app.dao;

import app.entities.Admin;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;
import app.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoImpl implements AdminDaoInterface {
    @Override
    public long create(Admin admin) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(admin);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admin.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Admin> readAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Admin> admins = new ArrayList<>();
        try {
            admins = session.createQuery("FROM Admin").list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admins;
    }

    @Override
    public Admin readById(long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Admin admin = null;
        try {
            admin = session.get(Admin.class, id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admin;
    }

    @Override
    public Admin update(Admin admin) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Admin updatedAdmin = null;
        try {
            updatedAdmin = (Admin) session.merge(admin);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return updatedAdmin;
    }

    @Override
    public void delete(Admin admin) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (admin != null) {
                session.delete(admin);
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
    public Admin authenticate(String patternEmail, String patternPassword) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Admin admin = null;
        try {
            Query<Admin> query = session.createQuery("FROM Admin WHERE email = :patternEmail AND password = :patternPassword");
            query.setParameter("patternEmail", patternEmail);
            query.setParameter("patternPassword", patternPassword);
            admin = query.uniqueResult();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admin;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Admin readByEmail(String patternEmail) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Admin admin = null;
        try {
            Query<Admin> query = session.createQuery("FROM Admin WHERE email = :patternEmail");
            query.setParameter("patternEmail", patternEmail);
            admin = query.uniqueResult();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admin;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Admin> readByLastName(String patternLastName) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Admin> admins = new ArrayList<>();
        try {
            Query<Admin> query = session.createQuery("FROM Admin WHERE lastName = :patternLastName");
            query.setParameter("patternLastName", patternLastName);
            admins = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admins;
    }

    @Override
    @SuppressWarnings("unchecked")
    public long getIdByEmail(String patternEmail) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        long id = 0;
        try {
            Query<Long> query = session.createQuery("SELECT id FROM Admin WHERE email = :patternEmail");
            query.setParameter("patternEmail", patternEmail);
            id = query.uniqueResult();
        } catch (NullPointerException | PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Admin> readByCity(City city) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Admin> admins = new ArrayList<>();
        try {
            Query<Admin> query = session.createQuery("FROM Admin WHERE city = :city");
            query.setParameter("city", city);
            admins = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admins;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Admin> readByPositionAndCity(AdminPosition position, City city) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Admin> admins = new ArrayList<>();
        try {
            Query<Admin> query = session.createQuery("FROM Admin WHERE position = :position AND city = :city");
            query.setParameter("position", position);
            query.setParameter("city", city);
            admins = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return admins;
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countAdminsByCityAndPosition(City city, AdminPosition position) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        long adminNum = 0;
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(position) AS num FROM Admin WHERE city = :city AND position = :position");
            query.setParameter("position", position);
            query.setParameter("city", city);
            adminNum = query.uniqueResult();
        } catch (NullPointerException | PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return adminNum;
    }
}