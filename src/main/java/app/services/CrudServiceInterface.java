package app.services;

import app.domain.Entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CrudServiceInterface<T extends Entity> {

    T create(T t);

    List<T> readAll(Specification<T> spec, Pageable pageable);

    T readById(long id);

    T update(long id, T t);

    void delete(long id);

}