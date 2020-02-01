package app.services;

import app.domain.User;

import java.util.List;

public interface UserServiceInterface<T extends User> extends CrudServiceInterface<T> {

    T readByEmail(String email);

    List<T> readByLastName(String lastName);

}