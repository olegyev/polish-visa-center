package app.services;

import app.domain.User;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServiceInterface<T extends User> extends CrudServiceInterface<T> {

    T readByEmail(String email);

    List<T> readByLastName(String lastName);

}