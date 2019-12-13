package app.dao;

import app.entities.Entity;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDao<T extends Entity> {
    protected Connection connection;

    void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean create(T t);
    public abstract List<T> readAll();
    public abstract T readById(long id);
    public abstract T update(T t);
    public abstract boolean deleteById(long id);
}