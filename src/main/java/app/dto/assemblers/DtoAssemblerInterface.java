package app.dto.assemblers;

import app.domain.Entity;
import app.dto.AbstractDto;

import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface DtoAssemblerInterface<T extends Entity, S extends AbstractDto> {

    S toModel(T t);

    CollectionModel<S> toCollectionModel(List<T> t);

}