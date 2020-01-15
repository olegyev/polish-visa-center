package app.dao;

import app.entities.Appointment;
import app.entities.enums.City;
import app.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.*;

public class AppointmentDaoImpl implements AppointmentDaoInterface {
    @Override
    public long create(Appointment appointment) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(appointment);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appointment.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Appointment> readAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Appointment> appointments = new ArrayList<>();
        try {
            appointments = session.createQuery("FROM Appointment").list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appointments;
    }

    @Override
    public Appointment readById(long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Appointment appointment = null;
        try {
            appointment = session.get(Appointment.class, id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appointment;
    }

    @Override
    public Appointment update(Appointment appointment) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Appointment updatedAppointment = null;
        try {
            updatedAppointment = (Appointment) session.merge(appointment);
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return updatedAppointment;
    }

    @Override
    public void delete(Appointment appointment) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (appointment != null) {
                session.delete(appointment);
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
    public List<Appointment> readAllByCity(City city) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Appointment> appointments = new ArrayList<>();
        try {
            Query<Appointment> query = session.createQuery("FROM Appointment WHERE city = :city");
            query.setParameter("city", city);
            appointments = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appointments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Appointment> readAllByCityAndDate(City city, Date date) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Appointment> appointments = new ArrayList<>();
        try {
            Query<Appointment> query = session.createQuery("FROM Appointment WHERE city = :city AND appointmentDate = :date");
            query.setParameter("city", city);
            query.setParameter("date", date);
            appointments = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appointments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Date> readDatesByCity(City city) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Date> dateList = new ArrayList<>();
        Set<Date> dateSet = new HashSet<>();
        try {
            Query<Date> query = session.createQuery("SELECT appointmentDate FROM Appointment WHERE city = :city");
            query.setParameter("city", city);
            dateList = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (!dateList.isEmpty()) {
            dateSet.addAll(dateList);
        }

        return dateSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String[] readTimeByCityAndDate(City city, Date date) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<String> timeList = new ArrayList<>();
        try {
            Query<String> query = session.createQuery("SELECT appointmentTime FROM Appointment WHERE city = :city AND appointmentDate = :date");
            query.setParameter("city", city);
            query.setParameter("date", date);
            timeList = query.list();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return timeList.toArray(new String[0]);
    }
}