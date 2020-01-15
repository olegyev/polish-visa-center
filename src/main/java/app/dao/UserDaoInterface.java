package app.dao;

import app.entities.User;

import javax.persistence.PersistenceException;
import java.util.List;

public interface UserDaoInterface<T extends User> extends AbstractDaoInterface<T> {
    T authenticate(String patternEmail, String patternPassword) throws PersistenceException;
    T readByEmail(String patternEmail) throws PersistenceException;
    List<T> readByLastName(String patternLastName) throws PersistenceException;
    long getIdByEmail(String patternEmail) throws PersistenceException;
}