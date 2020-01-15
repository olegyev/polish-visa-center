package app.dao;

import app.entities.Entity;

import javax.persistence.PersistenceException;
import java.util.List;

public interface AbstractDaoInterface<T extends Entity> {
    long create(T t) throws PersistenceException;
    List<T> readAll() throws PersistenceException;
    T readById(long id) throws PersistenceException;
    T update(T t) throws PersistenceException;
    void delete(T t) throws PersistenceException;
}