package app.dao;

import app.entities.User;

import java.util.List;

public abstract class UserDao<T extends User> extends AbstractDao<T> {
    public abstract T authenticate(String patternEmail, String patternPassword);

    public abstract T readByEmail(String patternEmail);

    public abstract List<T> readByLastName(String patternLastName);

    public abstract long getIdByEmail(String patternEmail);
}