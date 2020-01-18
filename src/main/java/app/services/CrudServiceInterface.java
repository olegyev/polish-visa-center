package app.services;

import app.domain.Entity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CrudServiceInterface<T extends Entity> {

    T create(T t);
    List<T> readAll();
    T readById(long id);
    T update(T tFromDb, T tFromClient);
    void delete(long id);

}