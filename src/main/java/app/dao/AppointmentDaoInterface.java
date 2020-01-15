package app.dao;

import app.entities.Appointment;
import app.entities.enums.City;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AppointmentDaoInterface extends AbstractDaoInterface<Appointment> {
    List<Appointment> readAllByCity(City city) throws PersistenceException;
    List<Appointment> readAllByCityAndDate(City city, Date date) throws PersistenceException;
    Set<Date> readDatesByCity(City city) throws PersistenceException;
    String[] readTimeByCityAndDate(City city, Date date) throws PersistenceException;
}